package com.manosalaobra.negocio.fachada.ensamblador;

import java.util.List;

public interface EnsambladorDTO<D, T> {

    T convertirADTO(D dominio);

    D convertirADominio(T dto);

    List<T> convertirADTO(List<D> dominios);
}
