package com.manosalaobra.negocio.negocio;

import com.manosalaobra.dominio.TecnicoDominio;
import java.util.List;

public interface TecnicoNegocio {

    List<TecnicoDominio> consultarPorFiltro(TecnicoDominio filtro);
}
