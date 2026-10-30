package com.manosalaobra.negocio.fachada;

import com.manosalaobra.dto.SolicitudDTO;
import java.util.List;
import java.util.UUID;

public interface SolicitudFachada {

    SolicitudDTO crear(UUID ciudadanoId, SolicitudDTO solicitud);

    List<SolicitudDTO> consultarDelCiudadano(UUID ciudadanoId);

    SolicitudDTO consultarUnaDelCiudadano(UUID ciudadanoId, UUID solicitudId);

    List<SolicitudDTO> consultarRecibidas(UUID usuarioTecnicoId);

    SolicitudDTO consultarUnaRecibida(UUID usuarioTecnicoId, UUID solicitudId);

    void marcarRecibidasComoNotificadas(UUID usuarioTecnicoId);
}
