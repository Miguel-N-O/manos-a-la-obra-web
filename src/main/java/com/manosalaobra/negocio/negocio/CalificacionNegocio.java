package com.manosalaobra.negocio.negocio;

import com.manosalaobra.dominio.CalificacionDominio;
import java.util.List;
import java.util.UUID;

public interface CalificacionNegocio {

    CalificacionDominio calificar(UUID ciudadanoId, CalificacionDominio calificacion);

    List<CalificacionDominio> consultarDelTecnico(UUID tecnicoId);

    List<CalificacionDominio> consultarTodasParaModeracion();

    CalificacionDominio ocultarComentario(UUID calificacionId, String motivo);

    CalificacionDominio mostrarComentario(UUID calificacionId);
}
