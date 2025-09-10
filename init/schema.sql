-- ==========================
-- TABLA DE USUARIOS
-- ==========================
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    -- CHECK (rol IN ('ADMIN','MEDICO','PACIENTE'))  -- intención original
    rol ENUM('ADMIN','MEDICO','PACIENTE') NOT NULL,  -- equivalente que se hace cumplir
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo TINYINT(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================
-- TABLA DE PACIENTES
-- ==========================
CREATE TABLE IF NOT EXISTS paciente (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT UNIQUE,
    tipo_documento VARCHAR(10) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    -- CHECK (genero IN ('M','F','O'))                -- intención original
    genero ENUM('M','F','O'),
    telefono VARCHAR(20),
    direccion TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================
-- TABLA DE MÉDICOS
-- ==========================
CREATE TABLE IF NOT EXISTS medico (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT UNIQUE,
    tipo_documento VARCHAR(10) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    registro_profesional VARCHAR(50) UNIQUE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================
-- ESPECIALIZACIONES
-- ==========================
CREATE TABLE IF NOT EXISTS especializacion (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    descripcion TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS medico_especializacion (
    medico_id INT NOT NULL,
    especializacion_id INT NOT NULL,
    PRIMARY KEY (medico_id, especializacion_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================
-- TABLA DE CITAS
-- ==========================
CREATE TABLE IF NOT EXISTS cita (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    -- CHECK (estado IN ('PENDIENTE','CONFIRMADA','CANCELADA','REALIZADA')) -- intención original
    estado ENUM('PENDIENTE','CONFIRMADA','CANCELADA','REALIZADA') NOT NULL,
    motivo TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================
-- TABLA DE MEDICAMENTOS
-- ==========================
CREATE TABLE IF NOT EXISTS medicamento (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) UNIQUE NOT NULL,
    descripcion TEXT,
    presentacion VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================
-- HISTORIA MÉDICA
-- ==========================
CREATE TABLE IF NOT EXISTS historia_medica (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    paciente_id INT NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================
-- ANOTACIONES DE HISTORIA
-- ==========================
CREATE TABLE IF NOT EXISTS anotacion_historia (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    historia_id INT NOT NULL,
    medico_id INT NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    descripcion TEXT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================
-- RECETAS MÉDICAS
-- ==========================
CREATE TABLE IF NOT EXISTS receta (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    cita_id INT NOT NULL,
    medicamento_id INT NOT NULL,
    dosis TEXT NOT NULL,
    indicaciones TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ==========================
-- ÍNDICES (como en el enunciado)
-- ==========================
CREATE INDEX IF NOT EXISTS idx_paciente_documento ON paciente (numero_documento);
CREATE INDEX IF NOT EXISTS idx_medico_documento ON medico (numero_documento);