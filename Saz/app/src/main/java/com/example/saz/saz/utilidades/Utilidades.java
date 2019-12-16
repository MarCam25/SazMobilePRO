package com.example.saz.saz.utilidades;

public class Utilidades {
    public static final String TABLA_TIENDA="tienda";
    public static final String CAMPO_ID="id";
    public static final String CAMPOS_NOMBRE="nombreT";
    public static final String CAMPOS_DATOS="datos";
    public static final String CAMPOS_STATTUS="status";
    public static final String CREAR_TABLA_TIENDA="CREATE TABLE "+TABLA_TIENDA+"("+CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPOS_NOMBRE+" TEXT, "+CAMPOS_DATOS+" TEXT,"+CAMPOS_STATTUS+" TEXT)";



    public static final String TABLA_CONTENEDOR="contenedor";
    public static final String CAMPO_ID_CONTENEDOR="id";
    public static final String CAMPOS_ESTILO="estilo";
    public static final String CAMPOS_IMAGEN="imagen";
    public static final String CAMPOS_TALLA="talla";
    public static final String CAMPOS_CANTIDAD="cantidad";
    public static final String CAMPOS_MARCA="marca";
    public static final String CAMPOS_TOTAL="total";
    public static final String CAMPOS_SUB="sub";
    public static final String CAMPOS_COLOR="color";
    public static final String CAMPOS_BARCODE="barcode";
    public static final String CAMPOS_ACABADOC="acabado";
    public static final String CAMPOS_CORRIDA="corrida";
    public static final String CAMPO_CLIENTE="cliente";
    public static final String CAMPOS_UBICAX="ubicacion";
    public static final String CREAR_TABLA_CONTENEDOR="CREATE TABLE "+ TABLA_CONTENEDOR+"("+CAMPO_ID_CONTENEDOR+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPOS_ESTILO+" TEXT, "+ CAMPOS_IMAGEN+" TEXT, "+CAMPOS_TALLA+" TEXT, "+CAMPOS_CANTIDAD+" TEXT, "+CAMPOS_MARCA+" TEXT, "+CAMPOS_COLOR+" TEXT, "+CAMPOS_SUB+" TEXT, "+CAMPOS_TOTAL+" TEXT, "+CAMPOS_BARCODE+" TEXT, "+CAMPOS_ACABADOC +" TEXT, "+ CAMPOS_CORRIDA + " TEXT, "+CAMPO_CLIENTE+" TEXT, "+CAMPOS_UBICAX+" TEXT ) ";


    public static final String TABLA_PEDIDO="pedido";
    public static final String CAMPO_ID_PEDIDO="id";
    public static final String CAMPOS_ESTILOP="estilo";
    public static final String CAMPOS_IMAGENP="imagen";
    public static final String CAMPOS_TALLAP="talla";
    public static final String CAMPOS_CANTIDADP="cantidad";
    public static final String CAMPOS_MARCAP="marca";
    public static final String CAMPOS_TOTALP="total";
    public static final String CAMPOS_SUBP="sub";
    public static final String CAMPOS_COLORP="color";
    public static final String CAMPOS_BARCODEP="barcode";
    public static final String CAMPOS_ACABADO="acabado";
    public static final String CAMPOS_CORRIDAP="corrida";
    public static final String CAMPOS_UBICA="ubicacion";
    public static final String CAMPOS_ID_ORDEN="idOrden";
    public static final String CREAR_TABLA_PEDIDO="CREATE TABLE "+ TABLA_PEDIDO+"("+CAMPO_ID_PEDIDO+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPOS_ESTILOP+" TEXT, "+ CAMPOS_IMAGENP+" TEXT, "+CAMPOS_TALLAP+" TEXT, "+CAMPOS_CANTIDADP+" TEXT, " +CAMPOS_MARCAP+" TEXT, "+CAMPOS_COLORP+" TEXT, "+CAMPOS_SUBP+" TEXT, "+CAMPOS_TOTALP+" TEXT, "+CAMPOS_BARCODEP+" TEXT, "+ CAMPOS_ACABADO + " TEXT, " +CAMPOS_CORRIDAP+ " TEXT, "+CAMPOS_UBICA+" TEXT, "+ CAMPOS_ID_ORDEN + " INTEGER )";

