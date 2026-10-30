package com.manosalaobra.negocio.negocio;

import com.manosalaobra.dominio.SolicitudDominio;
import java.util.List;
import java.util.UUID;

public interface SolicitudNegocio {

    SolicitudDominio crear(UUID ciudadanoId, SolicitudDominio solicitud);

    List<SolicitudDominio> consultarDelCiudadano(UUID ciudadanoId);

    SolicitudDominio consultarUnaDelCiudadano(UUID ciudadanoId, UUID solicitudId);
}
