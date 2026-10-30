package com.manosalaobra.dao.datos.entidad;

import com.manosalaobra.dao.datos.ConsultarDAO;
import com.manosalaobra.dao.datos.CrearDAO;
import com.manosalaobra.entidad.TecnicoEntidad;
import java.util.UUID;

public interface TecnicoDAO extends CrearDAO<TecnicoEntidad>, ConsultarDAO<TecnicoEntidad, UUID> {

    // HU-02: incluye a los técnicos que aún no tienen perfil (consultarPorFiltro solo trae los que sí)
    boolean existeDocumento(String documento);
}
