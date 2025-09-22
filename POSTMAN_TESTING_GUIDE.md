# 🚀 Guía de Pruebas en Postman - Servicio de Citas

## ⚙️ Configuración Inicial

### 1. Información del Servidor
- **URL Base**: `http://localhost:8081`
- **Context Path**: `/clinica/v1`
- **Puerto**: 8081
- **Estado**: ✅ Aplicación ejecutándose correctamente

### 2. Autenticación
- **Tipo**: HTTP Basic Authentication
- **Usuario**: `user`
- **Contraseña**: `1b3995f6-a6bf-41bc-8e15-19b12b8aa247` (password generado automáticamente)

---

## 🧪 Pruebas Paso a Paso

### **PRUEBA 1: Crear Cita Exitosa**

#### Configuración de Request:
- **Método**: `POST`
- **URL**: `http://localhost:8081/clinica/v1/cita/crear-con-validaciones`
- **Headers**:
  ```
  Content-Type: application/json
  Authorization: Basic dXNlcjoxYjM5OTVmNi1hNmJmLTQxYmMtOGUxNS0xOWIxMmI4YWEyNDc=
  ```

#### Body (JSON):
```json
{
  "pacienteId": 1,
  "medicoId": 1,
  "fechaHora": "2025-01-15T10:00:00",
  "motivo": "Consulta general de seguimiento"
}
```

#### Respuesta Esperada:
- **Status**: `201 Created`
- **Body**:
```json
{
  "citaId": 1,
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

---

### **PRUEBA 2: Error - Paciente No Encontrado**

#### Body (JSON):
```json
{
  "pacienteId": 999,
  "medicoId": 1,
  "fechaHora": "2025-01-15T10:00:00",
  "motivo": "Consulta de prueba"
}
```

#### Respuesta Esperada:
- **Status**: `404 Not Found`
- **Body**:
```json
{
  "message": "PACIENTE_NO_ENCONTRADO: No se encontró un paciente con ID: 999",
  "estaFuncionando": false
}
```

---

### **PRUEBA 3: Error - Médico No Encontrado**

#### Body (JSON):
```json
{
  "pacienteId": 1,
  "medicoId": 999,
  "fechaHora": "2025-01-15T10:00:00",
  "motivo": "Consulta de prueba"
}
```

#### Respuesta Esperada:
- **Status**: `404 Not Found`
- **Body**:
```json
{
  "message": "MEDICO_NO_ENCONTRADO: No se encontró un médico con ID: 999",
  "estaFuncionando": false
}
```

---

### **PRUEBA 4: Error - Horario No Válido (Fin de Semana)**

#### Body (JSON):
```json
{
  "pacienteId": 1,
  "medicoId": 1,
  "fechaHora": "2025-01-18T10:00:00",
  "motivo": "Consulta de fin de semana"
}
```

#### Respuesta Esperada:
- **Status**: `400 Bad Request`
- **Body**:
```json
{
  "message": "HORARIO_NO_VALIDO: No se pueden programar citas los fines de semana. Horario de atención: Lunes a Viernes de 8:00 AM a 6:00 PM",
  "estaFuncionando": false
}
```

---

### **PRUEBA 5: Error - Horario No Válido (Fuera de Horario)**

#### Body (JSON):
```json
{
  "pacienteId": 1,
  "medicoId": 1,
  "fechaHora": "2025-01-15T19:00:00",
  "motivo": "Consulta nocturna"
}
```

#### Respuesta Esperada:
- **Status**: `400 Bad Request`
- **Body**:
```json
{
  "message": "HORARIO_NO_VALIDO: La hora seleccionada está fuera del horario de atención. Horario: Lunes a Viernes de 8:00 AM a 6:00 PM",
  "estaFuncionando": false
}
```

---

### **PRUEBA 6: Error - Validación de Campos**

#### Body (JSON):
```json
{
  "pacienteId": null,
  "medicoId": 1,
  "fechaHora": "2024-01-15T10:00:00",
  "motivo": ""
}
```

#### Respuesta Esperada:
- **Status**: `400 Bad Request`
- **Body**:
```json
{
  "message": "ERROR_VALIDACION: Errores de validación: pacienteId: El ID del paciente es obligatorio; fechaHora: La fecha de la cita debe ser futura; motivo: El motivo de la cita es obligatorio;",
  "estaFuncionando": false
}
```

---

## 📋 Pasos para Configurar en Postman

### 1. **Crear Nueva Collection**
   - Abrir Postman
   - Crear nueva Collection: "Clínica API - Servicio de Citas"

### 2. **Configurar Authentication**
   - En la Collection, ir a "Authorization"
   - Type: "Basic Auth"
   - Username: `user`
   - Password: `1b3995f6-a6bf-41bc-8e15-19b12b8aa247`

### 3. **Crear Request para Cada Prueba**
   - Agregar nuevo request para cada prueba
   - Configurar método POST
   - URL: `{{baseUrl}}/cita/crear-con-validaciones`
   - Headers: Content-Type: application/json
   - Body: usar los JSON de cada prueba

### 4. **Variables de Environment**
   - Crear environment "Clínica Local"
   - Variable: `baseUrl` = `http://localhost:8081/clinica/v1`

---

## ✅ Checklist de Pruebas

- [ ] **Prueba 1**: Creación exitosa de cita
- [ ] **Prueba 2**: Error paciente no encontrado
- [ ] **Prueba 3**: Error médico no encontrado
- [ ] **Prueba 4**: Error horario fin de semana
- [ ] **Prueba 5**: Error horario fuera de rango
- [ ] **Prueba 6**: Error validación de campos

---

## 🔧 Información Técnica

- **Aplicación**: Spring Boot 2.7.18
- **Base de Datos**: MySQL (conectada y funcionando)
- **Autenticación**: Spring Security habilitada
- **Estado del Servidor**: ✅ ACTIVO en puerto 8081

## 📝 Notas Importantes

1. **Password Temporal**: La contraseña mostrada es generada automáticamente por Spring Security y cambia en cada reinicio
2. **Fechas**: Usar fechas futuras (2025 o posteriores) para evitar errores de validación
3. **IDs Válidos**: Usar IDs que existan en la base de datos (1, 2, 3, etc.)
4. **Formato de Fecha**: Usar formato ISO: `YYYY-MM-DDTHH:mm:ss`

¡Listo para probar! 🚀