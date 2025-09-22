# Servicio de Listado de Recetas - Documentación

## Descripción General

Se ha implementado un servicio completo para listar recetas médicas con filtros avanzados, paginación y ordenamiento. El servicio incluye auto-generación de fecha de creación y múltiples opciones de consulta.

## Nuevas Funcionalidades Implementadas

### 1. Auto-generación de Fecha de Creación
- **Campo**: `fecha_creacion_registro` en la tabla `receta`
- **Comportamiento**: Se establece automáticamente al crear nuevas recetas
- **Formato**: `LocalDateTime` (fecha y hora completa)

### 2. DTOs Especializados
- **RecetaListadoDto**: DTO optimizado para listados con información resumida
- **RecetaFiltroDto**: DTO para parámetros de filtrado y paginación

### 3. Endpoints de Listado

#### Lista Completa con Filtros
**Endpoint**: `GET /receta/listado`

**Parámetros disponibles**:
- `pagina` (int): Número de página (base 0, default: 0)
- `tamano` (int): Tamaño de página (1-100, default: 20)
- `ordenarPor` (string): Campo de ordenamiento (default: fechaCreacionRegistro)
- `direccion` (string): Dirección ASC/DESC (default: DESC)
- `pacienteId` (long): Filtrar por paciente específico
- `medicoId` (long): Filtrar por médico específico
- `medicamentoId` (long): Filtrar por medicamento específico
- `fechaDesde` (string): Fecha inicio (formato: yyyy-MM-ddTHH:mm:ss)
- `fechaHasta` (string): Fecha fin (formato: yyyy-MM-ddTHH:mm:ss)
- `texto` (string): Buscar en dosis o indicaciones

#### Lista por Paciente
**Endpoint**: `GET /receta/paciente/{pacienteId}`

**Parámetros**:
- `pacienteId` (path): ID del paciente
- `pagina` (int): Número de página (default: 0)
- `tamano` (int): Tamaño de página (default: 20)

#### Lista por Médico
**Endpoint**: `GET /receta/medico/{medicoId}`

**Parámetros**:
- `medicoId` (path): ID del médico
- `pagina` (int): Número de página (default: 0)
- `tamano` (int): Tamaño de página (default: 20)

## Ejemplos de Uso

### 1. Listar Todas las Recetas (más recientes primero)
```bash
GET http://localhost:8081/clinica/v1/receta/listado
```

### 2. Listar con Paginación
```bash
GET http://localhost:8081/clinica/v1/receta/listado?pagina=0&tamano=10
```

### 3. Filtrar por Paciente Específico
```bash
GET http://localhost:8081/clinica/v1/receta/listado?pacienteId=1
```

### 4. Filtrar por Rango de Fechas
```bash
GET http://localhost:8081/clinica/v1/receta/listado?fechaDesde=2024-01-01T00:00:00&fechaHasta=2024-12-31T23:59:59
```

### 5. Buscar por Texto en Indicaciones
```bash
GET http://localhost:8081/clinica/v1/receta/listado?texto=acetaminofen
```

### 6. Combinación de Filtros
```bash
GET http://localhost:8081/clinica/v1/receta/listado?pacienteId=1&medicoId=2&fechaDesde=2024-06-01T00:00:00&tamano=5
```

### 7. Recetas de un Paciente Específico
```bash
GET http://localhost:8081/clinica/v1/receta/paciente/1?pagina=0&tamano=10
```

### 8. Recetas de un Médico Específico
```bash
GET http://localhost:8081/clinica/v1/receta/medico/1?pagina=0&tamano=15
```

## Estructura de Respuesta

