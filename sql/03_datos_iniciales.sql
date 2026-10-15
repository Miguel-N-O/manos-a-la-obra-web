USE manos_a_la_obra;

INSERT INTO Rol (nombre) VALUES ('CIUDADANO'), ('TECNICO'), ('ADMIN');
INSERT INTO EstadoUsuario (nombre) VALUES ('ACTIVO'), ('SUSPENDIDO'), ('BLOQUEADO');
INSERT INTO EstadoSolicitud (nombre) VALUES ('PENDIENTE'), ('ACEPTADA'), ('RECHAZADA'), ('FINALIZADA');
INSERT INTO Servicio (nombre) VALUES ('Electricista'), ('Plomero'), ('Gasista'), ('Carpintero');
INSERT INTO Ciudad (nombre) VALUES ('Rionegro');
