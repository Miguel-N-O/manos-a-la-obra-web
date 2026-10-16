package com.manosalaobra.negocio.negocio;

import com.manosalaobra.dominio.TecnicoDominio;
import java.util.List;
import java.util.UUID;

public interface TecnicoNegocio {

    List<TecnicoDominio> consultarPorFiltro(TecnicoDominio filtro);

    TecnicoDominio consultarPorId(UUID id);
}
