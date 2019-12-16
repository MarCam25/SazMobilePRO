package com.example.saz.saz;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


import static android.content.ContentValues.TAG;

public class ActPrincipal extends AppCompatActivity {
    String usuario, nombreUsuario, numeroUsuario;

    String empresa;
    ConexionSqlServer conex=new ConexionSqlServer();
    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();
    Button acep;

    CheckBox check;
    ArrayList listaTiendas=new ArrayList();
    ArrayList listaTipo=new ArrayList();
    ArrayList listaZonas=new ArrayList();
    String token="";
    String numeroT;
    public String numeroTienda;
    private Spinner spTiendas, spZona,spTipo;
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"db tienda",null,1);


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

        CrearTablas crearTablas=new CrearTablas();
        crearTablas.execute();



        obtenerTiendas();



        registrarToken token=new registrarToken();
        token.execute();

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

    public String getIdUsuario(){
        String idUsuario=null;
        ModeloUsuario mu=new ModeloUsuario();
        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "Select numero from empleado where nombre='"+mu.getNombre()+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                idUsuario=(rs.getString(1));

            }

            st.close();

        } catch (RuntimeException r){

        }catch (Exception e) {
        }
        return idUsuario;
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

            st.close();
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
            st.close();


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
            st.close();

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
            st.close();
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

        st.close();
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
        st.close();

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

            st.close();


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
                st.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en traer tiendas", Toast.LENGTH_SHORT).show();
        }
    }


    private void insertarEntrada() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hh:mm:ss", Locale.getDefault());
        Date date = new Date();

        String fecha = dateFormat.format(date);

        String[] FechaHora;
        FechaHora=fecha.split("-");

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            st.executeUpdate("insert into logdia (nombre, fecha, tienda, hora,origen, tipo, idEmpleado, caja, id, llave, autoriza) values ('"+nombreUsuario+"', getDate(),1,'"+FechaHora[1]+"', 1, 'ENTRADA',"+numeroUsuario+",1 ,92911, newId(), 0 );");
            st.close();


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
            st.close();
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
            String sql="if exists (select 1 from areasAsignadas where idEmpleado ="+getIdUsuario()+" ) begin" +
                    " update areasAsignadas set token ='"+token+"', idArea="+id+", idTienda="+numeroTienda+"  where idEmpleado = "+getIdUsuario()+"  end else begin" +
                    " insert into AreasAsignadas (idTienda,idArea,idEmpleado,Fecha,Hora,token) values ("+numeroTienda+",'"+id+"',"+numeroUsuario+",getDate(),getDate(),'"+token+"') end";
                     st.executeQuery(sql);
                     st.close();


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
            st.close();

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
        }
        return id;
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



    private class CrearTablas extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {

            try{
                Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
                String sql="Create table comanderoLog (numero varchar(50), fecha date, hora time(7), llave uniqueidentifier)";
                st.executeUpdate(sql);


                sql="Create table comanderoDet (numero varchar(50), tienda numeric(5,0), barcode nvarchar(50), estilo nvarchar(50),color nvarchar(50), marca nvarchar(50), acabado nvarchar(50), talla decimal(4,1), status char(1), ubicacion nvarchar(50), llave uniqueidentifier)";
                st.executeUpdate(sql);

                sql="Create table UbicacionesProductos (idTienda numeric(18,0), idZona numeric(18,0), barcode nvarchar(50), id numeric(18,0) IDENTITY)";
                st.executeUpdate(sql);

                sql="Create table AreasAsignadas (idTienda numeric(18,0), idArea nvarchar(50), idEmpleado numeric(18,0), fecha date, hora time(2),token nvarchar(150) not null)";
                st.executeUpdate(sql);

                sql="Create table AreasDeControl (idArea numeric(18,0) IDENTITY not null, idTienda nvarchar(50) not null, nombre nchar(50) not null)";
                st.executeUpdate(sql);

                sql="Create table comandero(numero varchar(50), tienda numeric(5,0), cliente nvarchar(50), fecha date, total decimal(18,2), status nchar(1), pares numeric(4,0), empleado varchar(50), impreso bit, llave uniqueidentifier);";
                st.executeUpdate(sql);

                sql="create table ZonasDeSurtido(\n" +
                        "idZona numeric identity(1,1) ,\n" +
                        "idTienda nvarchar(50) not null,\n" +
                        "nombre nvarchar(50) not null,\n" +
                        "idArea numeric(18,0) not null\n" +
                        ")";
                st.executeUpdate(sql);

                sql="drop procedure LupitaApartados";
                st.executeUpdate(sql);

                sql="CREATE PROCEDURE [dbo].[LupitaApartados] @Barcode AS VARCHAR(8000) = NULL\n" +
                        "\t,@TallaBusca AS VARCHAR(5)\n" +
                        "\t,@TiendaBusca AS DECIMAL \n" +
                        "\t,@FechaBusca AS VARCHAR(8)\n" +
                        "AS\n" +
                        "SET NOCOUNT ON\n" +
                        "\n" +
                        "DECLARE @SQL AS NVARCHAR(MAX)\n" +
                        "\t,@Tienda AS NVARCHAR(200)\n" +
                        "\t,@Talla AS NVARCHAR(5)\n" +
                        "\t,@Cantidad AS NVARCHAR(10)\n" +
                        "\t,@Tallas AS NVARCHAR(MAX)\n" +
                        "\t,@TallasSort AS NVARCHAR(MAX)\n" +
                        "\t,@TallasUpdate AS NVARCHAR(MAX)\n" +
                        "\n" +
                        "IF object_id('tempdb..##TempTallas') IS NOT NULL\n" +
                        "BEGIN\n" +
                        "\tDROP TABLE ##TempTallas\n" +
                        "END\n" +
                        "\n" +
                        "IF object_id('tempdb..##TempTienda') IS NOT NULL\n" +
                        "BEGIN\n" +
                        "\tDROP TABLE ##TempTienda\n" +
                        "END\n" +
                        "\n" +
                        "IF object_id('tempdb..##Temp') IS NOT NULL\n" +
                        "BEGIN\n" +
                        "\tDROP TABLE ##Temp\n" +
                        "END\n" +
                        "\n" +
                        "CREATE TABLE ##TempTienda (Tienda INT NOT NULL)\n" +
                        "\n" +
                        "IF @TiendaBusca <= 0\n" +
                        "BEGIN\n" +
                        "\tSET @SQL = 'Insert Into ##TempTienda (Tienda) Select Distinct Tienda From Existen'\n" +
                        "END\n" +
                        "ELSE\n" +
                        "BEGIN\n" +
                        "\tSET @SQL = 'Insert Into ##TempTienda (Tienda) Select Distinct Tienda From Existen WHERE Tienda = ' + CAST(@TiendaBusca AS NVARCHAR(10))\n" +
                        "END\n" +
                        "EXECUTE (@SQL)\n" +
                        "\n" +
                        "CREATE TABLE ##Temp (\n" +
                        "\tBarcode VARCHAR(100) collate database_default NOT NULL\n" +
                        "\t,Tienda VARCHAR(100) collate database_default NOT NULL\n" +
                        "\t,Estilo VARCHAR(100) collate database_default NOT NULL\n" +
                        "\t,Color VARCHAR(100) collate database_default NOT NULL\n" +
                        "\t,Acabado VARCHAR(100) collate database_default NOT NULL\n" +
                        "\t,Marca VARCHAR(100) collate database_default NOT NULL\n" +
                        "\t,SubLinea VARCHAR(100) collate database_default NOT NULL\n" +
                        "\t,Linea VARCHAR(100) collate database_default NOT NULL\n" +
                        "\t,Corrida VARCHAR(100) collate database_default NOT NULL\n" +
                        "\t,Temporada VARCHAR(100) collate database_default NOT NULL\n" +
                        "\t,Basico BIT NOT NULL\n" +
                        "\t)\n" +
                        "\n" +
                        "DECLARE @CompTalla AS NVARCHAR(MAX)\n" +
                        "DECLARE @CompTiend AS NVARCHAR(MAX)\n" +
                        "DECLARE @CompFecha AS NVARCHAR(250)\n" +
                        "DECLARE @SiHay AS INT\n" +
                        "\n" +
                        "SET @SiHay = 0\n" +
                        "\n" +
                        "IF CAST(@TallaBusca AS DECIMAL) <= 0\n" +
                        "BEGIN\n" +
                        "\tSET @CompTalla = ''\n" +
                        "END\n" +
                        "ELSE\n" +
                        "BEGIN\n" +
                        "\tSET @CompTalla = 'AND existen.Talla = ' + CAST(@TallaBusca AS NVARCHAR(10))\n" +
                        "END\n" +
                        "\n" +
                        "IF @TiendaBusca <= 0\n" +
                        "BEGIN\n" +
                        "\tSET @CompTiend = ''\n" +
                        "END\n" +
                        "ELSE\n" +
                        "BEGIN\n" +
                        "\tSET @CompTiend = ' AND existen.TIENDA = ' + CAST(@TiendaBusca AS NVARCHAR(10))\n" +
                        "END\n" +
                        "\n" +
                        "IF NOT NULLIF(@FechaBusca, '') IS NULL\n" +
                        "BEGIN\n" +
                        "    SET @CompFecha = ' AND detap.FECHA >= ''' + @FechaBusca + ''''\n" +
                        "END\n" +
                        "ELSE\n" +
                        "BEGIN\n" +
                        "    SET @CompFecha = ''\n" +
                        "END\n" +
                        "\n" +
                        "SET @SQL = 'SELECT existen.BARCODE, tiendas.NOMBRE AS Tienda, existen.TALLA, ISNULL(existen.CANTIDAD, 0) - ISNULL(existen.CANTREAL, 0) - (SELECT ISNULL(SUM(CANTIDAD), 0) AS CUANTOS FROM detap WHERE BARCODE = existen.BARCODE AND PUNTO = existen.TALLA AND TIENDA = existen.TIENDA) AS CANTIDAD Into ##TempTallas FROM existen LEFT JOIN tiendas ON existen.TIENDA = tiendas.NUMERO Where (Existen.Tienda In (Select Tienda From ##TempTienda)) And (Existen.Cantidad <> 0) ' + @CompTalla + @CompTiend + ' and existen.barcode in(''' + @Barcode + ''') GROUP BY existen.BARCODE, tiendas.NOMBRE, existen.TALLA, existen.TIENDA, Existen.Cantidad, existen.CANTREAL ORDER BY existen.BARCODE, existen.TALLA'\n" +
                        "EXECUTE (@SQL)\n" +
                        "\n" +
                        "SET @sql = 'DECLARE temp_cursor CURSOR FOR SELECT Distinct existen.TALLA FROM existen INNER JOIN tiendas ON existen.TIENDA = tiendas.NUMERO INNER JOIN articulo ON existen.BARCODE = articulo.BARCODE INNER JOIN colores ON articulo.COLOR = colores.numero INNER JOIN acabados ON articulo.ACABADO = acabados.numero INNER JOIN marcas ON articulo.MARCA = marcas.numero INNER JOIN sublinea ON articulo.SUBLINEA = sublinea.numero INNER JOIN lineas ON articulo.LINEA = lineas.NUMERO inner join corridas on corridas.id=articulo.corrida Where (Existen.Tienda In (Select Tienda From ##TempTienda)) And (Existen.Cantidad <> 0) and articulo.barcode in( ''' + @Barcode + ''') ' + @CompTalla + @CompTiend + ' Order by Talla'\n" +
                        "EXECUTE (@SQL)\n" +
                        "\n" +
                        "OPEN temp_cursor\n" +
                        "\n" +
                        "FETCH NEXT\n" +
                        "FROM temp_cursor\n" +
                        "INTO @Talla\n" +
                        "\n" +
                        "SET @Tallas = ''\n" +
                        "SET @TallasSort = ''\n" +
                        "SET @TallasUpdate = ''\n" +
                        "\n" +
                        "WHILE @@FETCH_STATUS = 0\n" +
                        "BEGIN\n" +
                        "\tSET @SiHay = 1\n" +
                        "\tSET @Tallas = @Tallas + 'Isnull([' + @Talla + '], 0) + '\n" +
                        "\tSET @TallasSort = @TallasSort + 'Isnull([' + @Talla + '],0) as [' + @Talla + '],'\n" +
                        "\tSET @TallasUpdate = @TallasUpdate + '[' + @Talla + '] = (Select top 1 Cantidad From ##TempTallas Where (Barcode = ##Temp.Barcode) And (Tienda = ##Temp.Tienda) And (Talla = ' + @Talla + ')),'\n" +
                        "\tSET @SQL = 'ALTER TABLE ##Temp ADD [' + @Talla + '] int NULL '\n" +
                        "\tEXECUTE (@SQL)\n" +
                        "\n" +
                        "\tFETCH NEXT\n" +
                        "\tFROM temp_cursor\n" +
                        "\tINTO @Talla\n" +
                        "END\n" +
                        "\n" +
                        "CLOSE temp_cursor\n" +
                        "\n" +
                        "DEALLOCATE temp_cursor\n" +
                        "\n" +
                        "IF @SiHay = 1\n" +
                        "BEGIN\n" +
                        "\tBEGIN TRY\n" +
                        "\t\tSET @Tallas = Left(@Tallas, Len(@Tallas) - 2)\n" +
                        "\t\tSET @TallasUpdate = Left(@TallasUpdate, Len(@TallasUpdate) - 1)\n" +
                        "\n" +
                        "\t\tALTER TABLE ##Temp ADD Total INT NULL\n" +
                        "\n" +
                        "\t\tSET @SQL = 'Insert Into ##Temp (Barcode, Tienda, Estilo, Color, Acabado, Marca, SubLinea, Linea, Corrida, Temporada, Basico) SELECT distinct existen.BARCODE, tiendas.NOMBRE AS Tienda, articulo.ESTILO, colores.COLOR, acabados.ACABADO, marcas.MARCA, sublinea.SUBLINEA, Lineas.Linea, Corridas.Nombre, Temporad.Temporad, isnull(Articulo.basico,0) AS Basico FROM existen INNER JOIN tiendas ON existen.TIENDA = tiendas.NUMERO INNER JOIN articulo ON existen.BARCODE = articulo.BARCODE INNER JOIN colores ON articulo.COLOR = colores.numero INNER JOIN acabados ON articulo.ACABADO = acabados.numero INNER JOIN marcas ON articulo.MARCA = marcas.numero INNER JOIN sublinea ON articulo.SUBLINEA = sublinea.numero INNER JOIN lineas ON articulo.LINEA = lineas.NUMERO inner join corridas on corridas.id=articulo.corrida inner join temporad on temporad.numero=articulo.temporad Where (Existen.Tienda In (Select Tienda From ##TempTienda)) And (Existen.Cantidad <> 0) and articulo.barcode in (''' + @Barcode + \n" +
                        "\t\t\t''') Order by articulo.ESTILO, colores.COLOR, acabados.ACABADO, tiendas.NOMBRE'\n" +
                        "\t\tEXECUTE (@SQL)\n" +
                        "\n" +
                        "\t\t--SELECT * INTO dbo.datos FROM ##Temp \n" +
                        "\t\t--DECLARE temp_cursor CURSOR FOR \n" +
                        "\t\tSET @SQL = 'Update ##Temp Set ' + @TallasUpdate\n" +
                        "\t\tEXECUTE (@SQL)\n" +
                        "\n" +
                        "\t\tSET @SQL = 'Update ##Temp Set Total = ' + @Tallas\n" +
                        "\t\tEXECUTE (@SQL)\n" +
                        "\n" +
                        "\t\t--Set @SQL = 'Select Tienda, Estilo, Color, Acabado, Marca, SubLinea, Linea,Corrida,Temporada,Basico,' + @TallasSort + ' Total From ##Temp  '\n" +
                        "\t\tSET @SQL = 'Select Tienda, ' + @TallasSort + ' Total From ##Temp'\n" +
                        "\t\tEXECUTE (@SQL)\n" +
                        "\tEND TRY\n" +
                        "\tBEGIN CATCH\n" +
                        "\t\tSELECT ERROR_NUMBER() AS ErrorNumber\n" +
                        "\t\t\t,ERROR_SEVERITY() AS ErrorSeverity\n" +
                        "\t\t\t,ERROR_STATE() AS ErrorState\n" +
                        "\t\t\t,ERROR_PROCEDURE() AS ErrorProcedure\n" +
                        "\t\t\t,ERROR_LINE() AS ErrorLine\n" +
                        "\t\t\t,ERROR_MESSAGE() AS ErrorMessage;\n" +
                        "\tEND CATCH\n" +
                        "END\n" +
                        "\n" +
                        "IF object_id('tempdb..##TempTallas') IS NOT NULL\n" +
                        "BEGIN\n" +
                        "\tDROP TABLE ##TempTallas\n" +
                        "END\n" +
                        "\n" +
                        "IF object_id('tempdb..##TempTienda') IS NOT NULL\n" +
                        "BEGIN\n" +
                        "\tDROP TABLE ##TempTienda\n" +
                        "END\n" +
                        "\n" +
                        "IF object_id('tempdb..##Temp') IS NOT NULL\n" +
                        "BEGIN\n" +
                        "\tDROP TABLE ##Temp\n" +
                        "END";
                st.executeUpdate(sql);

                st.close();


            }catch (Exception e ){
                e.getMessage();
            }


            return null;
        }
    }



    private class registrarToken extends AsyncTask<Void,Void,Void>
    {
        @Override
        protected Void doInBackground(Void... voids) {

            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    token = task.getResult().getToken();

                    // Log and toast
                    @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) String msg = getString(R.string.project_id, token);
                    Log.d(TAG, msg);

                }
            });
            return null;
        }

    }


}



