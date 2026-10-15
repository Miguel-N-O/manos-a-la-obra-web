package com.manosalaobra.negocio.negocio.reglas;

public interface Regla<T> {

    @SuppressWarnings("unchecked")
    void ejecutar(T... datos);
}
