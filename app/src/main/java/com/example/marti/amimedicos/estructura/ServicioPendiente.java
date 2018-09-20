package com.example.marti.amimedicos.estructura;

/**
 * Created by Marti on 18/09/18.
 */

public class ServicioPendiente {

    String consec_movserv;

    String primer_nombre;

    String segundo_nombre;

    String primer_apellido;

    String segundo_apellido;

    String telefono_servicio;

    String tipo_servicio_id_tiposerv;

    public ServicioPendiente() {
    }

    public String getConsec_movserv() {
        return consec_movserv;
    }

    public void setConsec_movserv(String consec_movserv) {
        this.consec_movserv = consec_movserv;
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

    public String getTipo_servicio_id_tiposerv() {
        return tipo_servicio_id_tiposerv;
    }

    public void setTipo_servicio_id_tiposerv(String tipo_servicio_id_tiposerv) {
        this.tipo_servicio_id_tiposerv = tipo_servicio_id_tiposerv;
    }
}
