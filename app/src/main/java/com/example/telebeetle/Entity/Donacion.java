package com.example.telebeetle.Entity;

public class Donacion {

    private String asunto;

    private String fecha;

    private String monto;

    private Boolean accepted;

    private String imagenCaptura;



    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public Donacion(){

    }

    public Donacion(String asunto, String fecha) {
        this.asunto = asunto;
        this.fecha = fecha;
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

    public Boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public String getImagenCaptura() {
        return imagenCaptura;
    }

    public void setImagenCaptura(String imagenCaptura) {
        this.imagenCaptura = imagenCaptura;
    }
}
