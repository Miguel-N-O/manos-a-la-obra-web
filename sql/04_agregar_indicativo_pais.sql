-- Migración: indicativo de país y número de teléfono (14/10/2026).
-- Para quien ya tiene la base creada con 02_esquema_uuid.sql y 03_datos_iniciales.sql.
-- Borra los usuarios existentes (eran de prueba). Quien cree la base desde cero NO necesita este script.
USE manos_a_la_obra;

CREATE TABLE IndicativoPais (
  id          CHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
  pais        VARCHAR(60) NOT NULL,
  indicativo  VARCHAR(5) NOT NULL,
  activo      BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT uk_indicativo_pais_pais UNIQUE (pais)
) ENGINE=InnoDB;

INSERT INTO IndicativoPais (pais, indicativo) VALUES
  ('Colombia', '57'), ('Argentina', '54'), ('Bolivia', '591'), ('Brasil', '55'), ('Chile', '56'),
  ('Ecuador', '593'), ('España', '34'), ('Estados Unidos', '1'), ('México', '52'), ('Panamá', '507'),
  ('Paraguay', '595'), ('Perú', '51'), ('Uruguay', '598'), ('Venezuela', '58');

SET SQL_SAFE_UPDATES = 0;
DELETE FROM Usuario;
SET SQL_SAFE_UPDATES = 1;

ALTER TABLE Usuario
  DROP COLUMN telefono,
  ADD COLUMN indicativo_pais_id CHAR(36) NOT NULL,
  ADD COLUMN numero_telefono VARCHAR(14) NOT NULL,
  ADD CONSTRAINT fk_usuario_indicativo_pais_id FOREIGN KEY (indicativo_pais_id) REFERENCES IndicativoPais (id),
  ADD CONSTRAINT ck_usuario_numero_telefono CHECK (CHAR_LENGTH(numero_telefono) BETWEEN 7 AND 14);
