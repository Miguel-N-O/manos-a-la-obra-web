package com.manosalaobra.negocio.negocio;

import com.manosalaobra.dominio.ServicioDominio;
import java.util.List;

public interface ServicioNegocio {

    List<ServicioDominio> consultarServicios();
}
