package com.example.saz.saz.Modelo;

import java.io.Serializable;

public class DatosLupita implements Serializable {

    static  String barcode;
    static String punto;




    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getPunto() {
        return punto;
    }

    public void setPunto(String punto) {
        this.punto = punto;
    }
}