    public static final String TABLA_ORDEN="orden";
    public static final String CAMPO_ID_ORDEN="idOrden";
    public static final String CAMPOS_STATUSO="status";
    public static final String CAMPOS_CLIENTE="cliente";
    public static final String CAMPOS_EMPLEADO="empleado";
    public static final String CAMPOS_NORDEN="norden";
    public static final String CAMPO_TOTAL="total";
    public static final String CAMPO_PARES="pares";
    public static final String CAMPOS_IMPRESO="impreso";
    public static final String CREAR_TABLA_ORDEN="CREATE TABLE "+ TABLA_ORDEN+"("+CAMPO_ID_ORDEN+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPOS_STATUSO+" TEXT, "+CAMPOS_CLIENTE+" TEXT, "+CAMPOS_EMPLEADO+" TEXT, "+CAMPOS_NORDEN+" TEXT, "+CAMPO_TOTAL+" TEXT, "+CAMPO_PARES+" TEXT, "+CAMPOS_IMPRESO+" TEXT )";

    public static final String TABLA_LOGIN="login";
    public static final String CAMPO_IDLOG="id";
    public static final String CAMPO_USUARIO="usuario";
    public static final String CAMPO_PASSWORD="contraseña";
    public static final String CREAR_TABLA_LOGIN="CREATE TABLE "+ TABLA_LOGIN+ " ( "+CAMPO_IDLOG+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_USUARIO+" TEXT, "+CAMPO_PASSWORD+" TEXT )";

    public static final String TABLA_NOTIFY="notify";
    public static final String CAMPO_IDNOTIFY="id";
    public static final String CAMPO_NUMERO_REGISTROS="numeroRegistros";
    public static final String CREAR_TABLA_NOTIFY="CREATE TABLE "+TABLA_NOTIFY+"( "+CAMPO_IDNOTIFY+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_NUMERO_REGISTROS+ " TEXT )";


    public static final String TABLA_AREA="area";
    public static final String CAMPO_IDA="id";
    public static final String CAMPO_AREA="area";
    public static final String CAMPO_TIPO="tipo";
    public static final String CREAR_TABLA_AREA="CREATE TABLE "+TABLA_AREA+"( "+CAMPO_IDA+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_AREA+ " TEXT, "+CAMPO_TIPO+" TEXT )";

    public static final String TABLA_SIMILAR="similar";
    public static final String CAMPO_IDS="id";
    public static final String CAMPO_MARCA="marca";
    public static final String CAMPO_TEMPORADA="temporada";
    public static final String CAMPO_CLASIFICACION="clasificacion";
    public static final String CAMPO_SUBLINEA="sublinea";
    public static final String CAMPO_SUELA="suela";
    public static final String CAMPO_TACON="tacon";
    public static final String CAMPO_COLOR="color";
    public static final String CAMPO_ACABADO="acabado";
    public static final String CAMPO_CORRIDA="corrida";
    public static final String CREAR_TABLA_SIMILAR="CREATE TABLE "+TABLA_SIMILAR+"("+CAMPO_IDS+" INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_MARCA+" TEXT, "
            +CAMPO_TEMPORADA+" TEXT, "+CAMPO_CLASIFICACION+" TEXT, "+CAMPO_SUBLINEA+" TEXT, "+CAMPO_SUELA+" TEXT, "+CAMPO_TACON+" TEXT, "+CAMPO_COLOR+" TEXT, "
            +CAMPO_ACABADO+" TEXT,"+CAMPO_CORRIDA+" TEXT )";

    public static final String TABLA_CHECKB="checkBuscador";
    public static final String CAMPO_IDCHECKB="id";
    public static final String CAMPO_BUSCADOR="buscador";
    public static final String CAMPO_BUSCADOR2="buscador2";
    public static final String CREAR_TABLA_CHECKB="CREATE TABLE "+TABLA_CHECKB+"( "+CAMPO_IDCHECKB+" INTEGER PRIMARY KEY AUTOINCREMENT,"+CAMPO_BUSCADOR+" TEXT,"+CAMPO_BUSCADOR2+" TEXT  )";


}
