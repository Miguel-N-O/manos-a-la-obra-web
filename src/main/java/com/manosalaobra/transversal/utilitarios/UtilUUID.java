package com.manosalaobra.transversal.utilitarios;

import java.util.UUID;

public final class UtilUUID {

    public static final String UUID_DEFECTO_TEXTO = "00000000-0000-0000-0000-000000000000";

    private UtilUUID() {
    }

    public static UUID generar() {
        return UUID.randomUUID();
    }

    public static UUID convertirAUUID(String uuidTexto) {
        return UUID.fromString(UtilTexto.getUtilTexto().quitarEspacioEnBlanco(uuidTexto));
    }

    public static UUID obtenerUUIDDefecto() {
        return UUID.fromString(UUID_DEFECTO_TEXTO);
    }

    public static UUID obtenerValorDefecto(UUID valor, UUID valorDefecto) {
        return UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(valor, valorDefecto);
    }

    public static UUID obtenerValorDefecto(UUID valor) {
        return obtenerValorDefecto(valor, obtenerUUIDDefecto());
    }

    public static String obtenerValorDefectoComoTexto(String valor) {
        return UtilTexto.getUtilTexto().esVacia(valor) ? UUID_DEFECTO_TEXTO : valor;
    }

    public static boolean esValorDefecto(UUID valor) {
        return obtenerUUIDDefecto().equals(obtenerValorDefecto(valor));
    }
}
