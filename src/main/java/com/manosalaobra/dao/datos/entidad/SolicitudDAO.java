package com.manosalaobra.dao.datos.entidad;

import com.manosalaobra.dao.datos.ActualizarDAO;
import com.manosalaobra.dao.datos.ConsultarDAO;
import com.manosalaobra.dao.datos.CrearDAO;
import com.manosalaobra.entidad.SolicitudEntidad;
import java.util.UUID;

public interface SolicitudDAO extends CrearDAO<SolicitudEntidad>, ConsultarDAO<SolicitudEntidad, UUID>,
        ActualizarDAO<SolicitudEntidad, UUID> {

    void marcarComoNotificadas(UUID tecnicoId);
}
