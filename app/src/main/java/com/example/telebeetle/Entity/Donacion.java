package com.example.telebeetle.Entity;

import java.io.Serializable;

public class Donacion implements Serializable {

    private String keyDonacion;

    private String fecha;

    private String monto;

    private Boolean accepted;

    private String imagenCaptura;

    private String uidDonante;


    public String getUidDonante() {
        return uidDonante;
    }

    public void setUidDonante(String uidDonante) {
        this.uidDonante = uidDonante;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public Donacion(){

    }

    public Donacion(String keyDonacion, String fecha) {
        this.keyDonacion = keyDonacion;
        this.fecha = fecha;
    }

    public String getKeyDonacion() {
        return keyDonacion;
    }

    public void setKeyDonacion(String keyDonacion) {
        this.keyDonacion = keyDonacion;
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
