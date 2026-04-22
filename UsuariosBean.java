package com.videoteca.beans;

import com.videoteca.entities.Usuarios;
import com.videoteca.facades.UsuariosFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named("usuariosBean")
@SessionScoped
public class UsuariosBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Usuarios> listaUsuarios;
    private Usuarios usuarioSeleccionado;
    private Usuarios nuevoUsuario;

    @EJB
    private UsuariosFacade usuariosFacade;

    @Inject
    private SessionBean sessionBean;

    @PostConstruct
    public void init() {
        // Verificar que haya sesión activa
        verificarSesion();
        cargarUsuarios();
        nuevoUsuario = new Usuarios();
        usuarioSeleccionado = new Usuarios();
    }

    /**
     * Redirige al login si no hay sesión activa
     */
    private void verificarSesion() {
        if (!sessionBean.isLogueado()) {
            try {
                FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .redirect(FacesContext.getCurrentInstance()
                        .getExternalContext().getRequestContextPath() + "/login/login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void cargarUsuarios() {
        listaUsuarios = usuariosFacade.findAll();
    }

    // ======================== CRUD ========================

    /**
     * Crear nuevo usuario
     */
    public void guardarUsuario() {
        try {
            if (usuariosFacade.existeUsuario(nuevoUsuario.getUsuario())) {
                agregarMensaje(FacesMessage.SEVERITY_WARN,
                    "Advertencia", "El nombre de usuario ya existe.");
                return;
            }
            usuariosFacade.create(nuevoUsuario);
            cargarUsuarios();
            nuevoUsuario = new Usuarios();
            agregarMensaje(FacesMessage.SEVERITY_INFO,
                "Éxito", "Usuario creado correctamente.");
        } catch (Exception e) {
            agregarMensaje(FacesMessage.SEVERITY_ERROR,
                "Error", "No se pudo crear el usuario: " + e.getMessage());
        }
    }

    /**
     * Prepara el usuario para edición
     */
    public void prepararEdicion(Usuarios u) {
        this.usuarioSeleccionado = usuariosFacade.find(u.getId());
    }

    /**
     * Actualizar usuario seleccionado
     */
    public void actualizarUsuario() {
        try {
            usuariosFacade.edit(usuarioSeleccionado);
            cargarUsuarios();
            agregarMensaje(FacesMessage.SEVERITY_INFO,
                "Éxito", "Usuario actualizado correctamente.");
        } catch (Exception e) {
            agregarMensaje(FacesMessage.SEVERITY_ERROR,
                "Error", "No se pudo actualizar el usuario: " + e.getMessage());
        }
    }

    /**
     * Eliminar usuario
     */
    public void eliminarUsuario(Usuarios u) {
        try {
            // No permitir eliminar al usuario logueado
            if (u.getId().equals(sessionBean.getUsuarioLogueado().getId())) {
                agregarMensaje(FacesMessage.SEVERITY_WARN,
                    "Advertencia", "No puedes eliminar tu propio usuario.");
                return;
            }
            usuariosFacade.remove(usuariosFacade.find(u.getId()));
            cargarUsuarios();
            agregarMensaje(FacesMessage.SEVERITY_INFO,
                "Éxito", "Usuario eliminado correctamente.");
        } catch (Exception e) {
            agregarMensaje(FacesMessage.SEVERITY_ERROR,
                "Error", "No se pudo eliminar el usuario: " + e.getMessage());
        }
    }

    /**
     * Cierra sesión
     */
    public String cerrarSesion() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login/login?faces-redirect=true";
    }

    private void agregarMensaje(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(severity, summary, detail));
    }

    // Getters y Setters
    public List<Usuarios> getListaUsuarios() { return listaUsuarios; }
    public void setListaUsuarios(List<Usuarios> listaUsuarios) { this.listaUsuarios = listaUsuarios; }

    public Usuarios getUsuarioSeleccionado() { return usuarioSeleccionado; }
    public void setUsuarioSeleccionado(Usuarios usuarioSeleccionado) { this.usuarioSeleccionado = usuarioSeleccionado; }

    public Usuarios getNuevoUsuario() { return nuevoUsuario; }
    public void setNuevoUsuario(Usuarios nuevoUsuario) { this.nuevoUsuario = nuevoUsuario; }
}
