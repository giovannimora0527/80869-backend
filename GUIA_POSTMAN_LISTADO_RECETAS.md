# Guía de Uso - Colección Postman Listado de Recetas

## Descripción General

Esta colección contiene todas las pruebas necesarias para validar el servicio de listado de recetas médicas implementado. Incluye diferentes escenarios de filtrado, paginación y ordenamiento.

## Configuración Inicial

### Variables de Colección

Antes de ejecutar las pruebas, configure las siguientes variables en la pestaña "Variables" de la colección:

- `base_url`: URL base del servicio (por defecto: `http://localhost:8081/clinica/v1`)
- `paciente_id`: ID de un paciente existente para pruebas (por defecto: `1`)
- `medico_id`: ID de un médico existente para pruebas (por defecto: `1`)
- `medicamento_id`: ID de un medicamento existente para pruebas (por defecto: `1`)

### Requisitos Previos

1. **Aplicación en ejecución**: El servicio debe estar ejecutándose en el puerto configurado
2. **Datos de prueba**: Asegúrese de tener al menos:
   - 1 paciente registrado
   - 1 médico registrado
   - 1 medicamento registrado
   - Algunas recetas creadas previamente

## Estructura de la Colección

### 1. Listados Básicos
Casos fundamentales de listado sin filtros específicos:

- **Listar Todas las Recetas**: Configuración por defecto (20 registros)
- **Listar con Paginación**: Control personalizado de página y tamaño
- **Listar con Ordenamiento**: Especifica campo y dirección de ordenamiento

### 2. Filtros por Entidad
Filtros específicos por entidades relacionadas:

- **Filtrar por Paciente**: Recetas de un paciente específico
- **Filtrar por Médico**: Recetas prescritas por un médico
- **Filtrar por Medicamento**: Recetas que contienen un medicamento

### 3. Filtros por Fecha
Control temporal de las consultas:

- **Filtrar por Fecha Desde**: Recetas creadas después de una fecha
- **Filtrar por Rango de Fechas**: Recetas en un período específico
- **Últimos 7 Días**: Ejemplo de consulta temporal común

### 4. Búsqueda por Texto
Búsquedas de contenido en campos de texto:

- **Buscar en Dosis**: Encuentra texto en el campo dosis
- **Buscar Medicamento**: Localiza nombres de medicamentos
- **Buscar en Indicaciones**: Busca en las indicaciones médicas

### 5. Filtros Combinados
Combinaciones avanzadas de múltiples filtros:

- **Paciente + Fecha**: Combina filtros de entidad y temporales
- **Médico + Medicamento + Paginación**: Múltiples filtros con control de página
- **Filtro Completo**: Ejemplo con todos los parámetros disponibles

### 6. Endpoints Específicos
Endpoints dedicados para consultas especializadas:

- **Recetas por Paciente**: Endpoint `/paciente/{id}` con y sin paginación
- **Recetas por Médico**: Endpoint `/medico/{id}` con y sin paginación

### 7. Endpoints Legacy
Endpoints del sistema anterior para compatibilidad:

- **Listar Recetas (Método Anterior)**: Sin paginación ni filtros
- **Validar Cita para Receta**: Validación de relación cita-receta

## Casos de Prueba Recomendados

### Caso 1: Validación Básica
1. Ejecutar "Listar Todas las Recetas"
2. Verificar que devuelve resultado paginado
3. Confirmar estructura del DTO de respuesta

### Caso 2: Paginación
1. Ejecutar "Listar con Paginación" con `tamano=5`
2. Verificar que devuelve máximo 5 registros
3. Probar diferentes valores de `pagina`

### Caso 3: Filtros Individuales
1. Probar cada filtro por separado
2. Verificar que los resultados coinciden con el filtro aplicado
3. Confirmar que campos vacíos no causan errores

### Caso 4: Búsqueda de Texto
1. Usar textos que existan en las recetas
2. Verificar coincidencias parciales
3. Probar búsquedas sin resultados

### Caso 5: Combinaciones
1. Ejecutar filtros combinados
2. Verificar que se aplican todos los filtros
3. Confirmar lógica AND entre filtros

