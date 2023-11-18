package com.example.telebeetle.Entity;

import java.io.Serializable;
import java.util.List;

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

    private String latitud;
    private String longitud;

    private String estado; //(boolean)


    private List<String> rutasFotosEventos; //lista de rutas URL

    private String uidEvento;

    private List<String> listaApoyosBarras;  //(lista de KEY de usuarios)

    private List<String> listaApoyosParticipantes;  //(lista de KEY de usuarios)

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

    public String getUidEvento() {
        return uidEvento;
    }

    public void setUidEvento(String uidEvento) {
        this.uidEvento = uidEvento;
    }

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<String> getRutasFotosEventos() {
        return rutasFotosEventos;
    }

    public void setRutasFotosEventos(List<String> rutasFotosEventos) {
        this.rutasFotosEventos = rutasFotosEventos;
    }

    public List<String> getListaApoyosBarras() {
        return listaApoyosBarras;
    }

    public void setListaApoyosBarras(List<String> listaApoyosBarras) {
        this.listaApoyosBarras = listaApoyosBarras;
    }

    public List<String> getListaApoyosParticipantes() {
        return listaApoyosParticipantes;
    }

    public void setListaApoyosParticipantes(List<String> listaApoyosParticipantes) {
        this.listaApoyosParticipantes = listaApoyosParticipantes;
    }
}
