package com.example.saz.saz.Modelo;

public class ModeloNumeroOrden {

    public static String numeroOrden;
    public static String estilo;
    public static String usuario;
    public static String tienda;
    public static String idArea;
    public static String idZona;

    public  String getIdArea() {
        return idArea;
    }

    public void setIdArea(String idArea) {
        ModeloNumeroOrden.idArea = idArea;
    }

    public String getIdZona() {
        return idZona;
    }

    public  void setIdZona(String idZona) {
        ModeloNumeroOrden.idZona = idZona;
    }

    public  String getUsuario() {
        return usuario;
    }

    public  void setUsuario(String usuario) {
        ModeloNumeroOrden.usuario = usuario;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        ModeloNumeroOrden.numeroOrden = numeroOrden;
    }

    public String getEstilo(){return estilo;}
    public void setEstilo(String estilo){
        ModeloNumeroOrden.estilo=estilo;
    }

    public  String getTienda() {
        return tienda;
    }

    public  void setTienda(String tienda) {
        ModeloNumeroOrden.tienda = tienda;
    }
}
