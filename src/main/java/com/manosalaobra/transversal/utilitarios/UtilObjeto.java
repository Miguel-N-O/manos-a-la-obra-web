package com.manosalaobra.transversal.utilitarios;

public final class UtilObjeto {

    private UtilObjeto() {
    }

    public static <O> boolean esNulo(O objeto) {
        return objeto == null;
    }

    public static <O> O obtenerValorDefectoSiValorOriginalEsNulo(O valor, O valorDefecto) {
        return esNulo(valor) ? valorDefecto : valor;
    }
}
