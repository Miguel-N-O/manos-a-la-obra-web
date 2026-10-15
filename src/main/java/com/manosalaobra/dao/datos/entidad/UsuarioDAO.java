package com.manosalaobra.dao.datos.entidad;

import com.manosalaobra.dao.datos.ConsultarDAO;
import com.manosalaobra.dao.datos.CrearDAO;
import com.manosalaobra.entidad.UsuarioEntidad;

import java.util.UUID;

public interface UsuarioDAO extends CrearDAO<UsuarioEntidad>, ConsultarDAO<UsuarioEntidad, UUID> {
}
