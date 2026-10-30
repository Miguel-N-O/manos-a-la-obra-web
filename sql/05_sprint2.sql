-- Migración Sprint 2 (HU-02, HU-04, HU-09, HU-11 y HU-12) (30/10/2026).
-- Para quien ya tiene la base creada con 02_esquema_uuid.sql, 03_datos_iniciales.sql y 04_datos_prueba.sql.
-- No borra datos. Correr UNA sola vez. Quien cree la base desde cero NO necesita este script
-- (02_esquema_uuid.sql ya trae estos cambios).
USE manos_a_la_obra;

-- ---------- Tecnico ----------
-- HU-02: la fila del técnico se crea al registrarse, antes de tener perfil (HU-04).
-- Por eso especialidad, barrio y tarifa pueden quedar vacíos. La búsqueda usa JOIN con
-- Servicio y Barrio, así que un técnico sin perfil no aparece en los resultados (HU-02 CA-05).
ALTER TABLE Tecnico
  MODIFY servicio_id CHAR(36) NULL,
  MODIFY barrio_id   CHAR(36) NULL,
  MODIFY tarifa      DECIMAL(10,2) NULL,
  MODIFY documento   VARCHAR(30) NULL DEFAULT NULL;

-- Un documento vacío no cuenta como documento: evita choques con la restricción UNIQUE
UPDATE Tecnico SET documento = NULL WHERE documento = '';

ALTER TABLE Tecnico
  ADD COLUMN tipo_documento VARCHAR(2)   NULL AFTER usuario_id,  -- HU-02: CC | CE
  ADD COLUMN ruta_documento VARCHAR(255) NULL AFTER documento,   -- HU-02: archivo JPG, PNG o PDF
  ADD COLUMN tipo_tarifa    VARCHAR(12)  NULL AFTER tarifa,      -- HU-04: POR_HORA | POR_TRABAJO
  ADD COLUMN descripcion    VARCHAR(500) NULL AFTER experiencia, -- HU-04: "Sobre mí"
  ADD CONSTRAINT uk_tecnico_documento UNIQUE (documento),
  ADD CONSTRAINT ck_tecnico_tipo_documento CHECK (tipo_documento IN ('CC', 'CE')),
  ADD CONSTRAINT ck_tecnico_tipo_tarifa CHECK (tipo_tarifa IN ('POR_HORA', 'POR_TRABAJO')),
  ADD CONSTRAINT ck_tecnico_experiencia_maxima CHECK (experiencia <= 60);

-- Completa los técnicos que ya existen (datos de prueba)
UPDATE Tecnico SET tipo_documento = 'CC' WHERE tipo_documento IS NULL AND documento IS NOT NULL;
UPDATE Tecnico SET tipo_tarifa = 'POR_TRABAJO' WHERE tipo_tarifa IS NULL AND tarifa IS NOT NULL;

-- ---------- Solicitud ----------
-- Hasta HU-09 ningún código crea solicitudes, así que la tabla debería estar vacía.
-- Si tienes solicitudes insertadas a mano, bórralas antes (DELETE FROM Solicitud;).
ALTER TABLE Solicitud
  ADD COLUMN barrio_id      CHAR(36)    NOT NULL AFTER tecnico_id,  -- HU-09: barrio donde se hará el trabajo
  ADD COLUMN motivo_rechazo VARCHAR(30) NULL AFTER fecha_hora,      -- HU-12: opcional
  ADD COLUMN respondida_en  DATETIME    NULL AFTER notificada,      -- HU-11 y HU-12: fecha de la respuesta
  ADD CONSTRAINT fk_solicitud_barrio_id FOREIGN KEY (barrio_id) REFERENCES Barrio (id),
  ADD CONSTRAINT ck_solicitud_motivo_rechazo CHECK (motivo_rechazo IN
    ('SIN_DISPONIBILIDAD', 'FUERA_DE_ESPECIALIDAD', 'FUERA_DE_ZONA', 'OTRO')),
  ADD INDEX idx_solicitud_tecnico_estado (tecnico_id, estado_solicitud_id),
  ADD INDEX idx_solicitud_ciudadano (ciudadano_id, creada_en);
