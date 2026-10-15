package com.manosalaobra.negocio.fachada;

import com.manosalaobra.dto.ServicioDTO;
import java.util.List;

public interface ServicioFachada {

    List<ServicioDTO> consultarServicios();
}
