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
    String empresa,listalista;
    private TimerTask tarea;
    String nombreTieda;

    String dispositivo;



    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"db tienda",null,1);

ModeloTienda mt=new ModeloTienda();
    FragmentManager fm=getSupportFragmentManager();
    ConexionSqlServer conex=new ConexionSqlServer();
    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();
    TextView idTienda;
    ArrayList listaTiendas=new ArrayList();
    static Timer temporizadorComandero;
    private Handler handler = new Handler();
    String areaID = null;
    int tipo=0;


ModeloDatos md=new ModeloDatos();
    FragmentManager fmp=getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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



        getSupportActionBar().setTitle("Saz Mobile APP -Inicio-");

        if(Principal.location==1){
            getSupportActionBar().setTitle("Comandero");
            fm.beginTransaction().replace(R.id.contenedorMenu, new Coman()).commit();
            Principal.location=0;
        }else if(Principal.location==2){
            getSupportActionBar().setTitle("Venta");
            fm.beginTransaction().replace(R.id.contenedorMenu, new Venta()).commit();
            Principal.location=0;

        }else if(Principal.location==3){
            getSupportActionBar().setTitle("Configuración");
            fm.beginTransaction().replace(R.id.contenedorMenu, new  Ajustes()).commit();
            Principal.location=0;

        }

        IniciarTemporizador();


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
                x.setTextColor(Color.YELLOW); toast.show();
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
            getNameUser();
          insertarSalida();
          //cerramos secion en la app
            Intent  intent= new Intent(getApplicationContext(), Principal.class);
            //Cancelamos los hilos de notificaciones

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

    private void IniciarTemporizador() {
        try {
            temporizadorComandero = new Timer();
            tarea = new TimerTask() {
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            if (ConsultaF.not == true) {

                            }


                            if(tipo==2) {
                                consultarProductosComandero();
                            }else if(tipo==1){
                                consultarProductosVendedor();
                            }



                           /* notific = true;
                            vuelt = true;

                            ConsultarNuevosRegistros();


                            if (cantidadRegistros > Principal.hiloCantidadC) {
                                mandarNotificacione();
                                Principal.hiloCantidadC = cantidadRegistros;
                            } else {
                                Principal.hiloCantidadC = cantidadRegistros;
                            }
                            */

                        }
                    });
                }
            };
            temporizadorComandero.schedule(tarea, 0, 30000);
        } catch (RuntimeException r) {

        } catch (Exception e) {

        }
    }

    public void consultarProductosComandero() {
        String idTienda = getTienda();
        ModeloNumeroOrden mno = new ModeloNumeroOrden();

        String idArea=areaID;
        String llaveArea="";
        int cont=0;
        try {
            String tienda = ultimaVez();
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "select c.llave from comanderoDet c inner join UbicacionesProductos u on c.barcode = u.barcode inner join ZonasDeSurtido z on u.idZona = z.idZona where z.idArea = "+idArea+
                    " and c.status = 0";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                cont++;
                llaveArea+="'"+rs.getString(1)+"',";


            }
            llaveArea=llaveArea.substring(0,llaveArea.length()-1);
            getNotificacion(cont);



        } catch (SQLException e) {

        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }
        actualizarProductoComandero(llaveArea);
    }

    public void consultarProductosVendedor(){
        String idTienda = getTienda();
        ModeloNumeroOrden mno = new ModeloNumeroOrden();

        String idArea=areaID;
        String llaveArea="";
        int cont=0;
        try {
            String tienda = ultimaVez();
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "select cd.llave from comanderoDet cd inner join comandero c on c.numero = cd.numero  where c.empleado  = '"+mu.getNombre()+"' and cd.status = 1";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                cont++;
                llaveArea+="'"+rs.getString(1)+"',";


            }
            llaveArea=llaveArea.substring(0,llaveArea.length()-1);
            getNotificacionVendedor(cont);



        } catch (SQLException e) {

        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }
        actualizarProductoVendedor(llaveArea);
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



    public void getNotificacion(int cont) {
        notification("Nuevos pedidos", "Tienes  "+cont+" Productos por surtir ", getApplicationContext());
    }

    public void getNotificacionVendedor(int cont) {
        notification("Productos Surtidos", "Tienes  "+cont+" Productos surtidos ", getApplicationContext());
    }
    public void notification(String title, String message, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = createID();
        String channelId = "channel-id";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logotipo)
                .setContentTitle(title)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentText(message)
                .setVibrate(new long[]{100, 250})
                .setLights(Color.YELLOW, 500, 5000)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary));


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        Principal.location = 1;
        stackBuilder.addNextIntent(new Intent(context, menu.class));
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());

    }

    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.FRENCH).format(now));
        return id;

    }

    public void actualizarProductoComandero(String llaveArea){
       // String idTienda = getTienda();
       // ModeloNumeroOrden mno = new ModeloNumeroOrden();
       // String area = areaID;
        //String idArea=consultarIDArea();

        try {
            String tienda = ultimaVez();
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "update comanderoDet set [status] = 6 where llave in ("+llaveArea+")";
            st.executeQuery(sql);


        } catch (SQLException e) {

        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }

    }

    public void actualizarProductoVendedor(String llaveArea){
        // String idTienda = getTienda();
        // ModeloNumeroOrden mno = new ModeloNumeroOrden();
        // String area = areaID;
        //String idArea=consultarIDArea();

        try {
            String tienda = ultimaVez();
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "update comanderoDet set [status] = 7 where llave in ("+llaveArea+")";
            st.executeQuery(sql);


        } catch (SQLException e) {

        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }

    }
    public String  consultarIDArea() {
        String idTienda = getTienda();
        ModeloNumeroOrden mno = new ModeloNumeroOrden();
        String area = areaID;
        String idArea = "";

        try {
            String tienda = ultimaVez();
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "Select idarea from areasdecontrol where nombre='" + area + "' and idTienda=" + idTienda;
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

    public String getIdEmpresa(){
        String id="";
        try{
            Statement st= conex.conexionBD().createStatement();

            String sql="select idEmpresa from logins where Empresa='"+me.getEmpresa()+"'";
            ResultSet rs=st.executeQuery(sql);

            while(rs.next()){
                id=rs.getString(1);
            }

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

        }catch (Exception e ){
            e.getMessage();
            Toast.makeText(getApplicationContext(), "No sé puede actualizar el dispositivo", Toast.LENGTH_SHORT).show();
        }

        if (!dispositivo.isEmpty()) {

            String predeterminada = dispositivo;

            //declaramos una palabra de entrada
            String entrada = marca+"-"+serie+"-"+modelo;

            //variable usada para verificar si las palabras son iguales
            String aux = "";

            //se verifica que ambas palabras tengan la misma longitud
            //si no es asi no se pueden comparar
            if (entrada!= null) {
                if (predeterminada.length() == entrada.length()) {

                    for (int i = 0; i < predeterminada.length(); i++) {

                        //verificamos si el primer caracter de predeterminada
                        //es igual al primero de entrada
                        if (predeterminada.charAt(i) == entrada.charAt(i)) {
                            //si es asi guardamos ese concatenamos el caracter a la variable aux
                            aux += predeterminada.charAt(i);
                        }
                    }

                    //al finalizar el bucle verificamos si la variable aux es
                    //igual a la predeterminada
                    if (aux.equals(predeterminada)) {

                        //no hay pedo

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
