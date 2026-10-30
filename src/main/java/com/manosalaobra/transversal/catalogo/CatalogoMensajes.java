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

        public static final String USUARIO_ERROR_CAMPO_TIPO_DOCUMENTO = "el tipo de documento";
        public static final String USUARIO_ERROR_CAMPO_DOCUMENTO = "el número de documento";
        public static final String USUARIO_ERROR_TIPO_DOCUMENTO_NO_VALIDO =
                "El tipo de documento debe ser cédula de ciudadanía o cédula de extranjería.";
        public static final String USUARIO_ERROR_DOCUMENTO_FORMATO_NO_VALIDO =
                "El número de documento solo puede tener letras y números, sin espacios ni puntos.";
        public static final String USUARIO_ERROR_DOCUMENTO_LONGITUD_NO_VALIDA =
                "El número de documento es muy largo (máximo 30 caracteres).";
        public static final String USUARIO_ERROR_DOCUMENTO_YA_REGISTRADO =
                "Ya existe una cuenta registrada con este correo o documento.";

        public static final String USUARIO_ERROR_DATOS_INICIO_SESION_FALTANTES =
                "Escribe tu correo y tu contraseña.";
        public static final String USUARIO_ERROR_CREDENCIALES_INCORRECTAS = "Correo o contraseña incorrectos.";

        private UsuarioNegocioImpl() {
        }
    }

    public static final class UsuarioFachadaImpl {
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_REGISTRANDO_CIUDADANO =
                "No pudimos crear tu cuenta. Intenta de nuevo en unos minutos.";

        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_REGISTRANDO_TECNICO =
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
        public static final String USUARIO_TECNICO_REGISTRADO =
                "Tu cuenta de técnico se creó correctamente. Completa tu perfil para aparecer en las búsquedas.";

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
        public static final String USUARIO_ERROR_DOCUMENTO_YA_REGISTRADO =
                "Ya existe una cuenta registrada con este correo o documento.";
        public static final String USUARIO_ERROR_PROBLEMA_CREANDO_TECNICO =
                "No pudimos guardar tu cuenta de técnico. Revisa tus datos e intenta de nuevo.";
        public static final String USUARIO_ERROR_PROBLEMA_ACTUALIZANDO_PERFIL =
                "No fue posible guardar tu perfil. Por favor intenta nuevamente.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_ACTUALIZANDO_PERFIL =
                "Ocurrió un problema inesperado al guardar tu perfil. Intenta de nuevo o avisa al administrador.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CREANDO_TECNICO =
                "Ocurrió un problema inesperado al guardar tu cuenta de técnico. Intenta de nuevo o avisa al administrador.";

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
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_PERFIL =
                "No pudimos cargar el perfil del técnico. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_PERFIL_PROPIO =
                "No pudimos cargar tu perfil. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_GUARDANDO_PERFIL =
                "No fue posible guardar tu perfil. Por favor intenta nuevamente.";

        private TecnicoFachadaImpl() {
        }
    }

    public static final class TecnicoNegocioImpl {
        public static final String USUARIO_ERROR_TECNICO_NO_ENCONTRADO =
                "El técnico que buscas no existe o no está disponible.";
        public static final String USUARIO_ERROR_REGISTRO_TECNICO_NO_ENCONTRADO =
                "No encontramos tu registro de técnico. Cierra sesión, vuelve a entrar o avisa al administrador.";
        public static final String USUARIO_ERROR_CAMPOS_FALTANTES_PERFIL = "Para guardar tu perfil debes completar: %s.";
        public static final String USUARIO_ERROR_CAMPO_ESPECIALIDAD = "la especialidad";
        public static final String USUARIO_ERROR_CAMPO_BARRIO = "el barrio o zona de trabajo";
        public static final String USUARIO_ERROR_CAMPO_TIPO_TARIFA = "el tipo de tarifa";
        public static final String USUARIO_ERROR_TARIFA_O_EXPERIENCIA_NO_VALIDAS =
                "La tarifa debe ser mayor a cero y la experiencia un número entre 0 y 60.";
        public static final String USUARIO_ERROR_TARIFA_MAXIMA_NO_VALIDA =
                "La tarifa no puede superar $99.999.999.";
        public static final String USUARIO_ERROR_TIPO_TARIFA_NO_VALIDO =
                "El tipo de tarifa debe ser por hora o por trabajo.";
        public static final String USUARIO_ERROR_DESCRIPCION_LONGITUD_NO_VALIDA =
                "La descripción no puede superar 500 caracteres.";
        public static final String USUARIO_ERROR_ESPECIALIDAD_NO_VALIDA =
                "La especialidad elegida no está disponible. Elige una de la lista.";
        public static final String USUARIO_ERROR_BARRIO_NO_VALIDO =
                "El barrio elegido no está disponible. Elige uno de la lista.";

        private TecnicoNegocioImpl() {
        }
    }

    public static final class TecnicoController {
        public static final String USUARIO_ERROR_NO_HAY_SESION = "Inicia sesión para buscar técnicos.";
        public static final String USUARIO_ERROR_SOLO_CIUDADANOS = "Solo los ciudadanos pueden buscar técnicos.";
        public static final String USUARIO_ERROR_NO_HAY_SESION_PERFIL = "Inicia sesión para ver el perfil del técnico.";
        public static final String USUARIO_ERROR_SOLO_CIUDADANOS_PERFIL =
                "Solo los ciudadanos pueden ver el perfil de los técnicos.";
        public static final String USUARIO_TECNICOS_NO_ENCONTRADOS = "No se encontraron técnicos.";
        public static final String USUARIO_ERROR_NO_HAY_SESION_PERFIL_PROPIO = "Inicia sesión para ver tu perfil técnico.";
        public static final String USUARIO_ERROR_SOLO_TECNICOS_PERFIL_PROPIO =
                "Solo los técnicos tienen perfil técnico.";
        public static final String USUARIO_PERFIL_GUARDADO =
                "Tu perfil se guardó correctamente. Ya apareces en las búsquedas de los ciudadanos.";
        public static final String USUARIO_PERFIL_ACTUALIZADO = "Tu perfil se actualizó correctamente.";

        private TecnicoController() {
        }
    }

    public static final class EstadoSolicitudMySqlDAO {
        public static final String USUARIO_ERROR_PROBLEMA_CONSULTANDO_ESTADO_SOLICITUD =
                "No pudimos consultar los estados de las solicitudes. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_ESTADO_SOLICITUD =
                "Ocurrió un problema inesperado al consultar los estados de las solicitudes. Avisa al administrador.";

        private EstadoSolicitudMySqlDAO() {
        }
    }

    public static final class SolicitudMySqlDAO {
        public static final String USUARIO_ERROR_PROBLEMA_CREANDO_SOLICITUD =
                "No fue posible enviar tu solicitud. Por favor intenta nuevamente.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CREANDO_SOLICITUD =
                "Ocurrió un problema inesperado al enviar tu solicitud. Intenta de nuevo o avisa al administrador.";
        public static final String USUARIO_ERROR_PROBLEMA_CONSULTANDO_SOLICITUD =
                "No pudimos consultar las solicitudes. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_CONSULTANDO_SOLICITUD =
                "Ocurrió un problema inesperado al consultar las solicitudes. Avisa al administrador.";
        public static final String USUARIO_ERROR_PROBLEMA_MARCANDO_NOTIFICADAS =
                "No pudimos actualizar el aviso de solicitudes nuevas. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_MARCANDO_NOTIFICADAS =
                "Ocurrió un problema inesperado al actualizar el aviso de solicitudes nuevas. Avisa al administrador.";
        public static final String USUARIO_ERROR_PROBLEMA_ACTUALIZANDO_SOLICITUD =
                "No fue posible guardar la respuesta a la solicitud. Por favor intenta nuevamente.";
        public static final String USUARIO_ERROR_PROBLEMA_NO_CONTROLADO_ACTUALIZANDO_SOLICITUD =
                "Ocurrió un problema inesperado al guardar la respuesta a la solicitud. Avisa al administrador.";

        private SolicitudMySqlDAO() {
        }
    }

    public static final class SolicitudNegocioImpl {
        public static final String USUARIO_ERROR_CAMPOS_FALTANTES = "Para enviar la solicitud debes completar: %s.";
        public static final String USUARIO_ERROR_CAMPO_TECNICO = "el técnico";
        public static final String USUARIO_ERROR_CAMPO_DESCRIPCION = "la descripción del trabajo";
        public static final String USUARIO_ERROR_CAMPO_BARRIO = "el barrio donde se realizará el trabajo";
        public static final String USUARIO_ERROR_CAMPO_FECHA_HORA = "la fecha y hora propuestas";
        public static final String USUARIO_ERROR_DESCRIPCION_LONGITUD_NO_VALIDA =
                "La descripción del trabajo debe tener entre 10 y 500 caracteres.";
        public static final String USUARIO_ERROR_FECHA_HORA_NO_FUTURA =
                "La fecha y hora propuestas deben ser posteriores al momento actual.";
        public static final String USUARIO_ERROR_TECNICO_NO_DISPONIBLE =
                "El técnico que elegiste no está disponible. Busca otro técnico.";
        public static final String USUARIO_ERROR_SOLICITUD_NO_ENCONTRADA = "No encontramos esa solicitud.";
        public static final String USUARIO_ERROR_ESTADO_NO_CONFIGURADO =
                "No pudimos procesar la solicitud porque falta configurar los estados de solicitud. Avisa al administrador.";
        public static final String USUARIO_ERROR_SOLICITUD_YA_RESPONDIDA =
                "Esta solicitud ya fue respondida y no puede modificarse.";
        public static final String USUARIO_ERROR_MOTIVO_RECHAZO_NO_VALIDO =
                "El motivo del rechazo no es válido. Elige uno de la lista o deja el campo vacío.";
        public static final String USUARIO_ERROR_REGISTRO_TECNICO_NO_ENCONTRADO =
                "No encontramos tu registro de técnico. Cierra sesión, vuelve a entrar o avisa al administrador.";

        private SolicitudNegocioImpl() {
        }
    }

    public static final class SolicitudFachadaImpl {
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CREANDO_SOLICITUD =
                "No fue posible enviar tu solicitud. Por favor intenta nuevamente.";
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_CONSULTANDO_SOLICITUDES =
                "No pudimos cargar tus solicitudes. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_MARCANDO_NOTIFICADAS =
                "No pudimos actualizar el aviso de solicitudes nuevas. Intenta de nuevo en unos minutos.";
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_ACEPTANDO_SOLICITUD =
                "No fue posible aceptar la solicitud. Por favor intenta nuevamente.";
        public static final String USUARIO_ERROR_PROBLEMA_INESPERADO_RECHAZANDO_SOLICITUD =
                "No fue posible rechazar la solicitud. Por favor intenta nuevamente.";

        private SolicitudFachadaImpl() {
        }
    }

    public static final class SolicitudController {
        public static final String USUARIO_SOLICITUD_ENVIADA =
                "Tu solicitud fue enviada. Quedará pendiente hasta que el técnico responda.";
        public static final String USUARIO_SIN_SOLICITUDES =
                "Aún no has enviado solicitudes. Busca un técnico para empezar.";
        public static final String USUARIO_ERROR_NO_HAY_SESION = "Inicia sesión para solicitar servicios.";
        public static final String USUARIO_ERROR_SOLO_CIUDADANOS =
                "Solo los ciudadanos pueden solicitar servicios y ver sus solicitudes.";
        public static final String USUARIO_SIN_SOLICITUDES_RECIBIDAS = "Aún no has recibido solicitudes de servicio.";
        public static final String USUARIO_ERROR_NO_HAY_SESION_TECNICO =
                "Inicia sesión para ver las solicitudes que has recibido.";
        public static final String USUARIO_ERROR_SOLO_TECNICOS =
                "Solo los técnicos pueden ver y responder las solicitudes recibidas.";
        public static final String USUARIO_SOLICITUD_ACEPTADA =
                "Aceptaste la solicitud. El ciudadano ya puede ver tus datos de contacto para coordinar el trabajo.";
        public static final String USUARIO_SOLICITUD_RECHAZADA = "Rechazaste la solicitud. Le informaremos al ciudadano.";

        private SolicitudController() {
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
