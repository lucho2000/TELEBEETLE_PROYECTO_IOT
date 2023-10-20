package com.example.telebeetle.Entity;

public class Activity {
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    private String nombre;

    public String getDelegado() {
        return delegado;
    }

    public void setDelegado(String delegado) {
        this.delegado = delegado;
    }

    private String delegado;
     public Activity(){

     }
     public Activity(String nombre, String delegado){
         this.nombre = nombre;
         this.delegado = delegado;
     }

}
