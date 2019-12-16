package com.example.saz.saz;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
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

import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloTime;
import com.example.saz.saz.Modelo.ModeloUsuario;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.utilidades.ModeloTienda;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimerTask;

public class Hamburguesa extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FragmentManager fm=getSupportFragmentManager();

    public static ConexionBDCliente bdc=new ConexionBDCliente();
    public static ModeloEmpresa me=new ModeloEmpresa();
    View root;
    static Context context;
    Time actual;



    ModeloTienda mt=new ModeloTienda();
    String nombreTieda;

    String usuario, numeroUsuario, nombreUsuario;
    ModeloUsuario mu=new ModeloUsuario();
    String empresa,listalista;
    private TimerTask tarea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamburguesa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SazMobile Pro -Registro de pedidos-");


        if(Principal.busqueda2==true){
            fm.beginTransaction().replace(R.id.contenedor, new Consulta_Marca()).commit();
        }else  {
            Principal.passConsulta=false;
            fm.beginTransaction().replace(R.id.contenedor, new ConsultaF()).commit();
        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.usu);
        TextView navSucursal=(TextView) headerView.findViewById(R.id.sucu);

        getNameTienda();
        navUsername.setText("Usuario: "+mu.getNombre());
        navSucursal.setText("Sucursal: "+nombreTieda);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
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

    public void consultarHora(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

            Date date = new Date();
            String aux = dateFormat.format(date);


            actual = Time.valueOf(aux);
        }catch (Exception e){

        }
    }




    public void verificarTiempo() {
        ModeloTime time=new ModeloTime();

        Toast.makeText(this, ""+time.getTime(), Toast.LENGTH_LONG).show();
        try {
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "SELECT DATEADD(MINUTE,+"+time.getTime()+",ComanderoLog.hora),ComanderoLog.hora,comandero.numero FROM ComanderoLog INNER JOIN  comandero ON comandero.numero=ComanderoLog.numero WHERE [status]=1;  ";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Time horaMas= rs.getTime(1);
                Time horaAct=rs.getTime(2);
                String numero=rs.getString(3);


                if(!numero.isEmpty()){
                    compararHoras(horaAct,horaMas,numero);
                }



            }


        } catch (Exception e) { Toast.makeText(this, "No hay pedidos para reiniciar o no se a configurado el tiempo de los pedidos ", Toast.LENGTH_SHORT).show();
        }

    }



    public void compararHoras(Time horaAct,Time horaMas, String numero){

        if(actual.compareTo(horaMas)>=0){
            tiempoExpirado(numero);
        }else{


        }
    }


    public void tiempoExpirado(String numero){
        try {
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "UPDATE comandero SET [status]=5 WHERE numero="+numero+"";
            st.executeUpdate(sql);
            tiempoEXpiradoPares(numero);
            obtenerDatos(numero);


        } catch (Exception e) { Toast.makeText(this, "Error liberar", Toast.LENGTH_SHORT).show();
        }
    }
    public void tiempoEXpiradoPares(String numero){
        try {
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "UPDATE comanderoDet SET [status]=5 WHERE numero="+numero+"";
            st.executeUpdate(sql);



        } catch (Exception e) { Toast.makeText(this, "Error liberar en comanderoDet", Toast.LENGTH_SHORT).show();
        }
    }

   public void obtenerDatos(String numero){
       try {
           Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
           String sql = "SELECT barcode, talla, tienda FROM comanderoDet WHERE numero="+numero;
           ResultSet rs = st.executeQuery(sql);

           while (rs.next()) {

               String barcode=rs.getString(1);
               String talla=rs.getString(2);
               String tienda=rs.getString(3);
               devolderPares(barcode, talla, tienda);



           }



       } catch (Exception e) { Toast.makeText(this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
       }
   }


   public void devolderPares(String barcode, String talla, String tienda){
       try {

           Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
           String sql="update existen set cantreal= cantreal - 1 ,  pedido = pedido - 1 where barcode ='" + barcode+ "' and talla = " + talla + " and tienda = " + tienda;
           st.executeUpdate(sql);



       } catch (Exception e) {
           Toast.makeText(this, "Error al devolver pares", Toast.LENGTH_SHORT).show();
       }

   }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.hamburguesa, menu);
        return true;
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            //Acción
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            Intent intent = new Intent(this, menu.class);
            Principal.location=0;


            startActivity(intent);


        } else if (id == R.id.nav_gallery) {

            Intent intent=new Intent(getApplicationContext(),OrdenEspera.class);
            OrdenEspera.pass=false;
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setIcon(R.mipmap.ic_launcher);
            progressDialog.setMessage("Reiniciando...");
            progressDialog.show();


            consultarHora();
            verificarTiempo();
            progressDialog.show();
            progressDialog.dismiss();


            Toast.makeText(this, "Pedidos reiniciados", Toast.LENGTH_LONG).show();



        } else if (id == R.id.nav_manage) {
            getSupportActionBar().setTitle("Configuración de pedidos");
            fm.beginTransaction().replace(R.id.contenedor, new ReiniciarPedido()).commit();
        } else if (id == R.id.nav_share) {


        } else if (id == R.id.nav_send) {

        }else if(id == R.id.RegistrarPedido){
            if(Principal.busqueda2==true){
                fm.beginTransaction().replace(R.id.contenedor, new Consulta_Marca()).commit();
            }else {
                Principal.passConsulta=false;
                fm.beginTransaction().replace(R.id.contenedor, new ConsultaF()).commit();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getNameTienda(){

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select nombre from tiendas where numero='"+ultimaVez()+"';";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {


                nombreTieda=rs.getString(1);



            }


        } catch (SQLException e) {
            e.getMessage();
            Toast.makeText(getApplicationContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
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


        } catch (SQLException e) {
            e.getMessage();
            Toast.makeText(getApplicationContext(), "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
        }
    }
}
