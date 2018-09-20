package com.example.marti.amimedicos.estructura;

/**
 * Created by Marti on 19/09/18.
 */

public class DetalleServicio {

    boolean estado;

    String mensaje;

    Detalle[] detalle;

    public DetalleServicio() {
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Detalle[] getDetalle() {
        return detalle;
    }

    public void setDetalle(Detalle[] detalle) {
        this.detalle = detalle;
    }
}
