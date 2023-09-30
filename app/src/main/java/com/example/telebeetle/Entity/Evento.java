package com.example.telebeetle.Entity;

public class Evento {

    private String Actividad;

    //por ejemplo: cuartos de final, fase de grupos, etc
    private String nombre;

    private String fecha;
    private String lugar;

    public Evento(){

    }
    public Evento(String actividad, String nombre, String fecha, String lugar) {
        Actividad = actividad;
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
    }

    public String getActividad() {
        return Actividad;
    }

    public void setActividad(String actividad) {
        Actividad = actividad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
}
