package com.manosalaobra.transversal.utilitarios;

import java.time.LocalDateTime;

public final class UtilFecha {

    private static final LocalDateTime FECHA_HORA_DEFECTO = LocalDateTime.of(1000, 1, 1, 0, 0);

    private UtilFecha() {
    }

    public static LocalDateTime obtenerFechaHoraActual() {
        return LocalDateTime.now();
    }

    public static LocalDateTime obtenerFechaHoraDefecto() {
        return FECHA_HORA_DEFECTO;
    }

    public static LocalDateTime obtenerValorDefecto(LocalDateTime valor) {
        return UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(valor, FECHA_HORA_DEFECTO);
    }

    public static boolean esValorDefecto(LocalDateTime valor) {
        return FECHA_HORA_DEFECTO.equals(obtenerValorDefecto(valor));
    }

    public static boolean estaEnElFuturo(LocalDateTime valor) {
        return obtenerValorDefecto(valor).isAfter(obtenerFechaHoraActual());
    }
}
