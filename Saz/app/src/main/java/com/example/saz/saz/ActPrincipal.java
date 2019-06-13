package com.example.saz.saz;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.saz.saz.Modelo.ModeloDatos;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloNumeroOrden;
import com.example.saz.saz.Modelo.ModeloUsuario;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.conexion.ConexionSqlServer;
import com.example.saz.saz.utilidades.ModeloTienda;
import com.example.saz.saz.utilidades.Utilidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ActPrincipal extends AppCompatActivity {
    String usuario, nombreUsuario, numeroUsuario;

    String empresa;
    ConexionSqlServer conex=new ConexionSqlServer();
    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();
    Button acep;
    ModeloDatos md=new ModeloDatos();
    CheckBox check;
    ArrayList listaTiendas=new ArrayList();
    ArrayList listaTipo=new ArrayList();
    ArrayList listaZonas=new ArrayList();

    String numeroT;
    public String numeroTienda;
    private Spinner spTiendas, spZona,spTipo;
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"db tienda",null,1);
    String repetido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_principal);
        spTiendas=(Spinner)findViewById(R.id.spTiendas);
        spZona=(Spinner)findViewById(R.id.spZona);
        check=(CheckBox)findViewById(R.id.check);
        spTipo=(Spinner)findViewById(R.id.spTipo);
        acep=(Button)findViewById(R.id.prueba);

        llenarSpTipo();
        ArrayAdapter<String> adapterTipo = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, listaTipo);
        spTipo.setAdapter(adapterTipo);

        spTipo.setSelection(ultimaVezTipo());

        crearComanderoLog();
        crearComanderoDet();
        crearUbicacionTabla();
        crearAreaAsignadaTabla();
        crearZonaTabla();
        crearComanderoTabla();
        crearAreasTabla();


        obtenerTiendas();

        ArrayAdapter<String> adapterTiendas = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, listaTiendas);
        spTiendas.setAdapter(adapterTiendas);





        spTiendas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ArrayAdapter<String> adapterZonas=new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, listaZonas);
                adapterZonas.clear();
               obtenerNumeroTienda();

                consultarZona();


                spZona.setAdapter(adapterZonas);
                spZona.setSelection(obtenerPosicionItem(spZona, ultimaZona()));

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        acep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                if(spZona.getSelectedItem()!=null){


                    carga();

                }else{

                    Toast.makeText(getApplicationContext(),"Registra un Área ",Toast.LENGTH_LONG).show();
                    entrarSinArea();

                }




            }
        });


    }


    public void carga(){

        int hola=1;
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        hola++;
        obtenerNumero();
        ultimaVez();
        deleteArea();
        guardarArea();

        hola++;





    }

    public void deleteArea(){


            try{
                ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
                SQLiteDatabase db=conn.getWritableDatabase();
                db.execSQL("DELETE FROM "+Utilidades.TABLA_AREA);
            }catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error al actualizar area", Toast.LENGTH_LONG);
            }
    }

    private void guardarArea() {
        String idArea=consultarIDArea();
        String tipo=spTipo.getSelectedItem().toString();
        if(tipo.equals("VENDEDOR")){
            tipo="1";
        }else if(tipo.equals("ALMACEN")){
            tipo="2";
        }else{
            tipo="0";
        }

        try{
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
            SQLiteDatabase db=conn.getWritableDatabase();
            db.execSQL("INSERT INTO  "+Utilidades.TABLA_AREA+"  ("+Utilidades.CAMPO_AREA+","+Utilidades.CAMPO_TIPO+") VALUES('"+idArea+"','"+tipo+"')");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error al guardar Area en sql Lite",Toast.LENGTH_LONG);
        }


    }

    public int ultimaVezArea(){
        int tipo=0;
        try{
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
            SQLiteDatabase db=conn.getWritableDatabase();
            String sql="SELECT "+Utilidades.CAMPO_AREA+" FROM "+Utilidades.TABLA_AREA;
            Cursor cr=db.rawQuery(sql,null);
            while (cr.moveToNext()){
                tipo=cr.getInt(0);
            }


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error al guardar Area en sql Lite",Toast.LENGTH_LONG);
        }
        return tipo;
    }




    public int ultimaVezTipo(){
        int tipo=0;
        try{
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
            SQLiteDatabase db=conn.getWritableDatabase();
            String sql="SELECT "+Utilidades.CAMPO_TIPO+" FROM "+Utilidades.TABLA_AREA;
            Cursor cr=db.rawQuery(sql,null);
            while (cr.moveToNext()){
                tipo=cr.getInt(0);
            }


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error al guardar Area en sql Lite",Toast.LENGTH_LONG);
        }
        return tipo;
    }
    public  String  consultarIDArea() {
        String idTienda = numeroT;
        ModeloNumeroOrden mno = new ModeloNumeroOrden();
        String area = getArea();
        String idArea = "";

        try {
            String tienda = numeroT;
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String zona;
            zona=spZona.getSelectedItem().toString();
            String sql = "Select idarea from areasdecontrol where nombre='" + zona+ "' and idTienda=" + idTienda;
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                idArea=rs.getString(1);

            }


        } catch (SQLException e) {

        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }

        return idArea;
    }


    public void obtenerNumero(){
        try {



            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select numero from tiendas where nombre='"+spTiendas.getSelectedItem()+"'");
            ModeloTienda mt=new ModeloTienda();
            mt.setNumeroTienda(spTiendas.getSelectedItem().toString());
            while (rs.next()) {


                numeroTienda=rs.getString(1);

                usuario= getIntent().getStringExtra("Usuario");
                empresa=getIntent().getStringExtra("Empresa");
                getNameUser();
                insertarEntrada();

                Intent inte= new Intent(getApplicationContext(), menu.class);
                inte.putExtra("listado",numeroTienda);
                inte.putExtra("Usuario",usuario);
                inte.putExtra("Empresa",empresa);


                SQLiteDatabase db=conn.getWritableDatabase();
                ContentValues values=new ContentValues();



                values.put(Utilidades.CAMPOS_NOMBRE,numeroTienda);
                values.put(Utilidades.CAMPOS_DATOS,"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa");
                values.put(Utilidades.CAMPOS_STATTUS,"3");
                Long idResultante=db.insert(Utilidades.TABLA_TIENDA,Utilidades.CAMPOS_NOMBRE,values);
                db.close();



                actualizar();

                insertarAreaAsignada();
                startActivity(inte);

            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en sp", Toast.LENGTH_SHORT).show();
        }

    }
    public void entrarSinArea(){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select numero from tiendas where nombre='"+spTiendas.getSelectedItem()+"'");


            while (rs.next()) {


                numeroTienda=rs.getString(1);

                usuario= getIntent().getStringExtra("Usuario");
                empresa=getIntent().getStringExtra("Empresa");
                getNameUser();
                insertarEntrada();

                Intent inte= new Intent(getApplicationContext(), PrimerRegistroArea.class);
                inte.putExtra("listado",numeroTienda);
                inte.putExtra("Usuario",usuario);
                inte.putExtra("Empresa",empresa);


                SQLiteDatabase db=conn.getWritableDatabase();
                ContentValues values=new ContentValues();

                values.put(Utilidades.CAMPOS_NOMBRE,numeroTienda);
                values.put(Utilidades.CAMPOS_DATOS,"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa"+"-"+"aa");
                values.put(Utilidades.CAMPOS_STATTUS,"3");
                Long idResultante=db.insert(Utilidades.TABLA_TIENDA,Utilidades.CAMPOS_NOMBRE,values);
                db.close();



                actualizar();

                insertarAreaAsignada();
                startActivity(inte);

            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en sp", Toast.LENGTH_SHORT).show();
        }

    }

    public void obtenerNumeroTienda(){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select numero from tiendas where nombre='"+spTiendas.getSelectedItem()+"'");


            while (rs.next()) {


                numeroTienda=rs.getString(1);

            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en sp", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

        }

        return true;

    }
    public void actualizarZona(String zona){



            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
            SQLiteDatabase db=conn.getWritableDatabase();




            db.execSQL("UPDATE zona SET zona="+zona+"");



    }

    public void actualizar(){
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={"1"};
        ContentValues values=new ContentValues();
        try {
       values.put(Utilidades.CAMPOS_NOMBRE,numeroTienda);
       db.update(Utilidades.TABLA_TIENDA,values,Utilidades.CAMPO_ID+"=?",parametros);
       db.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"no pos no" , Toast.LENGTH_LONG).show();
        }

    }
