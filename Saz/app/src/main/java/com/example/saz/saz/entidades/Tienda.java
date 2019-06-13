package com.example.saz.saz.entidades;

import java.io.Serializable;


public class Tienda implements Serializable {
    private int id;
    private String nombreT;
    private String datos;
    private String status;

    public Tienda(int id, String nombreT, String datos, String status) {
        this.id = id;
        this.nombreT = nombreT;
        this.datos = datos;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreT() {
        return nombreT;
    }

    public void setNombreT(String nombreT) {
        this.nombreT = nombreT;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
