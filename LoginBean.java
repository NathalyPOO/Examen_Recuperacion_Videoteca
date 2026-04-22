package com.videoteca.beans;

import com.videoteca.entities.Usuarios;
import com.videoteca.facades.UsuariosFacade;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named("loginBean")
@RequestScoped
public class LoginBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String usuario;
    private String contrasena;

    @EJB
    private UsuariosFacade usuariosFacade;

    @Inject
    private SessionBean sessionBean;

    public LoginBean() {}

    /**
     * Acción del botón Iniciar Sesión.
     * Verifica credenciales en BD y redirige según resultado.
     */
    public String login() {
        Usuarios u = usuariosFacade.findByUsuarioYContrasena(usuario, contrasena);

        if (u != null) {
            sessionBean.setUsuarioLogueado(u);
            return "/admin/usuarios?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Credenciales incorrectas",
                    "Usuario o contraseña inválidos."));
            return null;
        }
    }

    /**
     * Cierra la sesión del usuario actual
     */
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login/login?faces-redirect=true";
    }

    // Getters y Setters
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
}
