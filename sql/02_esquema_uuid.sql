DROP DATABASE IF EXISTS manos_a_la_obra;
CREATE DATABASE manos_a_la_obra CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE manos_a_la_obra;

CREATE TABLE Rol (
  id      CHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
  nombre  VARCHAR(30) NOT NULL,
  activo  BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT uk_rol_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE EstadoUsuario (
  id      CHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
  nombre  VARCHAR(30) NOT NULL,
  activo  BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT uk_estado_usuario_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE EstadoSolicitud (
  id      CHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
  nombre  VARCHAR(30) NOT NULL,
  activo  BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT uk_estado_solicitud_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE Servicio (
  id      CHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
  nombre  VARCHAR(50) NOT NULL,
  activo  BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT uk_servicio_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE Ciudad (
  id      CHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
  nombre  VARCHAR(80) NOT NULL,
  activo  BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT uk_ciudad_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE Barrio (
  id         CHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
  ciudad_id  CHAR(36) NOT NULL,
  nombre     VARCHAR(80) NOT NULL,
  activo     BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT fk_barrio_ciudad_id FOREIGN KEY (ciudad_id) REFERENCES Ciudad (id),
  CONSTRAINT uk_barrio_ciudad_id_nombre UNIQUE (ciudad_id, nombre)
) ENGINE=InnoDB;

CREATE TABLE IndicativoPais (
  id          CHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
  pais        VARCHAR(60) NOT NULL,
  indicativo  VARCHAR(5) NOT NULL,
  activo      BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT uk_indicativo_pais_pais UNIQUE (pais)
) ENGINE=InnoDB;

CREATE TABLE Usuario (
  id                 CHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
  rol_id             CHAR(36) NOT NULL,
  estado_usuario_id  CHAR(36) NOT NULL,
  nombre             VARCHAR(100) NOT NULL,
  correo             VARCHAR(150) NOT NULL,
  clave_hash         VARCHAR(100) NOT NULL,
  indicativo_pais_id  CHAR(36) NOT NULL,
  numero_telefono    VARCHAR(14) NOT NULL,
  creado_en          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_usuario_rol_id FOREIGN KEY (rol_id) REFERENCES Rol (id),
  CONSTRAINT fk_usuario_estado_usuario_id FOREIGN KEY (estado_usuario_id) REFERENCES EstadoUsuario (id),
  CONSTRAINT fk_usuario_indicativo_pais_id FOREIGN KEY (indicativo_pais_id) REFERENCES IndicativoPais (id),
  CONSTRAINT ck_usuario_numero_telefono CHECK (CHAR_LENGTH(numero_telefono) BETWEEN 7 AND 14),
  CONSTRAINT uk_usuario_correo UNIQUE (correo)
) ENGINE=InnoDB;

CREATE TABLE Tecnico (
  id           CHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
  usuario_id   CHAR(36) NOT NULL,
  servicio_id  CHAR(36) NOT NULL,
  barrio_id    CHAR(36) NOT NULL,
  tarifa       DECIMAL(10,2) NOT NULL,
  experiencia  INT NOT NULL DEFAULT 0,
  documento    VARCHAR(30) NOT NULL DEFAULT '',
  promedio     DECIMAL(2,1) NOT NULL DEFAULT 0,
  CONSTRAINT fk_tecnico_usuario_id FOREIGN KEY (usuario_id) REFERENCES Usuario (id),
  CONSTRAINT fk_tecnico_servicio_id FOREIGN KEY (servicio_id) REFERENCES Servicio (id),
  CONSTRAINT fk_tecnico_barrio_id FOREIGN KEY (barrio_id) REFERENCES Barrio (id),
  CONSTRAINT uk_tecnico_usuario_id UNIQUE (usuario_id),
  CONSTRAINT ck_tecnico_tarifa CHECK (tarifa > 0),
  CONSTRAINT ck_tecnico_experiencia CHECK (experiencia >= 0),
  CONSTRAINT ck_tecnico_promedio CHECK (promedio BETWEEN 0 AND 5),
  INDEX idx_tecnico_servicio_barrio (servicio_id, barrio_id)
) ENGINE=InnoDB;

CREATE TABLE Solicitud (
  id                    CHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
  ciudadano_id          CHAR(36) NOT NULL,
  tecnico_id            CHAR(36) NOT NULL,
  estado_solicitud_id   CHAR(36) NOT NULL,
  descripcion           VARCHAR(500) NOT NULL,
  fecha_hora            DATETIME NOT NULL,
  notificada            BOOLEAN NOT NULL DEFAULT FALSE,
  creada_en             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_solicitud_ciudadano_id FOREIGN KEY (ciudadano_id) REFERENCES Usuario (id),
  CONSTRAINT fk_solicitud_tecnico_id FOREIGN KEY (tecnico_id) REFERENCES Tecnico (id),
  CONSTRAINT fk_solicitud_estado_solicitud_id FOREIGN KEY (estado_solicitud_id) REFERENCES EstadoSolicitud (id)
) ENGINE=InnoDB;

CREATE TABLE Calificacion (
  id            CHAR(36) NOT NULL DEFAULT (UUID()) PRIMARY KEY,
  solicitud_id  CHAR(36) NOT NULL,
  estrellas     TINYINT NOT NULL,
  comentario    VARCHAR(300) NOT NULL DEFAULT '',
  visible       BOOLEAN NOT NULL DEFAULT TRUE,
  creada_en     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_calificacion_solicitud_id FOREIGN KEY (solicitud_id) REFERENCES Solicitud (id),
  CONSTRAINT uk_calificacion_solicitud_id UNIQUE (solicitud_id),
  CONSTRAINT ck_calificacion_estrellas CHECK (estrellas BETWEEN 1 AND 5)
) ENGINE=InnoDB;
