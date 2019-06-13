package com.example.saz.saz.Modelo;

public class ModeloUsuario {
    public static String nombre="";
    public static String correo="";

    public   String getNombre() {
        return nombre;
    }

    public static void setNombre(String nombre) {
        ModeloUsuario.nombre = nombre;
    }

    public  String getCorreo() {
        return correo;
    }

    public static void setCorreo(String correo) {
        ModeloUsuario.correo = correo;
    }
}
