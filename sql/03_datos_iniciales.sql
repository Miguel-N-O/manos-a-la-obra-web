USE manos_a_la_obra;

INSERT INTO Rol (nombre) VALUES ('CIUDADANO'), ('TECNICO'), ('ADMIN');
INSERT INTO EstadoUsuario (nombre) VALUES ('ACTIVO'), ('SUSPENDIDO'), ('BLOQUEADO');
INSERT INTO EstadoSolicitud (nombre) VALUES ('PENDIENTE'), ('ACEPTADA'), ('RECHAZADA'), ('FINALIZADA');
INSERT INTO Servicio (nombre) VALUES ('Electricista'), ('Plomero'), ('Gasista'), ('Carpintero');
INSERT INTO Ciudad (nombre) VALUES ('Rionegro');
INSERT INTO IndicativoPais (pais, indicativo) VALUES
  ('Colombia', '57'), ('Argentina', '54'), ('Bolivia', '591'), ('Brasil', '55'), ('Chile', '56'),
  ('Ecuador', '593'), ('España', '34'), ('Estados Unidos', '1'), ('México', '52'), ('Panamá', '507'),
  ('Paraguay', '595'), ('Perú', '51'), ('Uruguay', '598'), ('Venezuela', '58');
