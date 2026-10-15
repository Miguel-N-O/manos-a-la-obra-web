package com.manosalaobra.transversal.catalogo;

public final class CatalogoMensajes {

    private CatalogoMensajes() {
    }

    public static final class UtilSQL {
        public static final String USUARIO_ERROR_CONEXION_NO_ESTA_ABIERTA =
                "No pudimos conectarnos con la información en este momento. Intenta de nuevo en unos minutos.";
        public static final String TECNICO_ERROR_CONEXION_NO_ESTA_ABIERTA =
                "La conexión SQL es nula o está cerrada.";
        public static final String USUARIO_ERROR_PROBLEMA_VERIFICANDO_CONEXION =
                "No pudimos comprobar la conexión con la información. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_CERRANDO_CONEXION =
                "Ocurrió un problema al terminar la operación. Revisa que tu solicitud se haya guardado e intenta de nuevo si hace falta.";
        public static final String USUARIO_ERROR_TRANSACCION_YA_INICIADA =
                "No pudimos empezar la operación porque otra todavía estaba en curso. Intenta de nuevo.";
        public static final String TECNICO_ERROR_TRANSACCION_YA_INICIADA =
                "Se intentó iniciar una transacción que ya estaba iniciada.";
        public static final String USUARIO_ERROR_TRANSACCION_NO_INICIADA =
                "No pudimos terminar la operación porque no se había empezado correctamente. Intenta de nuevo.";
        public static final String TECNICO_ERROR_TRANSACCION_NO_INICIADA =
                "Se intentó confirmar o cancelar una transacción que no estaba iniciada.";
        public static final String USUARIO_ERROR_PROBLEMA_INICIANDO_TRANSACCION =
                "No pudimos empezar la operación. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_CONFIRMANDO_TRANSACCION =
                "No pudimos guardar los cambios. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_CANCELANDO_TRANSACCION =
                "No pudimos deshacer la operación fallida. Avisa al administrador si ves datos raros.";

        private UtilSQL() {
        }
    }

    public static final class DAOFactory {
        public static final String USUARIO_ERROR_FACTORIA_NO_HABILITADA =
                "La fuente de datos configurada no está disponible. Avisa al administrador.";
        public static final String TECNICO_ERROR_FACTORIA_NO_HABILITADA =
                "La factoría de DAO solicitada no tiene una implementación habilitada.";

        private DAOFactory() {
        }
    }

    public static final class MySqlDAOFactory {
        public static final String USUARIO_ERROR_ABRIENDO_CONEXION =
                "No pudimos conectarnos con la información. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_NO_CONTROLADO_ABRIENDO_CONEXION =
                "Ocurrió un problema inesperado al conectarnos con la información. Avisa al administrador.";
        public static final String USUARIO_ERROR_CONFIGURACION_BASE_DE_DATOS =
                "La conexión con la información no está configurada. Avisa al administrador.";
        public static final String TECNICO_ERROR_FALTA_CONFIGURACION =
                "Faltan db.url o db.user (en db.properties o en las variables MANOSALAOBRA_DB_URL y MANOSALAOBRA_DB_USUARIO).";

        private MySqlDAOFactory() {
        }
    }

    public static final class UsuarioMySqlDAO {
        public static final String USUARIO_ERROR_CORREO_YA_REGISTRADO = "Ese correo ya está registrado.";
        public static final String USUARIO_ERROR_PROBLEMA_CREANDO_USUARIO =
                "No pudimos guardar tu cuenta. Revisa tus datos e intenta de nuevo.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CREANDO_USUARIO =
                "Ocurrió un problema inesperado al guardar tu cuenta. Intenta de nuevo o avisa al administrador.";
        public static final String USUARIO_ERROR_PROBLEMA_CONSULTANDO_USUARIO =
                "No pudimos consultar la cuenta. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_USUARIO =
                "Ocurrió un problema inesperado al consultar la cuenta. Intenta de nuevo o avisa al administrador.";

        private UsuarioMySqlDAO() {
        }
    }

