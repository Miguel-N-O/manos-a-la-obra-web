package com.manosalaobra.negocio.fachada;

import com.manosalaobra.dto.CalificacionDTO;
import java.util.List;
import java.util.UUID;

public interface CalificacionFachada {

    CalificacionDTO calificar(UUID ciudadanoId, CalificacionDTO calificacion);

    List<CalificacionDTO> consultarDelTecnico(UUID tecnicoId);

    List<CalificacionDTO> consultarTodasParaModeracion();

    CalificacionDTO ocultarComentario(UUID calificacionId, String motivo);

    CalificacionDTO mostrarComentario(UUID calificacionId);
}
