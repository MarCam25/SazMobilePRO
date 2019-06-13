package com.example.saz.saz;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloNumeroOrden;
import com.example.saz.saz.Modelo.ModeloResumen;
import com.example.saz.saz.Modelo.ModeloUsuario;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.conexion.ConexionSqlServer;
import com.example.saz.saz.entidades.Comandero;
import com.example.saz.saz.utilidades.AdaptadorListaComan;
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


public class Coman extends Fragment {
    RecyclerView recyclerView;

    ArrayList<ModeloResumen> listaResumen;
    public static ModeloEmpresa me = new ModeloEmpresa();
    public static ConexionBDCliente bdc = new ConexionBDCliente();
    public static boolean notific = false;
    public static boolean vuelt = false;
    int cantidadRegistros;
    ConexionSqlServer conex=new ConexionSqlServer();
    ModeloUsuario mu=new ModeloUsuario();
    static Timer temporizadorComandero;
    private TimerTask tarea;
    private Handler handler = new Handler();
    Button act;
    Context context;
    String dispositivo;
    View root;
    int tipo=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_coman, container, false);
        context = root.getContext();


        Principal.hiloCantidadC = cantidadRegistros;


        ConsultarNuevosRegistros();
        Principal.hiloCantidadC = cantidadRegistros;
        listaResumen = new ArrayList<>();


        recyclerView = (RecyclerView) root.findViewById(R.id.recyId);

        act = (Button) root.findViewById(R.id.idAct);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        int area = getAreaActual();
        getArea();

        if (area == -1) {
            mostrarOrdenesTodo();
        } else if(tipo==2) {
            mostrarOrdenes();
        }else{

            Toast toast = Toast.makeText(getActivity(), "No tienes acceso al contenido de esta pantalla", Toast.LENGTH_LONG);
            TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
            x.setTextColor(Color.YELLOW); toast.show();
        }
        AdaptadorListaComan adapter = new AdaptadorListaComan(listaResumen);
        recyclerView.setAdapter(adapter);
        IniciarTemporizador();

        act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIcon(R.mipmap.ic_launcher);
                progressDialog.setMessage("Actualizando pedidos...");
                progressDialog.show();

                Intent lista = new Intent(context, menu.class);
                Principal.location = 1;
                context.startActivity(lista);
            }
        });

        return root;


    }


    public void mostrarOrdenes() {
        String  ubicacionEmpleado = getArea();
        String tienda = getTienda();
        try {
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = " select numero,estilo,color,talla,ubicacion,llave, marca, acabado from comanderoDet INNER JOIN UbicacionesProductos on  UbicacionesProductos.barcode=comanderoDet.barcode inner join ZonasDeSurtido on UbicacionesProductos.idZona=ZonasDeSurtido.idZona where [status]=6 and Tienda=" + tienda + " and idArea=" + ubicacionEmpleado + ";";
            ResultSet rs = st.executeQuery(sql);
            ModeloResumen mr = null;

            Comandero comandero = null;
            while (rs.next()) {
                mr = new ModeloResumen();


                mr.setId(rs.getString(1));
                mr.setEstilo(rs.getString(2));
                mr.setColor(rs.getString(3));
                mr.setPunto(rs.getString(4));
                mr.setUbicacion(rs.getString(5));
                mr.setLlave(rs.getString(6));
                mr.setMarca(rs.getString(7));
                mr.setAcabado(rs.getString(8));

                listaResumen.add(mr);


            }

        } catch (SQLException e) {
            Toast.makeText(context, "error en mostrar ordenes ", Toast.LENGTH_SHORT).show();
        }

    }

    public void mostrarOrdenesTodo() {


        String tienda = getTienda();
        try {
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = " select numero,estilo,color,talla,ubicacion,llave, marca, acabado from comanderoDet where [status]=0 and Tienda=" + tienda + " ;";
            ResultSet rs = st.executeQuery(sql);
            ModeloResumen mr = null;
            Comandero comandero = null;
            while (rs.next()) {
                mr = new ModeloResumen();


                mr.setId(rs.getString(1));
                mr.setEstilo(rs.getString(2));
                mr.setColor(rs.getString(3));
                mr.setPunto(rs.getString(4));
                mr.setUbicacion(rs.getString(5));
                mr.setLlave(rs.getString(6));
                mr.setMarca(rs.getString(7));
                mr.setAcabado(rs.getString(8));

                listaResumen.add(mr);


            }

        } catch (Exception e) {
            Toast.makeText(context, "error en mostrar ordenes ", Toast.LENGTH_SHORT).show();
        }
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

    private void IniciarTemporizador() {
        try {
            temporizadorComandero = new Timer();
            tarea = new TimerTask() {
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            if (ConsultaF.not == true) {

                            }

                            consultarProductos();




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

    public void mandarNotificacione() {
        String pares = null;
        ModeloNumeroOrden mno = new ModeloNumeroOrden();
        String IdTienda = getTienda();

        int res = cantidadRegistros - Principal.hiloCantidadC;
        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "select top " + res + " barcode from comanderodet inner join comanderoLog on comanderoLog.numero=comanderoDet.numero where status=0 and tienda=" + IdTienda + " order by hora desc ";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                // pares = rs.getString(1);
                if (cantidadRegistros > Principal.hiloCantidadC) {

                    String barcode = rs.getString(1);
                    obtenerAreaDelProducto(barcode);

                }


            }


        } catch (SQLException e) {


        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }

    }

    public String getIdArea(int id) {


        String nombre = null;
        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "select nombre from  AreasDeControl where id=" + id;
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {

                nombre = rs.getString(1);


            }

        } catch (SQLException e) {
            Toast.makeText(getActivity(), "  Error 123", Toast.LENGTH_LONG).show();
        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }
        return nombre;
    }

    public void obtenerAreaDelProducto(String barcode) {

        String idTienda = getTienda();
        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "select idZona from UbicacionesProductos where barcode='" + barcode + "' and  idtienda=" + idTienda + "";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {

                int area = rs.getInt(1);
                validarArea(area);

            }

        } catch (SQLException e) {
        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }
    }

    public String getIdUsuario() {
        String idUsuario = null;
        ModeloUsuario mu = new ModeloUsuario();
        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "Select numero from empleado where nombre='" + mu.getNombre() + "'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                idUsuario = (rs.getString(1));

            }

        } catch (SQLException e) {
        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }
        return idUsuario;
    }

    public int getAreaActual() {
        int area = 0;
        String idEmpleado = getIdUsuario();

        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "select top 1 idArea from AreasAsignadas where idEmpleado=" + idEmpleado + " and  fecha=CONVERT(nCHAR(8), getDate() , 112) order by hora desc ";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                area = (rs.getInt(1));

            }

        } catch (SQLException e) {
        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }
        return area;
    }

    public String getNombreArea(String id) {
        String nombre = null;
        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "select nombre from AreasDeControl where id=" + id;
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                nombre = (rs.getString(1));

            }

        } catch (SQLException e) {
        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }
        return nombre;
    }

    public void validarArea(int area) {
        int areaA = getAreaActual();
        int zonas = 0;
        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "select * from ZonasDeSurtido where idArea=" + areaA;
            ResultSet rs = st.executeQuery(sql);
            if (areaA == -1) {
              //  getNotificacion();
            }

            while (rs.next()) {
                zonas = (rs.getInt(1));
                if (zonas == area) {
                  //  getNotificacion();
                }


            }

        } catch (SQLException e) {
            Toast.makeText(getActivity(), "Error al Validar area", Toast.LENGTH_LONG).show();
        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }


    }


    public void getNotificacion(int cont) {
        notification("Nuevos pedidos", "Tienes  "+cont+" Productos por surtir ", context);
    }


    public String ultimaVez() {

        String numeroT = null;
        try {
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
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


    public String getArea() {

        String area = null;
        try {
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
            SQLiteDatabase db = conn.getReadableDatabase();

            String sql = "SELECT area,tipo FROM " + Utilidades.TABLA_AREA;
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                area = (cursor.getString(0));
                tipo=cursor.getInt(1);
            }
        } catch (RuntimeException r) {

        }
        return area;
    }

    public void ConsultarNuevosRegistros() {
        String idTienda = getTienda();
        ModeloNumeroOrden mno = new ModeloNumeroOrden();
        try {
            String tienda = ultimaVez();
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "SELECT count(comanderodet.estilo) from comandero inner join comanderoLog on comanderoLog.numero=comandero.numero inner join comanderoDet on comanderoDet.numero=comandero.numero  where comanderoDet.status=0 and comanderoDet.tienda=" + idTienda + " ";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                cantidadRegistros = rs.getInt(1);

            }


        } catch (SQLException e) {

        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }
    }

    public String getTienda() {
        String tienda = "";
        try {
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getActivity(), "db tienda", null, 1);
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

    public void consultarProductos() {
        String idTienda = getTienda();
        ModeloNumeroOrden mno = new ModeloNumeroOrden();

        String idArea=getArea();
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
        actualizarProducto(llaveArea);
    }

    public void actualizarProducto(String llaveArea){
        String idTienda = getTienda();
        ModeloNumeroOrden mno = new ModeloNumeroOrden();
        String area = getArea();
        String idArea=consultarIDArea();

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


    public String  consultarIDArea() {
        String idTienda = getTienda();
        ModeloNumeroOrden mno = new ModeloNumeroOrden();
        String area = getArea();
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



}