## Parámetros de Query Disponibles

| Parámetro | Tipo | Descripción | Ejemplo |
|-----------|------|-------------|---------|
| `pacienteId` | Long | ID del paciente | `1` |
| `medicoId` | Long | ID del médico | `2` |
| `medicamentoId` | Long | ID del medicamento | `3` |
| `fechaDesde` | LocalDateTime | Fecha inicio (ISO) | `2024-01-01T00:00:00` |
| `fechaHasta` | LocalDateTime | Fecha fin (ISO) | `2024-12-31T23:59:59` |
| `texto` | String | Búsqueda en dosis/indicaciones | `500mg` |
| `pagina` | Integer | Número de página (0-based) | `0` |
| `tamano` | Integer | Registros por página (1-100) | `20` |
| `ordenarPor` | String | Campo de ordenamiento | `fechaCreacionRegistro` |
| `direccion` | String | Dirección del orden | `ASC` / `DESC` |

## Estructura de Respuesta Esperada

```json
{
  "content": [
    {
      "id": 1,
      "dosis": "500mg cada 8 horas",
      "indicaciones": "Tomar con alimentos",
      "fechaCreacionRegistro": "2024-09-21T10:30:00",
      "paciente": {
        "id": 1,
        "nombres": "Juan Carlos",
        "apellidos": "Pérez García",
        "numeroDocumento": "12345678"
      },
      "medico": {
        "id": 1,
        "nombres": "Dr. María",
        "apellidos": "González López",
        "especialidad": "Medicina General"
      },
      "medicamento": {
        "id": 1,
        "nombre": "Acetaminofén",
        "descripcion": "Analgésico y antipirético"
      },
      "cita": {
        "id": 1,
        "fechaHora": "2024-09-21T09:00:00",
        "motivo": "Consulta general"
      }
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false
    },
    "pageNumber": 0,
    "pageSize": 20
  },
  "totalElements": 15,
  "totalPages": 1,
  "last": true,
  "first": true,
  "numberOfElements": 15
}
```

## Validaciones de Respuesta

### Códigos de Estado HTTP
- **200 OK**: Consulta exitosa (con o sin resultados)
- **400 Bad Request**: Parámetros inválidos
- **404 Not Found**: Entidad no encontrada (para endpoints específicos)
- **500 Internal Server Error**: Error del servidor

### Validaciones de Contenido
1. **Estructura de paginación**: Verificar campos `totalElements`, `totalPages`, etc.
2. **Datos de recetas**: Confirmar que todos los campos requeridos están presentes
3. **Filtros aplicados**: Verificar que los resultados coinciden con los filtros
4. **Ordenamiento**: Confirmar que los resultados están ordenados correctamente

## Troubleshooting

### Error 400 - Parámetros Inválidos
- Verificar formato de fechas (ISO 8601)
- Confirmar que IDs son números válidos
- Revisar que `tamano` esté entre 1 y 100

### Error 404 - No Encontrado
- Verificar que los IDs de entidades existen en la base de datos
- Confirmar que la URL del endpoint es correcta

### Respuesta Vacía
- Normal si no hay datos que coincidan con los filtros
- Verificar que existen recetas en la base de datos
- Revisar que los filtros no sean demasiado restrictivos

### Tiempos de Respuesta Lentos
- Considerar reducir el tamaño de página
- Verificar la configuración de la base de datos
- Revisar logs del servidor para errores

## Ejemplos de Uso Avanzado

### Buscar Recetas de Paciente en el Último Mes
```
GET /receta/listado?pacienteId=1&fechaDesde=2024-08-21T00:00:00
```

### Recetas de Acetaminofén con Dosis Específica
```
GET /receta/listado?medicamentoId=1&texto=500mg
```

### Últimas 10 Recetas de un Médico
```
GET /receta/medico/1?tamano=10&ordenarPor=fechaCreacionRegistro&direccion=DESC
```

Esta colección proporciona una cobertura completa de funcionalidad para validar el servicio de listado de recetas implementado.