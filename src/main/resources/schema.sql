-- Esquema de base de datos para nuevas entidades: cita y receta

-- Tabla cita
CREATE TABLE IF NOT EXISTS cita (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  paciente_id BIGINT NOT NULL,
  medico_id BIGINT NOT NULL,
  fecha_hora DATETIME NOT NULL,
  CONSTRAINT fk_cita_paciente FOREIGN KEY (paciente_id) REFERENCES paciente(id),
  CONSTRAINT fk_cita_medico FOREIGN KEY (medico_id) REFERENCES medico(id)
);

-- Tabla receta
CREATE TABLE IF NOT EXISTS receta (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  cita_id BIGINT NOT NULL,
  descripcion VARCHAR(1024),
  fecha_creacion_registro DATETIME NOT NULL,
  CONSTRAINT fk_receta_cita FOREIGN KEY (cita_id) REFERENCES cita(id)
);