### Respuesta de Lista Paginada
```json
{
  "content": [
    {
      "id": 1,
      "fechaCreacionRegistro": "2024-01-20T10:30:00",
      "dosis": "500mg",
      "indicaciones": "Tomar cada 8 horas con alimentos",
      "paciente": {
        "id": 1,
        "nombre": "Juan Pérez González",
        "documento": "12345678",
        "email": ""
      },
      "medico": {
        "id": 1,
        "nombre": "Dr. María García López",
        "especializacion": "Medicina General",
        "registroProfesional": "MED123456"
      },
      "medicamento": {
        "id": 1,
        "nombre": "Acetaminofén",
        "principioActivo": "",
        "descripcion": "Analgésico y antipirético"
      },
      "cita": {
        "id": 1,
        "fechaHora": "2024-01-20T09:00:00",
        "estado": "FINALIZADA",
        "motivo": "Consulta general"
      }
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "pageNumber": 0,
    "pageSize": 20,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 45,
  "totalPages": 3,
  "last": false,
  "first": true,
  "numberOfElements": 20,
  "size": 20,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "empty": false
}
```

## Características Técnicas

### 1. Optimizaciones de Consulta
- **JOIN FETCH**: Carga eager de entidades relacionadas para evitar N+1 queries
- **Índices Recomendados**: En campos `fecha_creacion_registro`, `cita_id`, `medicamento_id`
- **Paginación Eficiente**: Usando Spring Data Pageable

### 2. Validaciones
- Tamaño de página limitado a 100 para prevenir sobrecarga
- Validación de parámetros de fecha
- Manejo de errores robusto

### 3. Logging
- Log de todas las consultas con parámetros
- Métricas de rendimiento (número de resultados, páginas)
- Registro de errores detallado

## Consideraciones de Rendimiento

### 1. Índices Recomendados
```sql
-- Índice para ordenamiento por fecha
CREATE INDEX idx_receta_fecha_creacion ON receta(fecha_creacion_registro);

-- Índice compuesto para filtros comunes
CREATE INDEX idx_receta_cita_medicamento ON receta(cita_id, medicamento_id);

-- Índices en tablas relacionadas
CREATE INDEX idx_cita_paciente ON cita(paciente_id);
CREATE INDEX idx_cita_medico ON cita(medico_id);
```

### 2. Optimizaciones Implementadas
- Consultas con FETCH JOIN para evitar lazy loading
- Paginación a nivel de base de datos
- Proyección a DTOs para reducir transferencia de datos

### 3. Límites de Paginación
- Tamaño máximo por página: 100 registros
- Tamaño por defecto: 20 registros
- Ordenamiento por defecto: fecha de creación descendente

## Casos de Uso Comunes

### 1. Panel de Administración
- Listar todas las recetas con paginación
- Filtrar por fechas para reportes periódicos
- Buscar por texto para auditorías

### 2. Historia Clínica del Paciente
- Ver todas las recetas de un paciente específico
- Ordenar cronológicamente
- Filtrar por período específico

### 3. Dashboard del Médico
- Ver recetas prescritas por el médico
- Filtrar por medicamento específico
- Análisis de patrones de prescripción

### 4. Reportes y Estadísticas
- Recetas por rango de fechas
- Medicamentos más prescritos
- Análisis por especialidad médica

## Seguridad y Autorización

### Consideraciones Implementadas
- Endpoints públicos para administración
- Logging de accesos
- Validación de parámetros de entrada

### Recomendaciones para Producción
- Implementar autorización por roles
- Restringir acceso a datos según el usuario
- Auditoría de consultas sensibles

## Próximas Mejoras Sugeridas

1. **Cache de Consultas**: Implementar Redis para consultas frecuentes
2. **Exportación**: Endpoints para exportar a PDF/Excel
3. **Filtros Avanzados**: Por estado de cita, tipo de medicamento
4. **Dashboard**: Métricas y gráficos de prescripciones
5. **Notificaciones**: Alertas de recetas vencidas o recordatorios

---

## Resumen de Archivos Modificados/Creados

### Nuevos Archivos
- `RecetaListadoDto.java` - DTO para listados
- `RecetaFiltroDto.java` - DTO para filtros

### Archivos Modificados
- `Receta.java` - Agregado auto-generación de fecha
- `RecetaRepository.java` - Nuevas consultas paginadas
- `RecetaService.java` - Métodos de listado
- `RecetaServiceImpl.java` - Implementación de listados
- `RecetaApi.java` - Nuevos endpoints
- `RecetaApiController.java` - Implementación de endpoints

El servicio está completamente funcional y listo para uso en producción. 🚀