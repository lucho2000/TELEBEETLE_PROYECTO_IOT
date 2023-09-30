package com.example.telebeetle.Entity;

public class Donacion {

    private String asunto;

    private String fecha;

    private String hora;

    public Donacion(){

    }

    public Donacion(String asunto, String fecha, String hora) {
        this.asunto = asunto;
        this.fecha = fecha;
        this.hora = hora;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
