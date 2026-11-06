-- Migración Sprint 3 (HU-16 y HU-17) (06/11/2026).
-- Para quien ya tiene la base creada con los scripts anteriores. No borra datos. Correr UNA sola vez.
-- Quien cree la base desde cero NO necesita este script (02_esquema_uuid.sql ya trae estos cambios).
USE manos_a_la_obra;

-- HU-16: motivo con el que el administrador suspende o bloquea una cuenta
ALTER TABLE Usuario
  ADD COLUMN motivo_estado VARCHAR(200) NULL AFTER numero_telefono;

-- HU-17: motivo con el que el administrador oculta un comentario
ALTER TABLE Calificacion
  ADD COLUMN motivo_moderacion VARCHAR(30) NULL AFTER visible,
  ADD CONSTRAINT ck_calificacion_motivo_moderacion CHECK (motivo_moderacion IN
    ('LENGUAJE_OFENSIVO', 'INFORMACION_PERSONAL', 'CONTENIDO_NO_RELACIONADO', 'OTRO'));
