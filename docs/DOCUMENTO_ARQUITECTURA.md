# SISTEMA DE GESTIÓN CLÍNICA
## Documento de Arquitectura de Software

---

**Universidad:** UNIMINUTO  
**Asignatura:** Programación Web  
**Estudiante:** Giovanni Mora Jaimes  
**Código:** 80869  
**Fecha:** Noviembre 2025  
**Versión:** 1.0.0

---

## TABLA DE CONTENIDOS

1. [Introducción](#introducción)
2. [Arquitectura del Sistema](#arquitectura-del-sistema)
3. [Diagramas UML](#diagramas-uml)
4. [Análisis: Monolítico vs Microservicios](#análisis-monolítico-vs-microservicios)
5. [Componentes Principales](#componentes-principales)
6. [Módulos de Seguridad](#módulos-de-seguridad)
7. [Flujos de Procesos](#flujos-de-procesos)
8. [Tecnologías Implementadas](#tecnologías-implementadas)
9. [Conclusiones](#conclusiones)

---

## 1. INTRODUCCIÓN

### 1.1 Propósito del Documento

Este documento presenta la arquitectura de software del **Sistema de Gestión Clínica**, un sistema web desarrollado para la administración integral de consultas médicas, incluyendo gestión de pacientes, médicos, citas, medicamentos y auditoría de seguridad.

### 1.2 Alcance del Sistema

El sistema implementa las siguientes funcionalidades principales:

- **Gestión de Usuarios y Autenticación**: Control de acceso con JWT, roles y permisos
- **Control de Intentos de Login**: Máximo 3 intentos fallidos con bloqueo temporal de 5 minutos
- **Recuperación de Contraseñas**: Envío de contraseña temporal por email con token de 24 horas
- **Auditoría Completa**: Registro de eventos de seguridad (login exitoso/fallido, bloqueos, recuperación)
- **Gestión Clínica**: Administración de pacientes, médicos, especialidades, citas, medicamentos y recetas
- **Visualización de Auditoría**: Consulta de logs con filtros por fecha, usuario, tipo de evento y nivel

### 1.3 Objetivos de Seguridad

- Protección contra ataques de fuerza bruta mediante bloqueo temporal
- Trazabilidad completa de eventos mediante auditoría
- Encriptación de contraseñas con BCrypt (factor de trabajo 10)
- Tokens JWT con expiración de 24 horas
- Recuperación segura de contraseñas sin revelar información sensible

---

## 2. ARQUITECTURA DEL SISTEMA

### 2.1 Patrón Arquitectónico

El sistema utiliza una **arquitectura en capas (Layered Architecture)** con separación clara de responsabilidades:

**Capa 1 - Presentación (Frontend)**
- Framework: Angular 19 con componentes standalone
- Responsabilidad: Interfaz de usuario, validaciones del lado del cliente
- Comunicación: HTTP/REST hacia el backend

**Capa 2 - API REST (Controllers)**
- Framework: Spring Boot 2.7.18
- Responsabilidad: Endpoints REST, validación de peticiones, manejo de respuestas HTTP
- Documentación: Swagger/OpenAPI 3.0

**Capa 3 - Lógica de Negocio (Services)**
- Responsabilidad: Reglas de negocio, validaciones complejas, orquestación de operaciones
- Componentes: AutenticarService, AuditoriaService, PasswordRecoveryService, etc.

**Capa 4 - Persistencia (Repositories)**
- Framework: Spring Data JPA + Hibernate 5.6.15
- Responsabilidad: Acceso a datos, consultas a base de datos
- Patrón: Repository Pattern

**Capa 5 - Base de Datos**
- Motor: MySQL 8.0.43
- Responsabilidad: Almacenamiento persistente de datos
- Schema: `clinica` con 12 tablas relacionales

### 2.2 Comunicación entre Capas

```
[Angular Frontend] 
    ↓ HTTP/REST (JSON)
[Spring Boot Controllers] 
    ↓ Llamadas a métodos
[Services - Lógica de Negocio]
    ↓ JPA/Hibernate
[Repositories]
    ↓ JDBC
[MySQL Database]
```

**Flujo de Autenticación:**
1. Usuario ingresa credenciales en formulario Angular
2. POST `/auth/login` → AutenticarApiController
3. AutenticarService valida credenciales con BCrypt
4. Si es válido: genera JWT y retorna LoginRs
5. Frontend almacena token en localStorage
6. Requests subsecuentes incluyen header `Authorization: Bearer <token>`

### 2.3 Componentes Externos

- **Gmail SMTP**: Envío de emails para recuperación de contraseñas
- **Swagger UI**: Documentación interactiva de la API REST

---

## 3. DIAGRAMAS UML

### 3.1 Diagrama de Clases

**Descripción**: Muestra las entidades principales del dominio, sus atributos y relaciones.

**Entidades Principales:**
- **Usuario**: username, password_hash, email, rol, intentos_fallidos, bloqueado_hasta
- **Medico**: extends Usuario, especialidad, licencia_medica
- **Paciente**: nombre, documento, fecha_nacimiento, telefono, direccion
- **Cita**: fecha_hora, estado, motivo_consulta
- **AuditoriaLog**: tipo_evento, nivel, descripcion, ip_address, fecha_hora

**Relaciones:**
- Usuario 1 → N AuditoriaLog
- Medico 1 → N Cita
- Paciente 1 → N Cita
- Cita 1 → 1 Receta
- Receta N → N Medicamento

![Diagrama de Clases](diagrama-clases.png)

*Nota: Para visualizar este diagrama, utilizar el archivo diagrama-clases.puml con PlantUML*

### 3.2 Diagrama de Despliegue

**Descripción**: Muestra la infraestructura física y lógica del sistema en producción.

**Nodos:**
- **Cliente (Navegador)**: Ejecuta aplicación Angular
- **Servidor Backend**: Spring Boot en puerto 8000
- **Servidor Base de Datos**: MySQL en puerto 3306
- **Servidor SMTP**: Gmail para envío de emails

**Protocolos:**
- HTTP/REST entre Cliente y Backend
- JDBC entre Backend y MySQL
- SMTP entre Backend y Gmail

![Diagrama de Despliegue](diagrama-despliegue.png)

*Nota: Para visualizar este diagrama, utilizar el archivo diagrama-despliegue.puml con PlantUML*

### 3.3 Diagrama de Arquitectura (5 Capas)

**Descripción**: Vista detallada de la arquitectura en capas con componentes específicos.

**Capas visualizadas:**
1. **Presentación**: LoginComponent, AuditoriaComponent, PacienteComponent
2. **API REST**: AutenticarApiController, AuditoriaApiController, PasswordRecoveryApiController
3. **Lógica de Negocio**: AutenticarService, AuditoriaService, PasswordRecoveryService
4. **Persistencia**: UsuarioRepository, AuditoriaLogRepository (JPA)
5. **Datos**: Tablas MySQL (usuario, auditoria_log, paciente, cita, medicamento)

**Componentes Transversales:**
- Seguridad: JWT, BCrypt, Guards
- Email: JavaMailSender
- Documentación: Swagger UI

![Diagrama de Arquitectura](diagrama-arquitectura.png)

*Nota: Para visualizar este diagrama, utilizar el archivo diagrama-arquitectura.puml con PlantUML*

---

## 4. ANÁLISIS: MONOLÍTICO VS MICROSERVICIOS

### 4.1 Arquitectura Actual: Monolítica

**Definición**: Todo el código del backend está en una sola aplicación Spring Boot que se despliega como un único archivo JAR.

**Ventajas implementadas:**
- ✅ Desarrollo más rápido y simple
- ✅ Fácil de probar localmente
- ✅ Despliegue sencillo (un solo JAR)
- ✅ Menor complejidad operacional
- ✅ Transacciones ACID simples (todo en una BD)
- ✅ Debugging más fácil

**Desventajas del monolito:**
- ❌ Escalabilidad limitada (hay que escalar todo, no módulos específicos)
- ❌ Acoplamiento entre módulos
- ❌ Despliegues de "todo o nada"
- ❌ Tecnología única (Java/Spring para todo)

### 4.2 Alternativa: Microservicios

**¿Cómo sería con microservicios?**

```
Microservicio 1: Autenticación y Seguridad
  - Login, JWT, Auditoría, Recuperación de Contraseña
  - Base de datos: usuarios, auditoria_log, sessions

Microservicio 2: Gestión Clínica
  - Pacientes, Médicos, Especialidades
  - Base de datos: paciente, medico, especializacion

Microservicio 3: Citas y Consultas
  - Agendamiento, Consultas, Recetas
  - Base de datos: cita, receta, medicamento

API Gateway
  - Punto único de entrada
  - Enrutamiento, balanceo de carga
```

**Ventajas de microservicios:**
- ✅ Escalabilidad independiente por servicio
- ✅ Tecnologías diferentes por servicio
- ✅ Despliegues independientes
- ✅ Equipos especializados por dominio
- ✅ Mejor tolerancia a fallos (aislamiento)

**Desventajas de microservicios:**
- ❌ Mayor complejidad operacional
- ❌ Comunicación de red entre servicios
- ❌ Transacciones distribuidas complejas
- ❌ Debugging más difícil
- ❌ Overhead de infraestructura (orquestación, service discovery)

### 4.3 Justificación de la Arquitectura Elegida

**¿Por qué Monolítica?**

Para el alcance actual del **Sistema de Gestión Clínica**, la arquitectura monolítica es la elección correcta por:

1. **Tamaño del equipo**: Desarrollo individual o equipo pequeño
2. **Complejidad del dominio**: Moderada, no requiere escalado independiente
3. **Volumen de usuarios**: Bajo a medio (clínica pequeña/mediana)
4. **Tiempo de desarrollo**: Limitado (parcial académico)
5. **Madurez organizacional**: Fase inicial del proyecto

**¿Cuándo migrar a microservicios?**

Considerar microservicios cuando:
- Usuarios concurrentes > 10,000
- Equipo de desarrollo > 20 personas
- Necesidad de escalar módulos específicos (ej: citas 10x más que auditoría)
- Diferentes tecnologías por módulo (ej: Python para ML de diagnóstico)
- Múltiples releases por día en módulos independientes

---

## 5. COMPONENTES PRINCIPALES

### 5.1 Módulo de Autenticación

**Componentes:**
- `AutenticarService`: Lógica de login con validación BCrypt
- `AutenticarApiController`: Endpoint POST /auth/login
- `JwtUtil`: Generación y validación de tokens JWT
- `AuthGuard`: Protección de rutas en Angular

**Características de Seguridad:**
- Hash BCrypt con factor 10 (2^10 = 1024 iteraciones)
- JWT con expiración de 24 horas
- Validación de token en cada request
- Logout limpia localStorage

**Código clave:**
```java
// Validación de contraseña con BCrypt
if (passwordEncoder.matches(loginRq.getPassword(), usuario.getPassword())) {
    String token = jwtUtil.generateToken(usuario.getUsername());
    return LoginRs.builder().token(token).username(usuario.getUsername()).build();
}
```

### 5.2 Módulo de Control de Intentos

**Componentes:**
- `AutenticarServiceImpl.validarCredenciales()`: Lógica de bloqueo
- Campos en Usuario: intentos_fallidos, bloqueado_hasta, ultimo_intento
- `AuditoriaLog.loginFallido()`: Registro de intentos fallidos

**Reglas de Negocio:**
- Máximo 3 intentos fallidos consecutivos
- Bloqueo de 5 minutos después del tercer intento
- Contador se resetea después del bloqueo
- Registro de IP en cada intento

**Flujo de validación:**
```
1. Verificar si usuario está bloqueado (bloqueado_hasta > NOW())
   → SI: retornar error con tiempo restante
2. Validar credenciales con BCrypt
   → VÁLIDA: resetear intentos_fallidos = 0, guardar ultimo_acceso
   → INVÁLIDA: incrementar intentos_fallidos
3. Si intentos_fallidos >= 3:
   → bloqueado_hasta = NOW() + 5 minutos
   → Registrar en auditoría: USUARIO_BLOQUEADO
```

### 5.3 Módulo de Recuperación de Contraseñas

**Componentes:**
- `PasswordRecoveryService`: Generación de contraseña temporal
- `EmailService`: Envío de emails via Gmail SMTP
- Campos en Usuario: password_reset_token, token_expiracion

**Flujo de Recuperación:**
```
1. Usuario ingresa username en formulario
2. Sistema valida si usuario existe
3. Genera contraseña temporal de 10 caracteres (letras, números, símbolos)
4. Encripta con BCrypt y actualiza usuario.password_hash
5. Genera token aleatorio de 32 caracteres
6. Establece token_expiracion = NOW() + 24 horas
7. Envía email con contraseña temporal
8. Registra en auditoría: PASSWORD_RECOVERY
9. Retorna mensaje genérico (sin revelar si usuario existe)
```

**Características de Seguridad:**
- Mensaje genérico siempre (no revela si usuario existe)
- Contraseña temporal compleja (10 caracteres)
- Token expira en 24 horas
- Email solo a dirección registrada
- Auditoría completa del proceso

### 5.4 Módulo de Auditoría

**Componentes:**
- `AuditoriaService`: Consulta y filtrado de logs
- `AuditoriaApiController`: Endpoints para visualización
- Tabla auditoria_log: tipo_evento, nivel, descripcion, ip_address, datos_adicionales (JSON)

**Eventos auditados:**
- LOGIN_EXITOSO: Autenticación correcta
- LOGIN_FALLIDO: Credenciales incorrectas
- USUARIO_BLOQUEADO: Exceso de intentos
- PASSWORD_RECOVERY: Solicitud de recuperación

**Consultas implementadas:**
```java
// Filtros disponibles
- Por tipo de evento (LOGIN_EXITOSO, LOGIN_FALLIDO, etc.)
- Por username
- Por nivel (INFO, WARNING, ERROR)
- Por rango de fechas (fechaInicio, fechaFin)
- Por IP address
- Paginación y ordenamiento
```

**Exportación:**
- Formato CSV para análisis en Excel
- Incluye todos los campos relevantes

---

## 6. MÓDULOS DE SEGURIDAD

### 6.1 Encriptación de Contraseñas

**Tecnología**: BCrypt con factor de trabajo 10

**Ventajas de BCrypt:**
- Diseñado específicamente para contraseñas
- Resistente a ataques GPU (lento por diseño)
- Salt automático incluido en el hash
- Factor de trabajo ajustable (10 = 1024 iteraciones)

**Formato del hash:**
```
$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
│  │  │                                                    │
│  │  └─ Salt (22 chars)                                  └─ Hash (31 chars)
│  └─ Factor de trabajo (10)
└─ Algoritmo (2a = BCrypt)
```

**Configuración:**
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10); // Factor 10
}
```

### 6.2 Tokens JWT

**Estructura del token:**
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.signature
│                     │                                                                      │
└─ Header             └─ Payload (username, issued_at, expiration)                          └─ Signature
```

**Payload incluye:**
- `sub`: Username del usuario autenticado
- `iat`: Timestamp de emisión
- `exp`: Timestamp de expiración (24 horas después)

**Validación:**
1. Verificar firma con clave secreta
2. Verificar que no haya expirado (exp > now)
3. Extraer username del payload

### 6.3 Protección de Endpoints

**Backend (Spring Security):**
```java
// Endpoints públicos (sin autenticación)
.antMatchers("/auth/login").permitAll()
.antMatchers("/password-recovery/request").permitAll()
.antMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

// Todos los demás requieren autenticación
.anyRequest().authenticated()
```

**Frontend (Angular Guards):**
```typescript
// Guard verifica si hay token válido
canActivate(): boolean {
  const token = localStorage.getItem('token');
  if (!token || this.isTokenExpired(token)) {
    this.router.navigate(['/login']);
    return false;
  }
  return true;
}
```

### 6.4 Auditoría y Trazabilidad

**Factory Methods para eventos:**
```java
// Login exitoso
AuditoriaLog.loginExitoso(Usuario usuario, String ip)
  → tipo_evento: LOGIN_EXITOSO
  → nivel: INFO
  → descripcion: "Login exitoso"

// Login fallido
AuditoriaLog.loginFallido(String username, String ip, int intentosRestantes)
  → tipo_evento: LOGIN_FALLIDO
  → nivel: WARNING
  → descripcion: "Credenciales incorrectas. Intentos restantes: X"

// Usuario bloqueado
AuditoriaLog.usuarioBloqueado(Usuario usuario, String ip)
  → tipo_evento: USUARIO_BLOQUEADO
  → nivel: ERROR
  → descripcion: "Usuario bloqueado por exceder intentos de login"

// Recuperación de contraseña
AuditoriaLog.recuperacionPassword(Usuario usuario, String ip)
  → tipo_evento: PASSWORD_RECOVERY
  → nivel: INFO
  → descripcion: "Solicitud de recuperación de contraseña"
```

**Captura de IP:**
```java
String ip = request.getHeader("X-Forwarded-For");
if (ip == null) ip = request.getHeader("X-Real-IP");
if (ip == null) ip = request.getRemoteAddr();
```

---

## 7. FLUJOS DE PROCESOS

### 7.1 Flujo de Login con Control de Intentos

```
[Usuario ingresa credenciales]
         ↓
[POST /auth/login con {username, password}]
         ↓
[AutenticarService.autenticar()]
         ↓
[¿Usuario existe?] ─NO→ [Registrar auditoría LOGIN_FALLIDO] → [Error 401]
         ↓ SI
[¿Está bloqueado?] ─SI→ [Calcular tiempo restante] → [Error 403 con bloqueadoHasta]
         ↓ NO
[Validar password con BCrypt]
         ↓
[¿Password correcto?]
    │
    ├─ NO → [intentos_fallidos++]
    │         ↓
    │       [¿intentos >= 3?]
    │         ├─ SI → [bloqueado_hasta = NOW() + 5min]
    │         │       [Auditoría: USUARIO_BLOQUEADO]
    │         │       [Error 403]
    │         └─ NO → [Auditoría: LOGIN_FALLIDO]
    │                 [Error 401 con intentosRestantes]
    │
    └─ SI → [intentos_fallidos = 0]
            [ultimo_acceso = NOW()]
            [ip_ultima_conexion = IP]
            [Generar JWT]
            [Auditoría: LOGIN_EXITOSO]
            [Retornar LoginRs con token]
                    ↓
            [Frontend guarda token en localStorage]
                    ↓
            [Navega a /inicio]
```

### 7.2 Flujo de Recuperación de Contraseña

```
[Usuario hace clic en "¿Olvidaste tu contraseña?"]
         ↓
[Modal solicita username]
         ↓
[Usuario ingresa username]
         ↓
[POST /password-recovery/request con {username}]
         ↓
[PasswordRecoveryService.solicitarRecuperacion()]
         ↓
[¿Usuario existe?]
    │
    ├─ NO → [Retornar mensaje genérico]
    │       [NO registrar en auditoría]
    │       [Respuesta 200 OK]
    │
    └─ SI → [Generar contraseña temporal de 10 caracteres]
            [passwordTemporal = SecureRandom(letras+números+símbolos)]
                    ↓
            [Encriptar con BCrypt]
            [passwordHash = passwordEncoder.encode(passwordTemporal)]
                    ↓
            [Actualizar usuario]
            [password_hash = passwordHash]
            [password_reset_token = random(32)]
            [token_expiracion = NOW() + 24h]
                    ↓
            [Guardar en base de datos]
                    ↓
            [Registrar auditoría: PASSWORD_RECOVERY]
                    ↓
            [Enviar email via Gmail SMTP]
            [Asunto: "Recuperación de Contraseña - Clínica"]
            [Cuerpo: "Tu contraseña temporal es: {passwordTemporal}"]
            [       "Expira en 24 horas"]
                    ↓
            [Retornar mensaje genérico]
            [Respuesta 200 OK]
```

### 7.3 Flujo de Consulta de Auditoría

```
[Usuario autenticado navega a /auditoria]
         ↓
[Componente carga formulario de filtros]
         ↓
[Usuario aplica filtros (opcional)]
  - Tipo de evento (LOGIN_EXITOSO, LOGIN_FALLIDO, etc.)
  - Username
  - Nivel (INFO, WARNING, ERROR)
  - Fecha inicio y fin
         ↓
[GET /auditoria/logs?tipoEvento=X&username=Y&fechaInicio=Z...]
         ↓
[AuditoriaService.consultarLogs(filtros)]
         ↓
[JPA Query con Specification dinámico]
  WHERE tipo_evento = ? 
    AND username = ?
    AND fecha_hora BETWEEN ? AND ?
    AND nivel = ?
         ↓
[Paginación: Page<AuditoriaLog>]
         ↓
[Retornar JSON con logs]
         ↓
[Frontend renderiza tabla con resultados]
         ↓
[¿Usuario exporta CSV?]
    │
    └─ SI → [GET /auditoria/export-csv con mismos filtros]
            [AuditoriaService genera CSV]
            [Content-Type: text/csv]
            [Headers: filename=auditoria_logs.csv]
            [Navegador descarga archivo]
```

---

## 8. TECNOLOGÍAS IMPLEMENTADAS

### 8.1 Backend

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Java | 21 | Lenguaje de programación |
| Spring Boot | 2.7.18 | Framework de aplicación |
| Spring Security | 5.7.11 | Autenticación y autorización |
| Spring Data JPA | 2.7.18 | Acceso a datos |
| Hibernate | 5.6.15 | ORM (Object-Relational Mapping) |
| MySQL Connector | 8.0.33 | Driver JDBC para MySQL |
| BCrypt | - | Encriptación de contraseñas |
| JWT (jjwt) | 0.11.5 | Generación de tokens |
| JavaMail | 1.6.2 | Envío de emails |
| Lombok | 1.18.30 | Reducción de código boilerplate |
| SpringDoc OpenAPI | 1.6.15 | Documentación Swagger |
| Maven | 3.x | Gestión de dependencias |

### 8.2 Frontend

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| Angular | 19 | Framework de frontend |
| TypeScript | 5.x | Lenguaje tipado |
| RxJS | 7.x | Programación reactiva |
| Bootstrap | 5.x | Framework CSS |
| SweetAlert2 | - | Modales y alertas |
| NgxSpinner | - | Spinners de carga |
| Font Awesome | 6.x | Iconos |

### 8.3 Base de Datos

| Elemento | Detalle |
|----------|---------|
| Motor | MySQL 8.0.43 |
| Charset | utf8mb4 |
| Collation | utf8mb4_unicode_ci |
| Storage Engine | InnoDB (transaccional) |
| Schema | clinica |
| Tablas | 12 (usuario, medico, paciente, cita, receta, medicamento, especializacion, session, auditoria_log, password_reset_token, password_temporal, sistema_configuracion) |

**Campos de seguridad en usuario:**
```sql
intentos_fallidos INT DEFAULT 0
bloqueado_hasta DATETIME NULL
ultimo_intento DATETIME NULL
password_reset_token VARCHAR(255) NULL
token_expiracion DATETIME NULL
ultimo_acceso DATETIME NULL
ip_ultima_conexion VARCHAR(45) NULL
```

### 8.4 Herramientas de Desarrollo

- **IDE Backend**: IntelliJ IDEA
- **IDE Frontend**: Visual Studio Code
- **Control de Versiones**: Git / GitHub
- **Gestión de BD**: MySQL Workbench
- **Pruebas de API**: Swagger UI (http://localhost:8000/clinica/v1/swagger-ui.html)
- **Documentación**: PlantUML para diagramas UML

---

## 9. CONCLUSIONES

### 9.1 Logros Implementados

El **Sistema de Gestión Clínica** cumple exitosamente con los requerimientos del parcial:

✅ **Control de Intentos de Login (25 pts)**
- Máximo 3 intentos fallidos
- Bloqueo temporal de 5 minutos
- Registro de IP en cada intento
- Auditoría completa

✅ **Visualización de Auditoría (20 pts)**
- Filtros múltiples (fecha, usuario, tipo evento, nivel)
- Paginación y ordenamiento
- Exportación a CSV
- Estadísticas agregadas

✅ **Recuperación de Contraseñas (25 pts)**
- Generación de contraseña temporal segura
- Envío de email real via Gmail SMTP
- Token de validación con expiración de 24 horas
- Auditoría del proceso
- Mensajes genéricos para evitar revelación de información

✅ **Documentación (15-20 pts)**
- JavaDoc completo en servicios y controladores
- Swagger/OpenAPI 3.0 con documentación interactiva
- 3 Diagramas UML (Clases, Despliegue, Arquitectura)
- Este documento de arquitectura

### 9.2 Arquitectura Elegida

La **arquitectura monolítica en capas** es adecuada para el proyecto actual porque:

1. Simplifica el desarrollo individual
2. Reduce la complejidad operacional
3. Facilita el debugging y pruebas
4. Es suficiente para el volumen esperado de usuarios
5. Permite transacciones ACID simples

La separación en 5 capas (Presentación, API, Lógica, Persistencia, Datos) permite:
- Mantenibilidad y evolución del código
- Testing independiente por capa
- Reutilización de componentes
- Migración futura a microservicios si es necesario

### 9.3 Seguridad Implementada

El sistema implementa múltiples capas de seguridad:

**Nivel 1 - Autenticación**
- BCrypt para contraseñas (factor 10)
- JWT con expiración de 24 horas
- Guards en frontend

**Nivel 2 - Protección contra Ataques**
- Control de intentos (máximo 3)
- Bloqueo temporal (5 minutos)
- Mensajes genéricos (no revelan información)

**Nivel 3 - Auditoría**
- Registro de todos los eventos de seguridad
- Trazabilidad con IP y timestamp
- Visualización para detección de anomalías

**Nivel 4 - Recuperación Segura**
- Contraseña temporal compleja
- Token con expiración
- Email solo a dirección registrada

### 9.4 Mejoras Futuras

**Corto Plazo:**
- [ ] Implementar cambio de contraseña por parte del usuario
- [ ] Agregar autenticación de dos factores (2FA)
- [ ] Notificaciones push de eventos de seguridad
- [ ] Dashboard de estadísticas de auditoría

**Mediano Plazo:**
- [ ] Implementar roles y permisos granulares (RBAC)
- [ ] Caché con Redis para mejorar performance
- [ ] Implementar rate limiting en API
- [ ] Backup automático de base de datos

**Largo Plazo (si escala):**
- [ ] Migración a microservicios
- [ ] Implementar Event Sourcing para auditoría
- [ ] Service Mesh (Istio) para comunicación segura
- [ ] Kubernetes para orquestación de contenedores

### 9.5 Lecciones Aprendidas

1. **BCrypt vs Hash Simple**: La inversión en BCrypt previene ataques de fuerza bruta
2. **Auditoría desde el inicio**: Es más fácil implementar auditoría desde el principio que agregarla después
3. **Mensajes genéricos**: No revelar si un usuario existe mejora la seguridad
4. **Swagger/OpenAPI**: Documentar la API acelera el desarrollo del frontend
5. **Arquitectura en Capas**: La separación de responsabilidades facilita el testing y mantenimiento

### 9.6 Conclusión Final

El **Sistema de Gestión Clínica** es una aplicación web completa que demuestra:

- Comprensión de arquitectura de software en capas
- Implementación de seguridad robusta (BCrypt, JWT, control de intentos)
- Desarrollo full-stack con Angular y Spring Boot
- Buenas prácticas de documentación (JavaDoc, Swagger, UML)
- Trazabilidad y auditoría completa

La arquitectura monolítica elegida es apropiada para el alcance actual, pero el diseño en capas permite una migración futura a microservicios si el sistema escala.

---

**Fin del Documento**

*Este documento fue elaborado como parte del parcial de Programación Web - UNIMINUTO 2025*
