package com.example.saz.saz.Modelo;

public class ModeloResumen {

    private String estilo;
    private String marca;
    private String color;
    private String punto;
    private String cantidad;
    private String subtotal;
    private String total;
    private String imagen;
    private String id;
    private String barcode;
    private String ubicacion;
    private String llave;
    private String idOrden;
    private String acabado;
    private String corrida;

    public ModeloResumen() {
        this.estilo = estilo;
        this.marca = marca;
        this.color = color;
        this.punto = punto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.total = total;
        this.imagen=imagen;
        this.id=id;
        this.barcode=barcode;
        this.ubicacion=ubicacion;
        this.llave=llave;
        this.idOrden=idOrden;
        this.acabado=acabado;
        this.corrida=corrida;
    }

    public String getCorrida() {
        return corrida;
    }

    public void setCorrida(String corrida) {
        this.corrida = corrida;
    }

    public String getAcabado() {
        return acabado;
    }

    public void setAcabado(String acabado) {
        this.acabado = acabado;
    }

    public String getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(String idOrden) {
        this.idOrden = idOrden;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPunto() {
        return punto;
    }

    public void setPunto(String punto) {
        this.punto = punto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getImagen(){return imagen;}

    public void setImagen(String imagen){ this.imagen=imagen; }

    public String getId(){return id;}

    public void setId(String id){ this.id=id; }

    public String getBarcode(){return barcode;}

    public void setBarcode(String barcode){ this.barcode=barcode; }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }
}
