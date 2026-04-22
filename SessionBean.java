package com.videoteca.beans;

import com.videoteca.entities.Usuarios;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("sessionBean")
@SessionScoped
public class SessionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuarios usuarioLogueado;

    public SessionBean() {}

    public Usuarios getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void setUsuarioLogueado(Usuarios usuarioLogueado) {
        this.usuarioLogueado = usuarioLogueado;
    }

    /**
     * Verifica si hay un usuario logueado en la sesión
     */
    public boolean isLogueado() {
        return usuarioLogueado != null;
    }

    /**
     * Retorna el nombre del usuario logueado o vacío
     */
    public String getNombreUsuario() {
        return usuarioLogueado != null ? usuarioLogueado.getNombre() : "";
    }
}
