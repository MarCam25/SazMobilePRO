package com.example.saz.saz.Modelo;

import java.io.Serializable;

public class Zonas implements Serializable {
    private String idZona;
    private String idTienda;
    private String nombre;
    private String idArea;
    private String nombreArea;

    public Zonas() {
        this.idZona = idZona;
        this.idTienda = idTienda;
        this.nombre = nombre;
        this.idArea = idArea;
        this.nombreArea=nombreArea;
    }

    public String getNombreArea() {
        return nombreArea;
    }

    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }

    public String getIdZona() {
        return idZona;
    }

    public void setIdZona(String idZona) {
        this.idZona = idZona;
    }

    public String getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIdArea() {
        return idArea;
    }

    public void setIdArea(String idArea) {
        this.idArea = idArea;
    }
}