public void ultimaVez(){
    ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
    SQLiteDatabase db = conn.getReadableDatabase();

    String sql="SELECT nombreT FROM tienda where id=1";
    Cursor cursor = db.rawQuery(sql, null);
    while (cursor.moveToNext()) {
         numeroT=(cursor.getString(0));
        getTienda(numeroT);

    }
}
public void getTienda(String numeroT){

    try {
        Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
        ResultSet rs = st.executeQuery("select nombre from tiendas where numero="+numeroT+";");


        while (rs.next()) {


            listaTiendas.add(rs.getString(1));



        }


    } catch (Exception e) {
        Toast.makeText(getApplicationContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
    }
}


public void consultarZona(){
    try {
        Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
        ResultSet rs = st.executeQuery("SELECT nombre FROM areasdecontrol where idTienda="+numeroTienda+";");


        while (rs.next()) {


            listaZonas.add(rs.getString(1));



        }


    } catch (Exception e) {
        Toast.makeText(getApplicationContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
    }
}

    public String ultimaZona(){
        String nombreArea="";
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="SELECT nombre FROM areasdecontrol where idTienda="+numeroTienda+" and idarea="+ultimaVezArea()+";";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {

                nombreArea=rs.getString(1);

                }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
        }
        return nombreArea;
    }



    private void obtenerTiendas() {
        ultimaVez();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select  nombre from tiendas ");


                while (rs.next())
                {

                    listaTiendas.add(rs.getString(1));
                }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en traer tiendas", Toast.LENGTH_SHORT).show();
        }
    }


    private void insertarEntrada() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss", Locale.getDefault());
        Date date = new Date();

        String fecha = dateFormat.format(date);

        String[] FechaHora;
        FechaHora=fecha.split("-");

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            st.executeUpdate("insert into logdia (nombre, fecha, tienda, hora,origen, tipo, idEmpleado, caja, id, llave, autoriza) values ('"+nombreUsuario+"', getDate(),1,'"+FechaHora[1]+"', 1, 'ENTRADA',"+numeroUsuario+",1 ,92911, newId(), 0 );");



        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al insertar en asistencias", Toast.LENGTH_SHORT).show();
        }
    }



    public void getNameUser(){
        ModeloUsuario mu=new ModeloUsuario();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select nombre, numero from empleado where [user]='"+mu.getCorreo()+"';");


            while (rs.next()) {

             nombreUsuario=rs.getString(1);
             numeroUsuario=rs.getString(2);

            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
        }
    }
    public void InsertarZona(String zona){


        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db=conn.getWritableDatabase();







        db.execSQL("INSERT INTO  zona  (zona) VALUES('"+zona+"')");

    }

    public void insertarAreaAsignada(){
        String id=getArea();
        try {

            if(check.isChecked()==true){
                id="-1";

            }
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="insert into AreasAsignadas (idTienda,idArea,idEmpleado,Fecha,Hora) values ("+numeroTienda+",'"+id+"',"+numeroUsuario+",getDate(),getDate());";
            st.executeQuery(sql);


        } catch (Exception e) {

        }
    }

    public String getArea(){
        String id=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select idArea from areasdecontrol where nombre='"+spZona.getSelectedItem().toString()+"'";
            ResultSet rs=st.executeQuery(sql);

            while(rs.next()){
                id=rs.getString(1);
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
        }
        return id;
    }


    public void crearComanderoTabla(){

        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Create table comandero(numero varchar(50), tienda numeric(5,0), cliente nvarchar(50), fecha date, total decimal(18,2), status nchar(1), pares numeric(4,0), empleado varchar(50), impreso bit, llave uniqueidentifier);";
            st.executeUpdate(sql);
        }catch (SQLException e){
            e.getMessage();
            Toast.makeText(getApplicationContext(),"No se puede crear la tabla Comandero",Toast.LENGTH_LONG);
        }

    }

    public void crearAreasTabla(){

        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Create table AreasDeControl(idArea numeric(18,0) IDENTITY, idTienda numeric(18,0), nombre nchar(50));";
            st.executeUpdate(sql);
        }catch (SQLException e){
            e.getMessage();
            Toast.makeText(getApplicationContext(),"No se puede crear la tabla AreasDeControl ",Toast.LENGTH_LONG);
        }

    }

    public void crearZonaTabla(){

        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Create table AreasDeControl (idZona numeric(18,0) IDENTITY, idTienda nvarchar(50), nombre nchar(50), idArea numeric(18, 0))";
            st.executeUpdate(sql);
        }catch (SQLException e){
            e.getMessage();
            Toast.makeText(getApplicationContext(),"No se puede crear la tabla AreasDeControl ",Toast.LENGTH_LONG);
        }

    }


    public void crearAreaAsignadaTabla(){
        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Create table AreasAsignadas (idTienda numeric(18,0), idArea nvarchar(50), idEmpleado numeric(18,0), fecha date, hora time(2))";
            st.executeUpdate(sql);
        }catch (SQLException e){
            e.getMessage();
            Toast.makeText(getApplicationContext(),"No se puede crear la tabla AreasDeControl ",Toast.LENGTH_LONG);
        }
    }

    public void crearUbicacionTabla(){
        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Create table UbicacionesProductos (idTienda numeric(18,0), idZona numeric(18,0), barcode nvarchar(50), id numeric(18,0) IDENTITY, hora time(2))";
            st.executeUpdate(sql);
        }catch (Exception e){
            e.getMessage();
        }
    }


    public void crearComanderoDet(){
        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Create table comanderoDet (numero varchar(50), tienda numeric(5,0), barcode nvarchar(50), estilo nvarchar(50),color nvarchar(50), marca nvarchar(50), acabado nvarchar(50), talla decimal(4,1), status char(1), ubicacion nvarchar(50), llave uniqueidentifier)";
            st.executeUpdate(sql);
        }catch (Exception e ){
            e.getMessage();
        }
    }



    public void crearComanderoLog(){
        try{
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Create table comanderoLog (numero varchar(50), fecha date, hora time(7), llave uniqueidentifier)";
            st.executeUpdate(sql);
        }catch (Exception e ){
            e.getMessage();
        }
    }

    public void llenarSpTipo(){
        listaTipo.add(" ");
        listaTipo.add("VENDEDOR");
        listaTipo.add("ALMACEN");

    }


    public static int obtenerPosicionItem(Spinner spinner, String fruta) {
        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < spinner.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(fruta)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }


    }



