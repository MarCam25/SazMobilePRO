package com.example.saz.saz.entidades;

import java.io.Serializable;

public class Comandero implements Serializable {

private String numero;
private String cliente;
private String total;
private String pares;
private String empleado;
private String idOrden;


public Comandero(){

}

    public Comandero(String numero, String cliente, String total, String pares, String empleado,String idOrden ) {
        this.numero = numero;
        this.cliente = cliente;
        this.total = total;
        this.pares = pares;
        this.empleado = empleado;
        this.idOrden=idOrden;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPares() {
        return pares;
    }

    public void setPares(String pares) {
        this.pares = pares;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    public String getIdOrden(){return idOrden;};

    public void setIdOrden(String idOrden){this.idOrden=idOrden;}
}