    public static final class IndicativoPaisMySqlDAO {
        public static final String USUARIO_ERROR_PROBLEMA_CONSULTANDO_INDICATIVO_PAIS =
                "No pudimos consultar los indicativos de país. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_INDICATIVO_PAIS =
                "Ocurrió un problema inesperado al consultar los indicativos de país. Avisa al administrador.";

        private IndicativoPaisMySqlDAO() {
        }
    }

    public static final class IndicativoPaisFachadaImpl {
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_INDICATIVOS_PAIS =
                "No pudimos cargar los indicativos de país. Intenta de nuevo en unos minutos.";

        private IndicativoPaisFachadaImpl() {
        }
    }

    public static final class RolMySqlDAO {
        public static final String USUARIO_ERROR_PROBLEMA_CONSULTANDO_ROL =
                "No pudimos consultar los tipos de cuenta. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_ROL =
                "Ocurrió un problema inesperado al consultar los tipos de cuenta. Avisa al administrador.";

        private RolMySqlDAO() {
        }
    }

    public static final class EstadoUsuarioMySqlDAO {
        public static final String USUARIO_ERROR_PROBLEMA_CONSULTANDO_ESTADO_USUARIO =
                "No pudimos consultar los estados de cuenta. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_ESTADO_USUARIO =
                "Ocurrió un problema inesperado al consultar los estados de cuenta. Avisa al administrador.";

        private EstadoUsuarioMySqlDAO() {
        }
    }

    public static final class UsuarioNegocioImpl {
        public static final String USUARIO_ERROR_CAMPOS_FALTANTES = "Falta completar: %s.";
        public static final String USUARIO_ERROR_CAMPO_NOMBRE = "tu nombre";
        public static final String USUARIO_ERROR_CAMPO_CORREO = "tu correo";
        public static final String USUARIO_ERROR_CAMPO_CLAVE = "tu contraseña";
        public static final String USUARIO_ERROR_NOMBRE_LONGITUD_NO_VALIDA =
                "Tu nombre es muy largo (máximo 100 caracteres).";
        public static final String USUARIO_ERROR_CORREO_FORMATO_NO_VALIDO = "El correo no es válido.";
        public static final String USUARIO_ERROR_CORREO_LONGITUD_NO_VALIDA =
                "Tu correo es muy largo (máximo 150 caracteres).";
        public static final String USUARIO_ERROR_CLAVE_LONGITUD_NO_VALIDA =
                "Tu contraseña es muy corta (mínimo 8 caracteres).";
        public static final String USUARIO_ERROR_CLAVE_LONGITUD_MAXIMA_NO_VALIDA =
                "Tu contraseña es muy larga (máximo 72 caracteres).";
        public static final String USUARIO_ERROR_CAMPO_INDICATIVO_PAIS = "el indicativo de país";
        public static final String USUARIO_ERROR_CAMPO_NUMERO_TELEFONO = "tu teléfono";
        public static final String USUARIO_ERROR_INDICATIVO_PAIS_NO_VALIDO =
                "El indicativo de país no es válido. Elige uno de la lista.";
        public static final String USUARIO_ERROR_NUMERO_TELEFONO_FORMATO_NO_VALIDO =
                "Tu teléfono solo puede tener números, sin el indicativo, espacios ni símbolos.";
        public static final String USUARIO_ERROR_NUMERO_TELEFONO_LONGITUD_NO_VALIDA =
                "Tu teléfono debe tener entre 7 y 14 números.";
        public static final String USUARIO_ERROR_CORREO_YA_REGISTRADO = "Ese correo ya está registrado.";
        public static final String USUARIO_ERROR_ROL_NO_CONFIGURADO =
                "No pudimos crear tu cuenta porque falta configurar los tipos de cuenta. Avisa al administrador.";
        public static final String USUARIO_ERROR_ESTADO_NO_CONFIGURADO =
                "No pudimos crear tu cuenta porque falta configurar los estados de cuenta. Avisa al administrador.";

