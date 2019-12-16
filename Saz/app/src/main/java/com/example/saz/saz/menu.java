package com.example.saz.saz;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
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
import com.google.firebase.iid.FirebaseInstanceId;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{


    String usuario, numeroUsuario, nombreUsuario;
ModeloUsuario mu=new ModeloUsuario();
    String empresa;

    String nombreTieda;

    String dispositivo;

    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"db tienda",null,1);

ModeloTienda mt=new ModeloTienda();
    FragmentManager fm=getSupportFragmentManager();
    ConexionSqlServer conex=new ConexionSqlServer();
    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();
    TextView idTienda;

    static Timer temporizadorComandero;
    private Handler handler = new Handler();
    String areaID = null;
    int tipo=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

      //  FirebaseInstanceId.getInstance().getInstanceId();

        obtenerLineaConexion();
        consultarT();
        getArea();
        getNameTienda();
        getNameUser();

        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        usuario= getIntent().getStringExtra("Usuario");

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.usu);
        TextView navSucursal=(TextView) headerView.findViewById(R.id.sucu);

        navUsername.setText("Usuario: "+nombreUsuario);
        navSucursal.setText("Sucursal: "+nombreTieda);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle("Saz Mobile APP -Inicio- ");



        if(Principal.location==1){

            getSupportActionBar().setTitle("Comandero");
            fm.beginTransaction().replace(R.id.contenedorMenu, new Coman()).commit();

        }else if(Principal.location==2){
            getSupportActionBar().setTitle("Venta");
            fm.beginTransaction().replace(R.id.contenedorMenu, new Venta()).commit();
            Principal.location=0;

        }else if(Principal.location==3){
            getSupportActionBar().setTitle("Configuración");
            fm.beginTransaction().replace(R.id.contenedorMenu, new  Ajustes()).commit();
            Principal.location=0;
        }else if(Principal.location==4){
            getSupportActionBar().setTitle("Registro de pedidos");
            fm.beginTransaction().replace(R.id.contenedorMenu, new ConsultaF()).commit();
            Principal.location=0;
        }





    }





    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

        }
        return true;
    }


    public void obtenerLineaConexion() {
        String empress = getIntent().getStringExtra("Empresa");
        me = new ModeloEmpresa();
        {
            try {
                Statement st = conex.conexionBD().createStatement();
                ResultSet rs = st.executeQuery("select  Server, Usuariosvr,PassSvr, basededatos, empresa from logins where idEmpresa= " + empress + " and status=1 and borrado=0 ");

                while (rs.next()) {

                    me.setServer(rs.getString("Server"));
                    me.setUsuario(rs.getString("Usuariosvr"));
                    me.setPass(rs.getString("PassSvr"));
                    me.setBase(rs.getString("basededatos"));
                    me.setEmpresa(rs.getString("empresa"));

                }
                st.close();


            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error en la linea de conexion", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);


        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    //Registramos la salida del usuario en la base de datos remota
    private void insertarSalida() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hh:mm:ss", Locale.getDefault());
        Date date = new Date();

        String fecha = dateFormat.format(date);

        String[] FechaHora;
        FechaHora=fecha.split("-");

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
           String sql="insert into logdia (nombre, fecha, tienda, hora,origen, tipo, idEmpleado, caja, id, llave, autoriza) values ('"+nombreUsuario+"', getDate(),1,'"+FechaHora[1]+"', 1, 'SALIDA',"+numeroUsuario+",1 ,92911, newId(), 0 );";
            st.executeUpdate(sql);
            st.close();


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al checar salida", Toast.LENGTH_SHORT).show();
        }
    }

//Obtenemos el nombre del ususario
    public void getNameUser(){
        String usu= getIntent().getStringExtra("Usuario");
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select nombre, numero from empleado where [user]='"+mu.getCorreo()+"';";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {

                nombreUsuario=rs.getString(1);
                numeroUsuario=rs.getString(2);
            }
            st.close();
        } catch (SQLException e) {
            e.getMessage();
            Toast.makeText(getApplicationContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            if (tipo==1)
            {
                Intent intent = new Intent(getApplicationContext(), Hamburguesa.class);
                startActivity(intent);
        }else{
                Toast toast = Toast.makeText(getApplicationContext(), "No tienes acceso a esta opción", Toast.LENGTH_LONG);
                TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                x.setTextColor(Color.BLACK); toast.show();
            }
        } else if (id == R.id.generales) {

            getSupportActionBar().setTitle("Configuración");
            fm.beginTransaction().replace(R.id.contenedorMenu, new Ajustes()).commit();
        } else if (id == R.id.nav_manage) {
            getSupportActionBar().setTitle("Comandero");
            mismoDispositivo();
            fm.beginTransaction().replace(R.id.contenedorMenu, new Coman()).commit();
        } else if (id == R.id.nav_new) {
            getSupportActionBar().setTitle("Nuevo Empleado");
            fm.beginTransaction().replace(R.id.contenedorMenu, new AddUser()).commit();
        } else if (id == R.id.nav_vender) {
            getSupportActionBar().setTitle("Vender");
            fm.beginTransaction().replace(R.id.contenedorMenu, new Venta()).commit();
        }else if (id == R.id.nav_cerrar) {
//            temporizadorComandero.cancel();
            getNameUser();
            insertarSalida();
            Intent  intent= new Intent(getApplicationContext(), Principal.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


          private void consultarT() {
              SQLiteDatabase db=conn.getReadableDatabase();
              String[] parametros={"1"};
              String[] campos={Utilidades.CAMPOS_NOMBRE};
              try {
                  Cursor cursor = db.query(Utilidades.TABLA_TIENDA, campos, Utilidades.CAMPO_ID+ "=?", parametros, null, null, null);
                  cursor.moveToFirst();
                  mt.setNombreTienda(cursor.getString(0));
                  cursor.close();

              }catch (Exception e){
                  Toast.makeText(this,"" , Toast.LENGTH_LONG).show();
              }

          }




    public String getTienda() {
        String tienda = "";
        try {
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext(), "db tienda", null, 1);
            SQLiteDatabase db = conn.getReadableDatabase();


            Cursor cursor = db.rawQuery("SELECT nombreT FROM " + Utilidades.TABLA_TIENDA, null);
            while (cursor.moveToNext()) {
                tienda = (cursor.getString(0));


            }
        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        } catch (Exception e) {

        }
        return tienda;
    }

    public void getArea() {
        try {
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext(), "db tienda", null, 1);
            SQLiteDatabase db = conn.getReadableDatabase();

            String sql = "SELECT area, tipo FROM " + Utilidades.TABLA_AREA;
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                areaID = (cursor.getString(0));
                tipo=cursor.getInt(1);
            }
        } catch (RuntimeException r) {

        }
    }


    public String ultimaVez() {

        String numeroT = null;
        try {
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext(), "db tienda", null, 1);
            SQLiteDatabase db = conn.getReadableDatabase();

            String sql = "SELECT nombreT FROM tienda where id=1";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                numeroT = (cursor.getString(0));
            }
        } catch (RuntimeException r) {

        }
        return numeroT;
    }













    public void getNameTienda(){

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select nombre from tiendas where numero='"+ultimaVez()+"';";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                nombreTieda=rs.getString(1);

            }
            st.close();


        } catch (SQLException e) {
            e.getMessage();
            Toast.makeText(getApplicationContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
        }catch (Exception es){
            es.getMessage();

        }
    }

    public String getIdEmpresa(){
        String id="";
        try{
            Statement st= conex.conexionBD().createStatement();

            String sql="select idEmpresa from logins where Empresa='"+me.getEmpresa()+"'";
            ResultSet rs=st.executeQuery(sql);

            while(rs.next()){
                id=rs.getString(1);
            }
            st.close();

        }catch (Exception e ){
            e.getMessage();
            Toast.makeText(getApplicationContext(), "No sé puede consultar el id de la empresa", Toast.LENGTH_SHORT).show();
        }
        return id;
    }
    public void mismoDispositivo(){

        String modelo = Build.MODEL;
        String serie = Build.MANUFACTURER;
        String marca = Build.ID;
        try{
            Statement st= conex.conexionBD().createStatement();

            String sql="  SELECT idDisp FROM smAppAccesos where mail='" + mu.getCorreo() + "' and idempresa=" + getIdEmpresa()+" and app=3";
            ResultSet rs=st.executeQuery(sql);

            while(rs.next()){
                dispositivo=rs.getString(1);
            }
            st.close();

        }catch (Exception e ){
            e.getMessage();
            Toast.makeText(getApplicationContext(), "No sé puede actualizar el dispositivo", Toast.LENGTH_SHORT).show();
        }

        if (!dispositivo.isEmpty()) {

            String predeterminada = dispositivo;

            String entrada = marca+"-"+serie+"-"+modelo;


            String aux = "";


            if (entrada!= null) {
                if (predeterminada.length() == entrada.length()) {

                    for (int i = 0; i < predeterminada.length(); i++) {


                        if (predeterminada.charAt(i) == entrada.charAt(i)) {

                            aux += predeterminada.charAt(i);
                        }
                    }


                    if (aux.equals(predeterminada)) {



                    } else {

                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getApplicationContext() );
                        alerta.setMessage("Se ha cerrado la sesión actual ya que otro usuario ha accedido con tus datos en otro dispositivo.")
                                .setCancelable(false).setIcon(R.drawable.aviso)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getApplicationContext(), Principal.class);
                                        startActivity(intent);


                                    }
                                });

                        android.app.AlertDialog titulo=alerta.create();
                        titulo.setTitle("Aviso");
                        titulo.show();

                    }


                } else {

                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getApplicationContext() );
                    alerta.setMessage("Se ha cerrado la sesión actual ya que otro usuario ha accedido con tus datos en otro dispositivo.")
                            .setCancelable(false).setIcon(R.drawable.aviso)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), Principal.class);
                                    startActivity(intent);


                                }
                            });

                    android.app.AlertDialog titulo=alerta.create();
                    titulo.setTitle("Aviso");
                    titulo.show();
                }
            }
        }
    }





}
