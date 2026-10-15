# Manos a la Obra

API REST (Java 26, Spring Boot, MySQL) con páginas HTML estáticas (Bootstrap). Arquitectura por capas basada en Sigma_Engineering y DevsStyle_BackEND.

## Estructura (`com.manosalaobra`)
- `controlador/` controladores REST y `respuesta/` · `negocio/fachada/` fachadas y ensambladores DTO
- `negocio/negocio/` reglas de negocio y ensambladores de entidad · `dao/` DAO, fábrica y MySQL
- `dominio/`, `entidad/`, `dto/` · `transversal/` mensajes, excepciones y utilitarios
- `src/main/resources/static/` páginas HTML · `sql/` scripts de base de datos

## Puesta en marcha (Eclipse)
0. Requisitos: JDK 26 y Eclipse reciente con soporte para Java 26.
1. File > Import > Maven > Existing Maven Projects → elegir esta carpeta.
2. Ejecutar en MySQL `sql/02_esquema_uuid.sql` (borra y recrea la base) y luego `sql/03_datos_iniciales.sql`; ajustar `src/main/resources/db.properties`.
   También se puede configurar con las variables `MANOSALAOBRA_DB_URL`, `MANOSALAOBRA_DB_USUARIO` y `MANOSALAOBRA_DB_CLAVE`.
3. Run As > Java Application sobre `ManosALaObraApplication`.
4. Abrir http://localhost:8080/

## Ramas
Una rama por historia (`feature/HU-01-registro`). Nada directo a `main`; PR revisado por un compañero.
