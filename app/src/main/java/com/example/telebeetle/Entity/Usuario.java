package com.example.telebeetle.Entity;

import java.io.Serializable;
import java.util.List;

public class Usuario implements Serializable {

    private String codigo;
    private String correo;
    private String nombres;
    private String apellidos;

    private String contrasena; //hasheada

    private String uidUsuario; //del autentication de firebase

    private String profile; //foto (url)

    private String rol; //Del.General , Del. Actividad , Usuario

    private String condicion; //Alumno , Egresado

    private Boolean enable; //true=activo, false=baneado

    private Boolean recibidoKitTeleco; //true=recibido, false=no_recibido

    private List<String> donaciones; //[keys de donaciones]

    public String getUidUsuario() {
        return uidUsuario;
    }

    public void setUidUsuario(String uidUsuario) {
        this.uidUsuario = uidUsuario;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Usuario(String nombres, String apellidos, String codigo, String correo, String condicion) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.codigo = codigo;
        this.correo = correo;
        this.condicion = condicion;
    }

    public Usuario() {
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getRecibidoKitTeleco() {
        return recibidoKitTeleco;
    }

    public void setRecibidoKitTeleco(Boolean recibidoKitTeleco) {
        this.recibidoKitTeleco = recibidoKitTeleco;
    }

    public List<String> getDonaciones() {
        return donaciones;
    }

    public void setDonaciones(List<String> donaciones) {
        this.donaciones = donaciones;
    }
}
