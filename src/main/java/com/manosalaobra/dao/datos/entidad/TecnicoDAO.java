package com.manosalaobra.dao.datos.entidad;

import com.manosalaobra.dao.datos.ActualizarDAO;
import com.manosalaobra.dao.datos.ConsultarDAO;
import com.manosalaobra.dao.datos.CrearDAO;
import com.manosalaobra.entidad.TecnicoEntidad;
import java.util.UUID;

public interface TecnicoDAO extends CrearDAO<TecnicoEntidad>, ConsultarDAO<TecnicoEntidad, UUID>,
        ActualizarDAO<TecnicoEntidad, UUID> {

    // HU-02: incluye a los técnicos que aún no tienen perfil (consultarPorFiltro solo trae los que sí)
    boolean existeDocumento(String documento);

    // HU-04: el técnico consulta su propio perfil, aunque todavía no lo haya completado
    TecnicoEntidad consultarPorUsuario(UUID usuarioId);
}
