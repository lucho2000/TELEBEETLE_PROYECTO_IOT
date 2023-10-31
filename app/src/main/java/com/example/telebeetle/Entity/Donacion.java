package com.example.telebeetle.Entity;

public class Donacion {

    private String asunto;

    private String fecha;

    private String hora;

    public String getDonante() {
        return donante;
    }

    public void setDonante(String donante) {
        this.donante = donante;
    }

    private String donante;

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    private String monto;

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
