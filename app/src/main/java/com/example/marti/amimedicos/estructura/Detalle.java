package com.example.marti.amimedicos.estructura;

/**
 * Created by Marti on 19/09/18.
 */

public class Detalle {

    String primer_nombre;

    String segundo_nombre;

    String primer_apellido;

    String segundo_apellido;

    String telefono_servicio;

    String diagnostico;

    String tipo_servicio_id_tiposerv;

    public Detalle() {
    }

    public String getPrimer_nombre() {
        return primer_nombre;
    }

    public void setPrimer_nombre(String primer_nombre) {
        this.primer_nombre = primer_nombre;
    }

    public String getSegundo_nombre() {
        return segundo_nombre;
    }

    public void setSegundo_nombre(String segundo_nombre) {
        this.segundo_nombre = segundo_nombre;
    }

    public String getPrimer_apellido() {
        return primer_apellido;
    }

    public void setPrimer_apellido(String primer_apellido) {
        this.primer_apellido = primer_apellido;
    }

    public String getSegundo_apellido() {
        return segundo_apellido;
    }

    public void setSegundo_apellido(String segundo_apellido) {
        this.segundo_apellido = segundo_apellido;
    }

    public String getTelefono_servicio() {
        return telefono_servicio;
    }

    public void setTelefono_servicio(String telefono_servicio) {
        this.telefono_servicio = telefono_servicio;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getTipo_servicio_id_tiposerv() {
        return tipo_servicio_id_tiposerv;
    }

    public void setTipo_servicio_id_tiposerv(String tipo_servicio_id_tiposerv) {
        this.tipo_servicio_id_tiposerv = tipo_servicio_id_tiposerv;
    }
}
