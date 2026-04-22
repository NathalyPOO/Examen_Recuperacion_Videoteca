package com.videoteca.beans;

import com.videoteca.entities.Videos;
import com.videoteca.facades.VideosFacade;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

@Named("videosBean")
@SessionScoped
public class VideosBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Videos> listaVideos;
    private Videos videoSeleccionado;
    private Videos nuevoVideo;

    // Para p:calendar - filtro por año
    private Date fechaFiltro;

    @EJB
    private VideosFacade videosFacade;

    @Inject
    private SessionBean sessionBean;

    @PostConstruct
    public void init() {
        verificarSesion();
        cargarVideos();
        nuevoVideo = new Videos();
        videoSeleccionado = new Videos();
    }

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

    public void cargarVideos() {
        listaVideos = videosFacade.findAll();
    }

    /**
     * Filtra videos por año usando p:calendar
     */
    public void filtrarPorAnio() {
        if (fechaFiltro != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaFiltro);
            int anio = cal.get(Calendar.YEAR);
            listaVideos = videosFacade.findByAnio(anio);
        } else {
            cargarVideos();
        }
    }

    /**
     * Limpia el filtro y muestra todos los videos
     */
    public void limpiarFiltro() {
        fechaFiltro = null;
        cargarVideos();
    }

    // ======================== CRUD ========================

    /**
     * Crear nuevo video
     */
    public void guardarVideo() {
        try {
            videosFacade.create(nuevoVideo);
            cargarVideos();
            nuevoVideo = new Videos();
            agregarMensaje(FacesMessage.SEVERITY_INFO,
                "Éxito", "Video registrado correctamente.");
        } catch (Exception e) {
            agregarMensaje(FacesMessage.SEVERITY_ERROR,
                "Error", "No se pudo registrar el video: " + e.getMessage());
        }
    }

    /**
     * Prepara el video para visualizar en modal (p:video)
     */
    public void prepararVisualizacion(Videos v) {
        this.videoSeleccionado = videosFacade.find(v.getId());
    }

    /**
     * Prepara el video para edición
     */
    public void prepararEdicion(Videos v) {
        this.videoSeleccionado = videosFacade.find(v.getId());
    }

    /**
     * Actualizar video
     */
    public void actualizarVideo() {
        try {
            videosFacade.edit(videoSeleccionado);
            cargarVideos();
            agregarMensaje(FacesMessage.SEVERITY_INFO,
                "Éxito", "Video actualizado correctamente.");
        } catch (Exception e) {
            agregarMensaje(FacesMessage.SEVERITY_ERROR,
                "Error", "No se pudo actualizar el video: " + e.getMessage());
        }
    }

    /**
     * Eliminar video
     */
    public void eliminarVideo(Videos v) {
        try {
            videosFacade.remove(videosFacade.find(v.getId()));
            cargarVideos();
            agregarMensaje(FacesMessage.SEVERITY_INFO,
                "Éxito", "Video eliminado correctamente.");
        } catch (Exception e) {
            agregarMensaje(FacesMessage.SEVERITY_ERROR,
                "Error", "No se pudo eliminar el video: " + e.getMessage());
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
    public List<Videos> getListaVideos() { return listaVideos; }
    public void setListaVideos(List<Videos> listaVideos) { this.listaVideos = listaVideos; }

    public Videos getVideoSeleccionado() { return videoSeleccionado; }
    public void setVideoSeleccionado(Videos videoSeleccionado) { this.videoSeleccionado = videoSeleccionado; }

    public Videos getNuevoVideo() { return nuevoVideo; }
    public void setNuevoVideo(Videos nuevoVideo) { this.nuevoVideo = nuevoVideo; }

    public Date getFechaFiltro() { return fechaFiltro; }
    public void setFechaFiltro(Date fechaFiltro) { this.fechaFiltro = fechaFiltro; }
}
