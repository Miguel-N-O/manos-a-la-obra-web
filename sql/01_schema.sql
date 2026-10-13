-- Manos a la Obra - esquema MySQL (MVP)
CREATE DATABASE IF NOT EXISTS manos_a_la_obra CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE manos_a_la_obra;

CREATE TABLE usuarios (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  nombre        VARCHAR(100) NOT NULL,
  correo        VARCHAR(150) NOT NULL UNIQUE,
  clave_hash    VARCHAR(100) NOT NULL,
  telefono      VARCHAR(20),
  rol           ENUM('CIUDADANO','TECNICO','ADMIN') NOT NULL,
  estado        ENUM('ACTIVO','SUSPENDIDO','BLOQUEADO') NOT NULL DEFAULT 'ACTIVO',
  creado_en     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE servicios (
  id      INT AUTO_INCREMENT PRIMARY KEY,
  nombre  VARCHAR(50) NOT NULL UNIQUE   -- electricista, plomero, gasista, carpintero
) ENGINE=InnoDB;

CREATE TABLE tecnicos (
  usuario_id   INT PRIMARY KEY,
  servicio_id  INT NOT NULL,
  ciudad       VARCHAR(80) NOT NULL,
  barrio       VARCHAR(80) NOT NULL,
  tarifa       DECIMAL(10,2) NOT NULL,
  experiencia  INT NOT NULL DEFAULT 0,       -- años
  documento    VARCHAR(30),
  promedio     DECIMAL(2,1) NOT NULL DEFAULT 0,
  FOREIGN KEY (usuario_id)  REFERENCES usuarios(id),
  FOREIGN KEY (servicio_id) REFERENCES servicios(id),
  INDEX idx_ciudad_servicio (ciudad, servicio_id)
) ENGINE=InnoDB;

CREATE TABLE solicitudes (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  ciudadano_id   INT NOT NULL,
  tecnico_id     INT NOT NULL,
  descripcion    VARCHAR(500) NOT NULL,
  fecha_hora     DATETIME NOT NULL,
  estado         ENUM('PENDIENTE','ACEPTADA','RECHAZADA','FINALIZADA') NOT NULL DEFAULT 'PENDIENTE',
  notificada     BOOLEAN NOT NULL DEFAULT FALSE,
  creada_en      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (ciudadano_id) REFERENCES usuarios(id),
  FOREIGN KEY (tecnico_id)   REFERENCES usuarios(id)
) ENGINE=InnoDB;

CREATE TABLE calificaciones (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  solicitud_id  INT NOT NULL UNIQUE,
  estrellas     TINYINT NOT NULL CHECK (estrellas BETWEEN 1 AND 5),
  comentario    VARCHAR(300),
  visible       BOOLEAN NOT NULL DEFAULT TRUE,   -- moderación
  creada_en     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (solicitud_id) REFERENCES solicitudes(id)
) ENGINE=InnoDB;

INSERT INTO servicios (nombre) VALUES ('Electricista'),('Plomero'),('Gasista'),('Carpintero');
