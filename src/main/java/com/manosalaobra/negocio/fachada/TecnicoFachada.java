package com.manosalaobra.negocio.fachada;

import com.manosalaobra.dto.TecnicoDTO;
import java.util.List;

public interface TecnicoFachada {

    List<TecnicoDTO> consultarPorFiltro(TecnicoDTO filtro);
}
