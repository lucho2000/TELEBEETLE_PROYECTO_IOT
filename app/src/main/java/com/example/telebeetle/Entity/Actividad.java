package com.example.telebeetle.Entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Actividad implements Serializable {

    private String nombreActividad;

    private String categoria;

    private String imagen;

    private String delegado;

    private Boolean estado;

    public HashMap<String, String> getEventos() {
        return eventos;
    }

    public void setEventos(HashMap<String, String> eventos) {
        this.eventos = eventos;
    }

    private HashMap<String,String> eventos;


    private String uidActividad;


    public String getNombreActividad() {
        return nombreActividad;
    }

    public String getUidActividad() {
        return uidActividad;
    }

    public void setUidActividad(String uidActividad) {
        this.uidActividad = uidActividad;
    }


    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDelegado() {
        return delegado;
    }

    public void setDelegado(String delegado) {
        this.delegado = delegado;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

}
