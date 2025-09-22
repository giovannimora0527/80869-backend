# Servicio de Recetas Médicas - Guía de Pruebas

## Descripción General

El servicio de recetas médicas permite a los médicos crear prescripciones completas asociadas a citas médicas. Incluye validaciones de negocio, autorización, y manejo de errores estructurado.

## Endpoints Disponibles

### 1. Crear Receta Completa

**Endpoint:** `POST /receta/crear-completa`

**Autorización:** Requiere rol MEDICO

**Descripción:** Crea una nueva receta médica con validaciones completas de negocio.

#### Request Body Ejemplo:

```json
{
  "citaId": 1,
  "medicamentos": [
    {
      "medicamentoId": 1,
      "nombre": "Acetaminofén",
      "dosis": "500mg",
      "frecuencia": "Cada 8 horas",
      "duracionDias": 7,
      "indicaciones": "Tomar con alimentos"
    },
    {
      "medicamentoId": 2,
      "nombre": "Ibuprofeno",
      "dosis": "400mg",
      "frecuencia": "Cada 12 horas",
      "duracionDias": 5,
      "indicaciones": "Tomar después de las comidas"
    }
  ],
  "observaciones": "Paciente presenta síntomas de dolor leve",
  "indicacionesGenerales": "Completar el tratamiento según indicaciones",
  "duracionTotalTratamiento": 7
}
```

#### Response Exitoso (201 Created):

```json
{
  "recetaId": 1,
  "fechaCreacion": "2024-01-20T10:30:00",
  "estado": "ACTIVA",
  "observaciones": "Paciente presenta síntomas de dolor leve",
  "indicacionesGenerales": "Completar el tratamiento según indicaciones",
  "duracionTotalTratamiento": 7,
  "medicoInfo": {
    "id": 1,
    "nombre": "Dr. Juan Pérez",
    "especializacion": "Medicina General",
    "registroProfesional": "MED123456"
  },
  "pacienteInfo": {
    "id": 1,
    "nombre": "María García",
    "documento": "12345678",
    "email": "maria.garcia@email.com"
  },
  "medicamentos": [
    {
      "nombre": "Acetaminofén",
      "dosis": "500mg",
      "frecuencia": "Cada 8 horas",
      "duracionDias": 7,
      "indicaciones": "Tomar con alimentos"
    },
    {
      "nombre": "Ibuprofeno",
      "dosis": "400mg",
      "frecuencia": "Cada 12 horas",
      "duracionDias": 5,
      "indicaciones": "Tomar después de las comidas"
    }
  ]
}
```

### 2. Validar Cita para Receta

**Endpoint:** `GET /receta/validar-cita/{citaId}`

**Descripción:** Valida si una cita está en estado apropiado para crear receta.

#### Response Exitoso (200 OK):

```json
true
```

## Casos de Prueba

### Casos Exitosos

#### 1. Crear Receta Simple
- **Cita:** Debe estar en estado FINALIZADA o EN_CURSO
- **Médico:** Debe ser el asignado a la cita
- **Medicamentos:** Al menos uno con datos válidos

#### 2. Crear Receta con Múltiples Medicamentos
- **Validar:** Todos los medicamentos tienen datos completos
- **Verificar:** Orden correcto de medicamentos

#### 3. Validar Cita Exitosa
- **Cita:** Estado válido (FINALIZADA/EN_CURSO)
- **Esperado:** Response `true`

### Casos de Error

#### 1. Cita No Encontrada (404)
```bash
POST /receta/crear-completa
{
  "citaId": 99999,
  "medicamentos": [...]
}
```

**Response:**
```json
{
  "timestamp": "2024-01-20T10:30:00",
  "status": 404,
  "error": "Cita No Encontrada",
  "message": "No se encontró la cita con ID: 99999",
  "path": "/receta/crear-completa"
}
```

#### 2. Estado de Cita Inválido (422)
```bash
POST /receta/crear-completa
# Cita en estado CANCELADA o PROGRAMADA
```

**Response:**
```json
{
  "timestamp": "2024-01-20T10:30:00",
  "status": 422,
  "error": "Estado de Cita Inválido",
  "message": "La cita con ID 1 está en estado 'PROGRAMADA' y no es válida para crear receta. Estados válidos: FINALIZADA, EN_CURSO",
  "path": "/receta/crear-completa"
}
```

#### 3. Médico No Autorizado (403)
```bash
POST /receta/crear-completa
# Médico diferente al asignado a la cita
```

**Response:**
```json
{
  "timestamp": "2024-01-20T10:30:00",
  "status": 403,
  "error": "Médico No Autorizado",
  "message": "El médico con ID 2 no está autorizado para crear receta en la cita 1",
  "path": "/receta/crear-completa"
}
```

#### 4. Receta Duplicada (409)
```bash
POST /receta/crear-completa
# Ya existe receta activa para la cita
```

**Response:**
```json
{
  "timestamp": "2024-01-20T10:30:00",
  "status": 409,
  "error": "Receta Duplicada",
  "message": "Ya existe una receta activa para la cita con ID: 1",
  "path": "/receta/crear-completa"
}
```

#### 5. Datos de Entrada Inválidos (400)
```bash
POST /receta/crear-completa
{
  "citaId": null,
  "medicamentos": []
}
```

**Response:**
```json
{
  "timestamp": "2024-01-20T10:30:00",
  "status": 400,
  "error": "Datos de Entrada Inválidos",
  "message": "Errores de validación: citaId: no puede ser nulo, medicamentos: no puede estar vacío",
  "path": "/receta/crear-completa"
}
```

#### 6. Medicamento No Encontrado (404)
```bash
POST /receta/crear-completa
{
  "citaId": 1,
  "medicamentos": [
    {
      "medicamentoId": 99999,
      "nombre": "Medicamento Inexistente",
      ...
    }
  ]
}
```

**Response:**
```json
{
  "timestamp": "2024-01-20T10:30:00",
  "status": 404,
  "error": "Medicamento No Encontrado",
  "message": "No se encontró el medicamento: Medicamento Inexistente",
  "path": "/receta/crear-completa"
}
```

## Configuración para Pruebas

### Headers Requeridos

```bash
Content-Type: application/json
Authorization: Bearer <token_medico>
```

### Datos de Prueba Necesarios

1. **Cita válida:** Estado FINALIZADA o EN_CURSO
2. **Médico autenticado:** Asignado a la cita
3. **Medicamentos:** Al menos uno en la base de datos
4. **Paciente:** Asociado a la cita

### Herramientas Recomendadas

- **Postman:** Para pruebas manuales
- **curl:** Para pruebas automatizadas
- **JUnit:** Para pruebas unitarias

## Validaciones Implementadas

1. **Existencia de cita**
2. **Estado válido de cita** (FINALIZADA/EN_CURSO)
3. **Autorización del médico**
4. **Prevención de recetas duplicadas**
5. **Validación de medicamentos**
6. **Validación de datos de entrada**
7. **Manejo de errores estructurado**

## Notas Importantes

- Solo médicos autenticados pueden crear recetas
- Una cita solo puede tener una receta activa
- Los medicamentos pueden ser por ID (existentes) o texto libre
- Todas las validaciones se realizan de forma transaccional
- Los errores están estructurados y categorizados