package com.example.telebeetle.Entity;

public class Actividad {

    private String nombreActividad;

    private String categoria;

    private String imagen;

    private Usuario delegado;

    public String getNombreActividad() {
        return nombreActividad;
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

    public Usuario getDelegado() {
        return delegado;
    }

    public void setDelegado(Usuario delegado) {
        this.delegado = delegado;
    }
}
