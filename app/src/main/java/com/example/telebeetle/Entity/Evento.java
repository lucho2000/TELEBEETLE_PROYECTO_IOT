package com.example.telebeetle.Entity;

public class Evento {

    private String Actividad;

    //por ejemplo: cuartos de final, fase de grupos, etc
    private String etapa;

    private String hora; //se agregó el atributo hora para el evento

    private String fecha;
    private String lugar;

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
