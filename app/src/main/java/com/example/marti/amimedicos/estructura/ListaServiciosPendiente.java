package com.example.marti.amimedicos.estructura;

/**
 * Created by Marti on 18/09/18.
 */

public class ListaServiciosPendiente {

    boolean estado;

    String mensaje;

    ServicioPendiente servicio;

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

    public ServicioPendiente getServicio() {
        return servicio;
    }

    public void setServicio(ServicioPendiente servicio) {
        this.servicio = servicio;
    }
}
