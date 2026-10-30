package com.manosalaobra.negocio.fachada;

import com.manosalaobra.dto.TecnicoDTO;
import java.util.List;
import java.util.UUID;

public interface TecnicoFachada {

    List<TecnicoDTO> consultarPorFiltro(TecnicoDTO filtro);

    TecnicoDTO consultarPorId(UUID id);

    TecnicoDTO consultarPerfilPropio(UUID usuarioId);

    TecnicoDTO guardarPerfilPropio(UUID usuarioId, TecnicoDTO perfil);
}
