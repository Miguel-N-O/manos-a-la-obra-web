package com.manosalaobra.dao.datos.entidad;

import com.manosalaobra.dao.datos.ActualizarDAO;
import com.manosalaobra.dao.datos.ConsultarDAO;
import com.manosalaobra.dao.datos.CrearDAO;
import com.manosalaobra.entidad.CalificacionEntidad;
import java.util.UUID;

public interface CalificacionDAO extends CrearDAO<CalificacionEntidad>, ConsultarDAO<CalificacionEntidad, UUID>,
        ActualizarDAO<CalificacionEntidad, UUID> {

    // HU-15: recalcula el promedio del técnico con todas sus calificaciones y lo guarda en su perfil
    void actualizarPromedioDelTecnico(UUID tecnicoId);
}
