package com.manosalaobra.negocio.negocio.reglas.impl.tecnico;

import com.manosalaobra.dominio.TecnicoDominio;
import com.manosalaobra.negocio.negocio.reglas.Regla;
import com.manosalaobra.transversal.catalogo.CatalogoMensajes;
import com.manosalaobra.transversal.excepciones.ManosALaObraNegocioExcepcion;
import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// HU-04 CA-03 y CA-04: datos obligatorios y valores válidos del perfil del técnico
public final class ValidarDatosPerfilTecnicoRegla implements Regla<TecnicoDominio> {

    private static final Regla<TecnicoDominio> INSTANCIA = new ValidarDatosPerfilTecnicoRegla();

    private static final int EXPERIENCIA_MAXIMA = 60;
    private static final int LONGITUD_MAXIMA_DESCRIPCION = 500;
    // La columna Tecnico.tarifa es DECIMAL(10,2)
    private static final BigDecimal TARIFA_MAXIMA = new BigDecimal("99999999.99");

    private ValidarDatosPerfilTecnicoRegla() {
    }

    public static Regla<TecnicoDominio> obtenerInstancia() {
        return INSTANCIA;
    }

    @Override
    @SafeVarargs
    public final void ejecutar(TecnicoDominio... datos) {
        TecnicoDominio perfil = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(
                datos.length > 0 ? datos[0] : null, new TecnicoDominio.Builder().build());
        asegurarQueNoFaltenCampos(perfil);
        asegurarTarifaYExperienciaValidas(perfil);
        asegurarTipoTarifaValido(perfil.getTipoTarifa());
        asegurarDescripcionValida(perfil.getDescripcion());
    }

    private void asegurarQueNoFaltenCampos(TecnicoDominio perfil) {
        List<String> faltantes = new ArrayList<>();
        if (UtilUUID.esValorDefecto(perfil.getServicio().getId())) {
            faltantes.add(CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_CAMPO_ESPECIALIDAD);
        }
        if (UtilUUID.esValorDefecto(perfil.getBarrio().getId())) {
            faltantes.add(CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_CAMPO_BARRIO);
        }
        if (UtilTexto.getUtilTexto().esVacia(perfil.getTipoTarifa())) {
            faltantes.add(CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_CAMPO_TIPO_TARIFA);
        }
        if (!faltantes.isEmpty()) {
            throw ManosALaObraNegocioExcepcion.crear(String.format(
                    CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_CAMPOS_FALTANTES_PERFIL,
                    String.join(", ", faltantes)));
        }
    }

    private void asegurarTarifaYExperienciaValidas(TecnicoDominio perfil) {
        if (perfil.getTarifa().compareTo(BigDecimal.ZERO) <= 0
                || perfil.getExperiencia() < 0 || perfil.getExperiencia() > EXPERIENCIA_MAXIMA) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_TARIFA_O_EXPERIENCIA_NO_VALIDAS);
        }
        if (perfil.getTarifa().compareTo(TARIFA_MAXIMA) > 0) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_TARIFA_MAXIMA_NO_VALIDA);
        }
    }

    private void asegurarTipoTarifaValido(String tipoTarifa) {
        if (!TecnicoDominio.TARIFA_POR_HORA.equals(tipoTarifa)
                && !TecnicoDominio.TARIFA_POR_TRABAJO.equals(tipoTarifa)) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_TIPO_TARIFA_NO_VALIDO);
        }
    }

    private void asegurarDescripcionValida(String descripcion) {
        if (UtilTexto.getUtilTexto().obtenerLongitudCadena(descripcion, true) > LONGITUD_MAXIMA_DESCRIPCION) {
            throw ManosALaObraNegocioExcepcion.crear(
                    CatalogoMensajes.TecnicoNegocioImpl.USUARIO_ERROR_DESCRIPCION_LONGITUD_NO_VALIDA);
        }
    }
}
