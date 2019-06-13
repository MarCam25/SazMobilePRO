package com.example.saz.saz.entidades;

public class LineaDeConexion {
private int id;
private String server;
private String nombreBD;
private String usuario;
private String password;

    public LineaDeConexion(int id, String server, String nombreBD, String usuario, String password) {
        this.id = id;
        this.server = server;
        this.nombreBD = nombreBD;
        this.usuario = usuario;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getNombreBD() {
        return nombreBD;
    }

    public void setNombreBD(String nombreBD) {
        this.nombreBD = nombreBD;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
