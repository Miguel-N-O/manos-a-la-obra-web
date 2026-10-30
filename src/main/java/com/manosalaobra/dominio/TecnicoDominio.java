package com.manosalaobra.dominio;

import com.manosalaobra.transversal.utilitarios.UtilObjeto;
import com.manosalaobra.transversal.utilitarios.UtilTexto;
import com.manosalaobra.transversal.utilitarios.UtilUUID;

import java.math.BigDecimal;
import java.util.UUID;

public final class TecnicoDominio {

    // Tipos de documento aceptados en el registro (HU-02)
    public static final String CEDULA_CIUDADANIA = "CC";
    public static final String CEDULA_EXTRANJERIA = "CE";

    private final UUID id;
    private final UsuarioDominio usuario;
    private final ServicioDominio servicio;
    private final BarrioDominio barrio;
    private final BigDecimal tarifa;
    private final int experiencia;
    private final String tipoDocumento;
    private final String documento;
    private final BigDecimal promedio;

    private TecnicoDominio(Builder builder) {
        this.id = builder.id;
        this.usuario = builder.usuario;
        this.servicio = builder.servicio;
        this.barrio = builder.barrio;
        this.tarifa = builder.tarifa;
        this.experiencia = builder.experiencia;
        this.tipoDocumento = builder.tipoDocumento;
        this.documento = builder.documento;
        this.promedio = builder.promedio;
    }

    public UUID getId() {
        return id;
    }

    public UsuarioDominio getUsuario() {
        return usuario;
    }

    public ServicioDominio getServicio() {
        return servicio;
    }

    public BarrioDominio getBarrio() {
        return barrio;
    }

    public BigDecimal getTarifa() {
        return tarifa;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public BigDecimal getPromedio() {
        return promedio;
    }

    public static class Builder {
        private UUID id;
        private UsuarioDominio usuario;
        private ServicioDominio servicio;
        private BarrioDominio barrio;
        private BigDecimal tarifa;
        private int experiencia;
        private String tipoDocumento;
        private String documento;
        private BigDecimal promedio;

        public Builder() {
            id = UtilUUID.obtenerUUIDDefecto();
            usuario = new UsuarioDominio.Builder().build();
            servicio = new ServicioDominio.Builder().build();
            barrio = new BarrioDominio.Builder().build();
            tarifa = BigDecimal.ZERO;
            experiencia = 0;
            tipoDocumento = UtilTexto.VACIO;
            documento = UtilTexto.VACIO;
            promedio = BigDecimal.ZERO;
        }

        public Builder id(UUID id) {
            this.id = UtilUUID.obtenerValorDefecto(id);
            return this;
        }

        public Builder usuario(UsuarioDominio usuario) {
            this.usuario = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(usuario, new UsuarioDominio.Builder().build());
            return this;
        }

        public Builder servicio(ServicioDominio servicio) {
            this.servicio = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(servicio, new ServicioDominio.Builder().build());
            return this;
        }

        public Builder barrio(BarrioDominio barrio) {
            this.barrio = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(barrio, new BarrioDominio.Builder().build());
            return this;
        }

        public Builder tarifa(BigDecimal tarifa) {
            this.tarifa = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(tarifa, BigDecimal.ZERO);
            return this;
        }

        public Builder experiencia(int experiencia) {
            this.experiencia = experiencia;
            return this;
        }

        public Builder tipoDocumento(String tipoDocumento) {
            this.tipoDocumento = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(tipoDocumento);
            return this;
        }

        public Builder documento(String documento) {
            this.documento = UtilTexto.getUtilTexto().quitarEspacioEnBlanco(documento);
            return this;
        }


        public Builder promedio(BigDecimal promedio) {
            this.promedio = UtilObjeto.obtenerValorDefectoSiValorOriginalEsNulo(promedio, BigDecimal.ZERO);
            return this;
        }

        public TecnicoDominio build() {
            return new TecnicoDominio(this);
        }
    }
}
