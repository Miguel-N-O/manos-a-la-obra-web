package com.manosalaobra.negocio.negocio.ensamblador;

public interface EnsambladorEntidad<D, E> {

    E convertirAEntidad(D dominio);

    D convertirADominio(E entidad);
}
