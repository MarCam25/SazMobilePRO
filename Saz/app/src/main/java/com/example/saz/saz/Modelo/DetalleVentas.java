package com.example.saz.saz.Modelo;

public class DetalleVentas {

    private String estilo;
    private String color;
    private String marca;
    private String acabado;
    private String punto;
    private String llave;


    public DetalleVentas() {
        this.estilo = estilo;
        this.color = color;
        this.marca = marca;
        this.acabado = acabado;
        this.punto = punto;
        this.llave=llave;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getAcabado() {
        return acabado;
    }

    public void setAcabado(String acabado) {
        this.acabado = acabado;
    }

    public String getPunto() {
        return punto;
    }

    public void setPunto(String punto) {
        this.punto = punto;
    }
}
