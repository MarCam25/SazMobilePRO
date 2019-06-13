package com.example.saz.saz.Modelo;

import java.io.Serializable;

public class Lupita implements Serializable {

    String tienda;
    String punto;

    public Lupita() {
        this.tienda = tienda;
        this.punto = punto;
    }

    public String getTienda() {
        return tienda;
    }

    public void setTienda(String tienda) {
        this.tienda = tienda;
    }

    public String getPunto() {
        return punto;
    }

    public void setPunto(String punto) {
        this.punto = punto;
    }
}