        public static final String USUARIO_ERROR_DATOS_INICIO_SESION_FALTANTES =
                "Escribe tu correo y tu contraseña.";
        public static final String USUARIO_ERROR_CREDENCIALES_INCORRECTAS = "Correo o contraseña incorrectos.";

        private UsuarioNegocioImpl() {
        }
    }

    public static final class UsuarioFachadaImpl {
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_REGISTRANDO_CIUDADANO =
                "No pudimos crear tu cuenta. Intenta de nuevo en unos minutos.";

        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_INICIANDO_SESION =
                "No pudimos iniciar tu sesión. Intenta de nuevo en unos minutos.";

        private UsuarioFachadaImpl() {
        }
    }

    public static final class AbstractRestController {
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO =
                "Algo salió mal de nuestro lado. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_RECURSO_NO_ENCONTRADO =
                "No encontramos lo que buscabas. Revisa la dirección e intenta de nuevo.";
        public static final String USUARIO_ERROR_DATO_NO_VALIDO =
                "Uno de los datos enviados no es válido. Revisa lo que elegiste e intenta de nuevo.";
        public static final String USUARIO_ERROR_CUERPO_DE_LA_PETICION_INVALIDO =
                "No pudimos leer los datos enviados. Revisa el formulario e intenta de nuevo.";

        private AbstractRestController() {
        }
    }

    public static final class UsuarioController {
        public static final String USUARIO_CIUDADANO_REGISTRADO = "¡Cuenta creada! Ya puedes iniciar sesión.";

        private UsuarioController() {
        }
    }

    public static final class ServicioMySqlDAO {
        public static final String USUARIO_ERROR_PROBLEMA_CONSULTANDO_SERVICIO =
                "No pudimos consultar las especialidades. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_SERVICIO =
                "Ocurrió un problema inesperado al consultar las especialidades. Avisa al administrador.";

        private ServicioMySqlDAO() {
        }
    }

    public static final class BarrioMySqlDAO {
        public static final String USUARIO_ERROR_PROBLEMA_CONSULTANDO_BARRIO =
                "No pudimos consultar los barrios. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_BARRIO =
                "Ocurrió un problema inesperado al consultar los barrios. Avisa al administrador.";

        private BarrioMySqlDAO() {
        }
    }

    public static final class TecnicoMySqlDAO {
        public static final String USUARIO_ERROR_PROBLEMA_CONSULTANDO_TECNICO =
                "No pudimos consultar los técnicos. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_TECNICO =
                "Ocurrió un problema inesperado al consultar los técnicos. Avisa al administrador.";

        private TecnicoMySqlDAO() {
        }
    }

    public static final class ServicioFachadaImpl {
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_SERVICIOS =
                "No pudimos cargar las especialidades. Intenta de nuevo en unos minutos.";

        private ServicioFachadaImpl() {
        }
    }

    public static final class BarrioFachadaImpl {
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_BARRIOS =
                "No pudimos cargar los barrios. Intenta de nuevo en unos minutos.";

        private BarrioFachadaImpl() {
        }
    }

    public static final class TecnicoFachadaImpl {
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_TECNICOS =
                "No pudimos buscar técnicos. Intenta de nuevo en unos minutos.";

        private TecnicoFachadaImpl() {
        }
    }

    public static final class TecnicoController {
        public static final String USUARIO_ERROR_NO_HAY_SESION = "Inicia sesión para buscar técnicos.";
        public static final String USUARIO_ERROR_SOLO_CIUDADANOS = "Solo los ciudadanos pueden buscar técnicos.";
        public static final String USUARIO_TECNICOS_NO_ENCONTRADOS = "No se encontraron técnicos.";

        private TecnicoController() {
        }
    }

    public static final class SesionController {
        public static final String USUARIO_SESION_INICIADA = "¡Bienvenido de nuevo!";
        public static final String USUARIO_SESION_CERRADA = "Cerraste sesión.";
        public static final String USUARIO_ERROR_NO_HAY_SESION = "Inicia sesión para continuar.";

        private SesionController() {
        }
    }
}
