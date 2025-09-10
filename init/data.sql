-- =================
-- Datos de prueba
-- =================

-- USUARIOS
INSERT INTO usuario (id, username, password_hash, rol, fecha_creacion, activo) VALUES
(1,'admin.sistema','hash_admin','ADMIN',    CURRENT_TIMESTAMP,1),
(2,'mramirez','hash_medico1','MEDICO',      CURRENT_TIMESTAMP,1),
(3,'lgarcia','hash_medico2','MEDICO',       CURRENT_TIMESTAMP,1),
(4,'cmunoz','hash_pac1','PACIENTE',         CURRENT_TIMESTAMP,1),
(5,'jrojas','hash_pac2','PACIENTE',         CURRENT_TIMESTAMP,1),
(6,'apaez','hash_pac3','PACIENTE',          CURRENT_TIMESTAMP,1)
ON DUPLICATE KEY UPDATE username=VALUES(username);

-- PACIENTES (usuario_id es UNIQUE según el modelo)
INSERT INTO paciente (id, usuario_id, tipo_documento, numero_documento, nombres, apellidos, fecha_nacimiento, genero, telefono, direccion) VALUES
(1, 4, 'CC','1012345678','Camila','Muñoz Reyes','1994-07-12','F','3001112233','Calle 45 #12-34, Bogotá'),
(2, 5, 'CC','1023456789','Juan','Rojas Castaño','1990-03-21','M','3102223344','Carrera 10 #45-67, Medellín'),
(3, 6, 'CC','1034567890','Andrea','Páez Suárez','1997-11-05','F','3203334455','Av. 68 #80-15, Cali')
ON DUPLICATE KEY UPDATE numero_documento=VALUES(numero_documento);

-- MÉDICOS (con usuario_id UNIQUE y registro_profesional único)
INSERT INTO medico (id, usuario_id, tipo_documento, numero_documento, nombres, apellidos, telefono, registro_profesional) VALUES
(1, 2, 'CC','1001001001','María','Ramírez Rincón','3015550001','RP-2001'),
(2, 3, 'CC','1001001002','Luis','García Téllez','3015550002','RP-2002')
ON DUPLICATE KEY UPDATE numero_documento=VALUES(numero_documento);

-- ESPECIALIZACIONES
INSERT INTO especializacion (id, nombre, descripcion) VALUES
(1,'Medicina Interna','Atención integral del adulto'),
(2,'Cardiología','Enfermedades del corazón'),
(3,'Pediatría','Salud de niños y adolescentes')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- RELACIÓN M:N MÉDICO-ESPECIALIZACIÓN
INSERT INTO medico_especializacion (medico_id, especializacion_id) VALUES
(1,1), -- María Ramírez -> Medicina Interna
(1,2), -- María Ramírez -> Cardiología
(2,3)  -- Luis García   -> Pediatría
ON DUPLICATE KEY UPDATE especializacion_id=VALUES(especializacion_id);

-- MEDICAMENTOS
INSERT INTO medicamento (id, nombre, descripcion, presentacion) VALUES
(1,'Acetaminofén','Analgésico/antipirético','Tabletas 500 mg'),
(2,'Ibuprofeno','Antiinflamatorio no esteroideo','Cápsulas 400 mg'),
(3,'Amoxicilina','Antibiótico penicilínico','Suspensión 250 mg/5ml')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- CITAS (estado con ENUM del schema)
INSERT INTO cita (id, paciente_id, medico_id, fecha_hora, estado, motivo) VALUES
(1,1,1,'2025-09-10 09:00:00','CONFIRMADA','Control general'),
(2,2,1,'2025-09-11 10:30:00','PENDIENTE','Dolor torácico'),
(3,3,2,'2025-09-12 14:00:00','PENDIENTE','Revisión pediátrica')
ON DUPLICATE KEY UPDATE estado=VALUES(estado);

-- HISTORIAS MÉDICAS
INSERT INTO historia_medica (id, paciente_id, fecha_creacion) VALUES
(1,1,CURRENT_TIMESTAMP),
(2,2,CURRENT_TIMESTAMP),
(3,3,CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE paciente_id=VALUES(paciente_id);

-- ANOTACIONES DE HISTORIA
INSERT INTO anotacion_historia (id, historia_id, medico_id, fecha, descripcion) VALUES
(1,1,1,CURRENT_TIMESTAMP,'Paciente en buen estado general, PA 120/80.'),
(2,2,1,CURRENT_TIMESTAMP,'Refiere dolor torácico leve al esfuerzo. Solicitar ECG.'),
(3,3,2,CURRENT_TIMESTAMP,'Vacunas al día. Recomendaciones nutricionales.')
ON DUPLICATE KEY UPDATE descripcion=VALUES(descripcion);

-- RECETAS
INSERT INTO receta (id, cita_id, medicamento_id, dosis, indicaciones) VALUES
(1,1,1,'500 mg cada 8 horas por 3 días','No exceder 4 g al día'),
(2,2,2,'400 mg cada 12 horas por 5 días','Tomar con alimentos'),
(3,3,3,'5 ml cada 8 horas por 7 días','Agitar antes de usar')
ON DUPLICATE KEY UPDATE dosis=VALUES(dosis);