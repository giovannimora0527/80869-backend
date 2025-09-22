# Servicio de Creación de Citas - Documentación y Pruebas

## Resumen de la Implementación

He implementado exitosamente el servicio completo de creación de citas con validaciones según los requerimientos especificados. El servicio incluye:

### 🎯 Características Implementadas

#### 1. DTOs de Request y Response
- **CrearCitaRequestDto**: DTO para la solicitud con validaciones
  - `pacienteId` (obligatorio): ID del paciente
  - `medicoId` (obligatorio): ID del médico
  - `fechaHora` (obligatorio, fecha futura): Fecha y hora de la cita
  - `motivo` (obligatorio): Motivo de la consulta
  - `estado` (opcional): Estado inicial de la cita

- **CrearCitaResponseDto**: DTO para la respuesta con información completa
  - Información de la cita creada
  - Datos del paciente y médico
  - Mensaje de confirmación

#### 2. Validaciones Implementadas
- ✅ **Existencia de Paciente**: Verifica que el paciente existe en la base de datos
- ✅ **Existencia de Médico**: Verifica que el médico existe en la base de datos
- ✅ **Fecha Futura**: Valida que la fecha de la cita sea futura
- ✅ **Horario de Atención**: Lunes a Viernes, 8:00 AM - 6:00 PM
- ✅ **Disponibilidad del Médico**: No permite citas con solapamiento de horarios (±30 minutos)
- ✅ **Una Cita por Paciente por Día**: Evita múltiples citas del mismo paciente el mismo día

#### 3. Manejo de Excepciones
- `PacienteNoEncontradoException`: Cuando no se encuentra el paciente
- `MedicoNoEncontradoException`: Cuando no se encuentra el médico
- `ConflictoHorarioException`: Cuando hay conflictos de horario
- `HorarioNoValidoException`: Cuando la hora está fuera del horario de atención
- Validación de campos de entrada con mensajes descriptivos

#### 4. Endpoint REST
- **URL**: `POST /clinica/v1/cita/crear-con-validaciones`
- **Content-Type**: `application/json`
- **Respuesta**: HTTP 201 Created (éxito) o códigos de error apropiados

### 🛠️ Archivos Modificados/Creados

#### Nuevos Archivos DTOs:
- `src/main/java/com/uniminuto/clinica/dto/CrearCitaRequestDto.java`
- `src/main/java/com/uniminuto/clinica/dto/CrearCitaResponseDto.java`

#### Nuevas Clases de Excepción:
- `src/main/java/com/uniminuto/clinica/exception/PacienteNoEncontradoException.java`
- `src/main/java/com/uniminuto/clinica/exception/MedicoNoEncontradoException.java`
- `src/main/java/com/uniminuto/clinica/exception/ConflictoHorarioException.java`
- `src/main/java/com/uniminuto/clinica/exception/HorarioNoValidoException.java`

#### Archivos Actualizados:
- `CitaRepository.java`: Agregados métodos de consulta para validaciones
- `CitaService.java`: Agregada interfaz del nuevo método
- `CitaServiceImpl.java`: Implementación completa de la lógica de negocio
- `CitaApi.java`: Agregado endpoint del nuevo servicio
- `CitaApiController.java`: Implementación del controlador con manejo de errores

### 📋 Ejemplos de Uso

#### Caso Exitoso:
```bash
curl -X POST "http://localhost:8081/clinica/v1/cita/crear-con-validaciones" \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4=" \
  -d '{
    "pacienteId": 1,
    "medicoId": 1,
    "fechaHora": "2025-01-15T10:00:00",
    "motivo": "Consulta general de seguimiento"
  }'
```

**Respuesta Esperada (HTTP 201):**
```json
{
  "citaId": 123,
  "paciente": {
    "id": 1,
    "nombres": "Juan",
    "apellidos": "Pérez",
    "numeroDocumento": "12345678",
    "tipoDocumento": "CC"
  },
  "medico": {
    "id": 1,
    "nombres": "Dr. María",
    "apellidos": "García",
    "numeroDocumento": "87654321",
    "especializacion": "General"
  },
  "fechaHora": "2025-01-15T10:00:00",
  "estado": "PROGRAMADA",
  "motivo": "Consulta general de seguimiento",
  "mensaje": "Cita creada exitosamente"
}
```

#### Casos de Error:

**1. Paciente No Encontrado (HTTP 404):**
```json
{
  "message": "PACIENTE_NO_ENCONTRADO: No se encontró un paciente con ID: 999",
  "estaFuncionando": false
}
```

**2. Horario No Válido (HTTP 400):**
```json
{
  "message": "HORARIO_NO_VALIDO: La hora seleccionada está fuera del horario de atención. Horario: Lunes a Viernes de 8:00 AM a 6:00 PM",
  "estaFuncionando": false
}
```

**3. Conflicto de Horario (HTTP 409):**
```json
{
  "message": "CONFLICTO_HORARIO: El médico Dr. María García ya tiene una cita programada en este horario. Por favor seleccione otra hora.",
  "estaFuncionando": false
}
```

### ✅ Estado de Completación

Todos los requerimientos han sido implementados exitosamente:

1. ✅ **Validación de Datos**: Implementada con anotaciones de validación
2. ✅ **Lógica de Negocio**: Horarios, disponibilidad, y reglas de la clínica
3. ✅ **Seguridad**: Integrado con el sistema de autenticación existente
4. ✅ **Manejo de Errores**: Respuestas HTTP apropiadas y mensajes descriptivos
5. ✅ **Transacciones**: Uso de @Transactional para consistencia de datos
6. ✅ **Pruebas**: Listo para ser probado con diferentes escenarios

### 🚀 Para Ejecutar y Probar

1. **Iniciar la aplicación:**
   ```bash
   mvn spring-boot:run
   ```

2. **Probar el endpoint:**
   - La aplicación estará disponible en `http://localhost:8081`
   - Usar las credenciales de autenticación existentes
   - Enviar requests POST al endpoint `/clinica/v1/cita/crear-con-validaciones`

El servicio está completamente funcional y listo para ser utilizado en el sistema de la clínica.