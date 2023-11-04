package com.example.telebeetle.Entity;

import java.io.Serializable;

public class Evento implements Serializable {

    private String Actividad;

    //por ejemplo: cuartos de final, fase de grupos, etc
    private String etapa;

    private String hora; //se agregó el atributo hora para el evento

    private String fecha;
    private String lugar;

    private String descripcion;

    private String nroMaxBarra;

    private String nroMaxParticipante;

    private String delegadoActividadAsignado;

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    private String latitud;
    private String longitud;



    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNroMaxBarra() {
        return nroMaxBarra;
    }

    public void setNroMaxBarra(String nroMaxBarra) {
        this.nroMaxBarra = nroMaxBarra;
    }

    public String getNroMaxParticipante() {
        return nroMaxParticipante;
    }

    public void setNroMaxParticipante(String nroMaxParticipante) {
        this.nroMaxParticipante = nroMaxParticipante;
    }

    public String getDelegadoActividadAsignado() {
        return delegadoActividadAsignado;
    }

    public void setDelegadoActividadAsignado(String delegadoActividadAsignado) {
        this.delegadoActividadAsignado = delegadoActividadAsignado;
    }



    public Evento(){

    }
    public Evento(String actividad, String etapa, String fecha, String lugar) {
        Actividad = actividad;
        this.etapa = etapa;
        this.fecha = fecha;
        this.lugar = lugar;
    }

    public String getActividad() {
        return Actividad;
    }

    public void setActividad(String actividad) {
        Actividad = actividad;
    }

    public String getEtapa() {
        return etapa;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
