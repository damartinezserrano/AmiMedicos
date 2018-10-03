package com.example.marti.amimedicos.estructura;

/**
 * Created by Marti on 18/09/18.
 */

public class Identificacion {

    String usuario;

    String contrasena;

    String token_movil;

    public Identificacion() {
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getToken_movil() {
        return token_movil;
    }

    public void setToken_movil(String token_movil) {
        this.token_movil = token_movil;
    }
}
