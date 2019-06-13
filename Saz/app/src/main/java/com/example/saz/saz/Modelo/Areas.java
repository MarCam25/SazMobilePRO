package com.example.saz.saz.Modelo;

import java.io.Serializable;

public class Areas implements Serializable {

    private String idArea;
    private String idTienda;
    private String nombreArea;

    public Areas() {
        this.idArea = idArea;
        this.nombreArea = nombreArea;
        this.idTienda=idTienda;
    }

    public String getIdTienda() {
        return idTienda;
    }

    public void setIdTienda(String idTienda) {
        this.idTienda = idTienda;
    }

    public String getIdArea() {
        return idArea;
    }

    public void setIdArea(String idArea) {
        this.idArea = idArea;
    }

    public String getNombreArea() {
        return nombreArea;
    }

    public void setNombreArea(String nombreArea) {
        this.nombreArea = nombreArea;
    }
}
