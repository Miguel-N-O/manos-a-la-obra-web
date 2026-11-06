package com.manosalaobra.dao.datos.entidad;

import com.manosalaobra.dao.datos.ActualizarDAO;
import com.manosalaobra.dao.datos.ConsultarDAO;
import com.manosalaobra.dao.datos.CrearDAO;
import com.manosalaobra.entidad.UsuarioEntidad;

import java.util.UUID;

// HU-16: actualizar solo cambia el estado de la cuenta (activo, suspendido o bloqueado)
public interface UsuarioDAO extends CrearDAO<UsuarioEntidad>, ConsultarDAO<UsuarioEntidad, UUID>,
        ActualizarDAO<UsuarioEntidad, UUID> {
}
