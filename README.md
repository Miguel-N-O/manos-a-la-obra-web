# Manos a la Obra

Aplicación web (Java 26, Servlets/JSP, MySQL, Bootstrap) - arquitectura MVC.

## Estructura
- `modelo/` entidades · `dao/` acceso a MySQL · `controlador/` servlets · `util/` conexión y cifrado
- `src/main/webapp/` vistas JSP · `sql/01_schema.sql` esquema de base de datos

## Puesta en marcha (Eclipse)
0. Requisitos: JDK 26 y Eclipse reciente con soporte para Java 26 (Eclipse for Enterprise Java).
1. Eclipse for Enterprise Java → File > Import > Maven > Existing Maven Projects → elegir esta carpeta.
2. Ejecutar `sql/01_schema.sql` en MySQL (XAMPP/WAMP) y ajustar `src/main/resources/db.properties`.
3. Servidor: Apache Tomcat 10.1 (Jakarta EE). Run As > Run on Server.
4. Abrir http://localhost:8080/manos-a-la-obra/

## Ramas
Una rama por historia (`feature/HU-01-registro`). Nada directo a `main`; PR revisado por un compañero.
