package com.manosalaobra.transversal.utilitarios;

import java.util.Locale;
import java.util.regex.Pattern;

public final class UtilTexto {

    public static final String VACIO = "";
    public static final String SOLO_LETRAS_ESPACIOS = "^[a-zA-Z ñÑáÁéÉíÍóÓúÚüÜ]*$";
    public static final String LETRAS_DIGITOS = "^[a-zA-Z0-9]*$";
    public static final String SOLO_LETRAS = "^[a-zA-ZñÑáÁéÉíÍóÓúÚüÜ]*$";
    public static final String SOLO_DIGITOS = "^[0-9]*$";
    public static final String CORREO = "^[a-zA-Z0-9._+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final UtilTexto INSTANCIA = new UtilTexto();

    private UtilTexto() {
    }

    public static UtilTexto getUtilTexto() {
        return INSTANCIA;
    }

    public boolean esNula(String cadena) {
        return UtilObjeto.esNulo(cadena);
    }

    public boolean esVacia(String cadena) {
        return quitarEspacioEnBlanco(cadena).isEmpty();
    }

    public String obtenerValorDefecto(String valor, String valorDefecto) {
        return UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(valor, valorDefecto);
    }

    public String obtenerValorDefecto(String valor) {
        return obtenerValorDefecto(valor, VACIO);
    }

    public String quitarEspacioEnBlanco(String valor) {
        return obtenerValorDefecto(valor).trim();
    }

    public String normalizarCorreo(String correo) {
        return quitarEspacioEnBlanco(correo).toLowerCase(Locale.ROOT);
    }

    public int obtenerLongitudCadena(String valor) {
        return obtenerLongitudCadena(valor, false);
    }

    public int obtenerLongitudCadena(String valor, boolean quitarEspaciosBlanco) {
        String texto = obtenerValorDefecto(valor);
        return quitarEspaciosBlanco ? texto.trim().length() : texto.length();
    }

    public boolean obtenerLongitudCadenaEsValida(String valor, int longitudInicial, int longitudFinal,
            boolean quitarEspaciosBlanco) {
        int longitud = obtenerLongitudCadena(valor, quitarEspaciosBlanco);
        return longitud >= longitudInicial && longitud <= longitudFinal;
    }

    public boolean formatoEsValido(String valor, String patron) {
        return Pattern.matches(patron, quitarEspacioEnBlanco(valor));
    }
}
