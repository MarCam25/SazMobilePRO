package com.example.saz.saz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Color;

import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.KeyEvent;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.saz.saz.ConsultaF.spCorrida;

public class AltaDeProducto extends AppCompatActivity {



    public static boolean not=false;
    public static boolean vuelta=false;
    GridView gridview;


    String idImagen;
    static Context context;
    String ubica;


    static int colorCantidad=-1, acabadoCantidad=0, marcaCantidad=0, corridaCantidad=0,puntosCantidad=0;

    Button btnDetalle, btnMas, btnMenos, btnAgregar,btnResumen,btnFinalizar;
    static Double puntoBar;
    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();
    ConexionSqlServer conex=new ConexionSqlServer();

    public static int up=0;
    public static String VarEstilo;
    static String  BarCodeFIN;
    static  String estiloBar="";
    static int scu=0;

    static String colorBar, acabadoBar, marcaBar, corridaBar;


    public static ArrayList lista=new ArrayList();
    ArrayList listaAcabado=new ArrayList();
    static String CodigoBar;

    ArrayList listaMarca=new ArrayList();
    ArrayList listaCorrida=new ArrayList();
    ArrayList listaZon=new ArrayList();
    static ArrayList puntos=new ArrayList();
    String numeroUsuario;
    TextView idPedido, barco;
    boolean xcaner=false;
    ModeloTienda mt=new ModeloTienda();


    long time =System.currentTimeMillis();
    String barcode;


    TextView precioTXT;
    int index=0;
    Button btnBardoce;


    static String sku;

    public static double  contenedor;

    String point;
    private Spinner sp,sp3, sp4, sp5, punto,spZonas;

    public static TextView existenciasTXT, cantidadTXT, unidadesTXT, importeTXT,descuentoTXT,totalTXT;

    public static EditText sp2;
    static TextView clienteTXT;

    double r;
    static String in, finn, inc;
    String estilo, color, acabado, marcas, linea, sublinea, temporada, descripcion, observaciones;
    TextView pagina, basico, comprador, departamento, tacon, plantilla, forro, clasificacion, corrida, suela, ubicacion, ubicaTXT;


    TextView encabezado;

    public static String listado;

    RecyclerView recycler;


    static String idFecha;


    ModeloUsuario mu=new ModeloUsuario();
    ModeloDatos md=new ModeloDatos();
    String NombreUsuario;



    static  String idColor=null,idAcabado=null,idMarca=null,idCorrida=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_de_producto);






        listado=mt.getNombreTienda();



        getUser();


        clienteTXT=(TextView)findViewById(R.id.clienteTXT);
        sp2=(EditText)findViewById(R.id.sp2);

        sp =(Spinner) findViewById(R.id.sp);
        sp3=(Spinner)findViewById(R.id.sp3);
        sp4=(Spinner)findViewById(R.id.sp4);
        sp5=(Spinner)findViewById(R.id.sp5);
        spZonas=(Spinner)findViewById(R.id.spZonaP);
        punto=(Spinner)findViewById(R.id.punto);
        precioTXT=(TextView)findViewById(R.id.precioTXT);
        cantidadTXT=(TextView)findViewById(R.id.cantidadTXT);
        importeTXT=(TextView)findViewById(R.id.importeTXT);
        unidadesTXT=(TextView)findViewById(R.id.unidadesTXT);
        descuentoTXT=(TextView)findViewById(R.id.descuentoTXT);
        totalTXT=(TextView)findViewById(R.id.totalTXT);
        barco=(TextView)findViewById(R.id.idBarco);


        btnMas=(Button)findViewById(R.id.btnMas);
        btnAgregar=(Button)findViewById(R.id.btnAgregar);
        btnMenos=(Button)findViewById(R.id.btnMenos);
        btnResumen=(Button)findViewById(R.id.btnResumen);
        btnFinalizar=(Button)findViewById(R.id.btnFinalizar);
        btnBardoce=(Button)findViewById(R.id.btnBarCode);

        idPedido=(TextView)findViewById(R.id.idPedido);


        btnDetalle=(Button)findViewById(R.id.btnDetalle);
        existenciasTXT=(TextView)findViewById(R.id.existenciasTXT);
        ubicaTXT=(TextView)findViewById(R.id.txtUbica);
        FormatoFecha();







        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = new Toast(getApplication());
                pl.droidsonroids.gif.GifImageView view = new pl.droidsonroids.gif.GifImageView(getApplicationContext());
                view.setImageResource(R.drawable.loading);
                toast.setView(view);
                toast.show();


                UbicarProducto();

                sp2.setText(null);
                spZonas.setAdapter(null);





                }

        });


        btnBardoce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Principal.escan=2;
                xcaner=true;
                startActivity(new Intent(getApplicationContext(),Scann.class));


            }
        });//termino del evento

        sp2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {



                limpiarListas();

                ReinicarContadores();

                Toast gif =new  Toast(getApplicationContext());
                pl.droidsonroids.gif.GifImageView view=new  pl.droidsonroids.gif.GifImageView(getApplicationContext());
                view.setImageResource(R.drawable.loading);
                gif.setView(view);
                gif.show();

                Toast toaste =new  Toast(getApplicationContext());
                pl.droidsonroids.gif.GifImageView viewe=new  pl.droidsonroids.gif.GifImageView(getApplicationContext());
                view.setImageResource(R.drawable.loading);
                toaste.setView(view);
                toaste.show();

                Toast toast = Toast.makeText(getApplicationContext(), "Cargando...", Toast.LENGTH_LONG);
                TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                x.setTextColor(Color.YELLOW); toast.show();

                puntos.clear();
                // mismoDispositivo();




                contarSp();
                if(colorCantidad>0) {
                    if (Principal.scannPass == true) {
                        llenarSp();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, lista);
                        sp.setAdapter(adapter);
                        sp.setSelection(obtenerPosicionItem(sp, colorBar));
                    } else if (Principal.scannPass == false && colorCantidad == 1) {
                        llenarSp();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, lista);
                        sp.setAdapter(adapter);


                    } else if (Principal.scannPass == false && colorCantidad > 1) {
                        llenarSp();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, lista);
                        sp.setAdapter(adapter);

                    } else if (Principal.scannPass == false) {
                        llenarSp();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, lista);
                        sp.setAdapter(adapter);

                    }
                }

                return false;
            }
        });


        sp2.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lista.clear();
                sp.setAdapter(null);

                listaAcabado.clear();
                sp3.setAdapter(null);

                listaMarca.clear();
                sp4.setAdapter(null);

                listaCorrida.clear();
                sp5.setAdapter(null);

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp3.setAdapter(null);
                listaAcabado.clear();
                llenarSp3();

                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, listaAcabado);
                sp3.setAdapter(adapter3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                listaMarca.clear();
                listaCorrida.clear();
                puntos.clear();

                sp4.setAdapter(null);
                sp4.setAdapter(null);

                ReinicarContadores();

                contarSp4();
                if(Principal.scannPass==true){
                    llenarSp4();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, listaMarca);
                    sp4.setAdapter(adapter);
                    sp4.setSelection(obtenerPosicionItem(sp4,marcaBar));
                }else if(Principal.scannPass==false && marcaCantidad==1){
                    llenarSp4();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, listaMarca);
                    sp4.setAdapter(adapter);


                }else if(Principal.scannPass==false && marcaCantidad>1){
                    llenarSp4();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, listaMarca);
                    sp4.setAdapter(adapter);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast toast = Toast.makeText(getApplicationContext(), "Cargando...", Toast.LENGTH_LONG);
                TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                x.setTextColor(Color.YELLOW); toast.show();



                listaCorrida.clear();
                puntos.clear();
                limpiarCajas();
                ReinicarContadores();
                sp5.setAdapter(null);
                listaCorrida.clear();



                contarSp5();
                if(Principal.scannPass==true ){
                    llenarSp5();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, listaCorrida);
                    sp5.setAdapter(adapter);
                    sp5.setSelection(obtenerPosicionItem(sp5,corridaBar));
                }else if(Principal.scannPass==false && corridaCantidad==1 ){
                    llenarSp5();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, listaCorrida);
                    sp5.setAdapter(adapter);


                }else if(Principal.scannPass==false && corridaCantidad>1 ){
                    llenarSp5();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, listaCorrida);
                    sp5.setAdapter(adapter);

                }else if(Principal.scannPass==false ){
                    llenarSp5();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, listaCorrida);
                    sp5.setAdapter(adapter);

                }


            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                puntos.clear();
                limpiarCajas();
                ReinicarContadores();
                puntos.clear();
                //   llenarExistencias();

                    llenarTabla();
                    mostrarZonas();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item, listaZon);
                    spZonas.setAdapter(adapter);
                    ubicacion();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



public void mostrarZonas(){
    String tienda =ultimaVez();
    try {

        Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
        String sql="select ZonasDeSurtido.nombre from ZonasDeSurtido inner join AreasDeControl on ZonasDeSurtido.idArea=AreasDeControl.idArea where ZonasDeSurtido.idTienda="+listado+"";
        ResultSet rs=st.executeQuery(sql);
        while(rs.next()){

            listaZon.add(rs.getString(1));

        }


    } catch (Exception e) {
        Toast.makeText(getApplicationContext(), "Error al finalizar el pedido", Toast.LENGTH_SHORT).show();
    }

}




    public void UbicarProducto(){
        try {
            String idZona=getIdZona(spZonas.getSelectedItem().toString());
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql="if exists (select 1 from UbicacionesProductos where barcode = '"
                    +barco.getText().toString()+"' and idTienda = '"
                    +listado+"') begin update UbicacionesProductos set idZona = '"
                    +idZona+"' where barcode = '"+barco.getText().toString()+"' and idTienda = '"
                    +listado+"' end else begin INSERT INTO ubicacionesProductos(idTienda,idZona,barcode)VALUES ('"
                    +listado+"','"+idZona+"','"+barco.getText().toString()+"') end";

            st.executeUpdate(sql);
            updateUbica(idZona);
            Toast.makeText(getApplicationContext(), "Producto ha sido ubicado...!!!", Toast.LENGTH_LONG+100).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Uups...!!! Debes agregar un estilo para poder ubicarlo ", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateUbica(String idZona){

        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql="UPDATE articulo set ubica='"+spZonas.getSelectedItem().toString()+"' where barcode='"+barco.getText().toString()+"'";

            st.executeUpdate(sql);

            Toast.makeText(getApplicationContext(), "Producto ha sido ubicado...!!!", Toast.LENGTH_LONG+100).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Uups...!!! Debes agregar un estilo para poder ubicarlo ", Toast.LENGTH_SHORT).show();
        }
    }

    public String getIdZona(String nombreZona){
        String numeroZona=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select idZona from ZonasDeSurtido WHERE nombre='"+nombreZona+"'";
            ResultSet rs=st.executeQuery(sql);

            while(rs.next()){
                numeroZona=rs.getString(1);
            }

        } catch (Exception e) {

            Toast.makeText(this, "Error al obtener id de la zona", Toast.LENGTH_SHORT).show();
        }

        return numeroZona;
    }

    public String getUusario(){
        ModeloUsuario mu=new ModeloUsuario();

        String numero =ultimaVez();
        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql="select numero from empleado where nombre='"+mu.getNombre()+"'";
            ResultSet rs=st.executeQuery(sql);

            while(rs.next()){
                numero=rs.getString(1);
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al finalizar el pedido", Toast.LENGTH_SHORT).show();
        }

        return numero;
    }


    public static void cargarDatos(String var){
        scu=0;
        BarCodeFIN="";
        estiloBar="";
        VarEstilo="";

        sku=null;
        VarEstilo=var;
        buscarEnSku();
        validarSku();
        if(scu==0) {
            getBarcode();
        }
        getEstilo();
        if(estiloBar.equals("")){
            Toast.makeText(context, "Codigo de barras no existente en el sistema ", Toast.LENGTH_LONG).show();
        }
        sp2.setText(estiloBar);
        cargarDatosBarcode();

        llenarSp();
    }
    public void limpiarCajas(){


    }

    public static void cargarDatosBarcode(){

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select ac.acabado, co.color, ma.MARCA ,cor.Nombre from articulo a inner join acabados ac  on a.ACABADO=ac.numero inner join colores co on a.COLOR=co.numero inner join marcas ma on a.MARCA=ma.NUMERO inner join corridas cor on a.corrida=cor.id where a.barcode='"+BarCodeFIN+"';";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                acabadoBar=rs.getString(1);
                colorBar=rs.getString(2);
                marcaBar=rs.getString(3);
                corridaBar=rs.getString(4);
            }

            //
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public  static void llenarSp() {
        String add="";
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select DISTINCT c.color, numero from colores c inner join articulo a on c.numero = a.color where a.estilo = '"+sp2.getText()+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {

                add=rs.getString(1);
                colorCantidad++;
                lista.add(add);

            }

            //
        } catch (Exception e) {

        }

    }

    public  static void contarSp() {
        String add="";
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select DISTINCT c.color, numero from colores c inner join articulo a on c.numero = a.color where a.estilo = '"+sp2.getText()+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {

                add=rs.getString(1);
                colorCantidad++;


            }

            //
        } catch (Exception e) {

        }

    }
    public static String getColor(String color){

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="SELECT numero FROM colores WHERE color='"+color+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {

                idColor=rs.getString(1);



            }


        } catch (Exception e) {

        }

        return idColor;
    }


    public void llenarSp3() {
        idColor=getColor(sp.getSelectedItem().toString());
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select DISTINCT ac.Acabado, numero from acabados ac inner join articulo a on ac.numero = a.acabado where a.estilo = '"+sp2.getText()+"' and a.Color = "+idColor+"";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                acabadoCantidad++;
                listaAcabado.add(rs.getString(1));

            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en sp3", Toast.LENGTH_SHORT).show();
        }

    }

    public String getAcabado(String acabado){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="SELECT numero FROM acabados WHERE acabado='"+acabado+"'";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                idAcabado=rs.getString(1);

            }


        } catch (Exception e) {

        }
        return idAcabado;
    }

    public void llenarSp4() {


        idAcabado=getAcabado(sp3.getSelectedItem().toString());
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select DISTINCT  m.marca, numero from marcas m inner join articulo a on m.numero = a.marca where a.estilo = '"+sp2.getText()+"' and a.Color = "+idColor+" and a.acabado = "+idAcabado+"";
            ResultSet rs = st.executeQuery(sql);


            //cislo
            while (rs.next()) {
                marcaCantidad++;
                listaMarca.add(rs.getString(1));
                sp4.setId(rs.getInt(2));
            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en sp4", Toast.LENGTH_SHORT).show();
        }

    }

    public void contarSp4() {


        idAcabado=getAcabado(sp3.getSelectedItem().toString());
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select DISTINCT  m.marca, numero from marcas m inner join articulo a on m.numero = a.marca where a.estilo = '"+sp2.getText()+"' and a.Color = "+idColor+" and a.acabado = "+idAcabado+"";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                marcaCantidad++;
                String l=(rs.getString(1));

            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en sp4", Toast.LENGTH_SHORT).show();
        }

    }

    public String getMarca(String marca){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="SELECT numero FROM marcas WHERE marca='"+marca+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {

                idMarca=rs.getString(1);



            }


        } catch (Exception e) {

        }
        return idMarca;
    }


    public void llenarSp5() {
        idMarca=getMarca(sp4.getSelectedItem().toString());
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select  co.Nombre as Corrida,  co.id from corridas co inner join articulo a on co.id = a.corrida where a.estilo = '"+sp2.getText()+"' and a.Color = "+idColor+" and a.acabado = "+idAcabado+" and a.marca =" +idMarca+"";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                corridaCantidad++;
                listaCorrida.add(rs.getString(1));
                sp5.setId(rs.getInt(2));
            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en sp5", Toast.LENGTH_SHORT).show();
        }
    }

    public void contarSp5() {
        idMarca=getMarca(sp4.getSelectedItem().toString());
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select  co.Nombre as Corrida,  co.id from corridas co inner join articulo a on co.id = a.corrida where a.estilo = '"+sp2.getText()+"' and a.Color = "+idColor+" and a.acabado = "+idAcabado+" and a.marca =" +idMarca+"";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                corridaCantidad++;
                String f=(rs.getString(2));
            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en sp5", Toast.LENGTH_SHORT).show();
        }
    }


    public String getIdCorrida(){
        String idCorrida=null;
        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql=" select corridas.id from corridas  inner join articulo on  corridas.id=articulo.corrida  where estilo='"+sp2.getText()+"' and color="+idColor+" and acabado="+idAcabado+" and marca="+idMarca;
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                idCorrida=rs.getString(1);

            }


        } catch (Exception e) {

        }
        return  idCorrida;
    }


    public void llenarTabla(){
        String idCorrida=getIdCorrida();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();

            String sql="select a.BARCODE,co.inicial,co.final,co.incremento,c.color,ma.marca,ac.acabado,im.id,a.UBICA\n" +
                    " from articulo a inner join lineas l on a.LINEA=l.NUMERO inner join sublinea sl on a.SUBLINEA=sl.NUMERO inner join temporad t on a.TEMPORAD=t.NUMERO\n" +
                    "  inner join proveed p on a.PROVEED=p.numero\n" +
                    "  left join empleado e on a.comprador=e.numero inner join departamentos d on a.DEPARTAMENTO=d.NUMERO\n" +
                    "  inner join tacones ta on a.TACON=ta.NUMERO inner join plantillas pl on a.PLANTILLA=pl.NUMERO inner join forros f on a.FORRO=f.NUMERO \n" +
                    "  inner join corridas co on a.corrida=co.id inner join suelas su on a.SUELA=su.numero inner join colores c on a.color = c.numero\n" +
                    "  inner join acabados ac on a.ACABADO=ac.NUMERO inner join marcas ma on a.MARCA=ma.NUMERO left join imagenes im on a.id=im.id where a.estilo = '"+sp2.getText()+"' and a.Color ="+idColor+" and a.acabado = "+idAcabado+" and a.marca = "+idMarca+" and a.corrida="+idCorrida;
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {


                barco.setText(barcode=rs.getString(1));
                in=rs.getString(2);
                finn=rs.getString(3);
                inc=rs.getString(4);
                color=rs.getString(5);
                marcas=rs.getString(6);
                acabado=rs.getString(7);
                idImagen=rs.getString(8);
                ubica=rs.getString(9);



            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en llenar Tabla", Toast.LENGTH_SHORT).show();
        }

    }






    public void FormatoFecha(){
        String au=null;
        int cont=0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss", Locale.getDefault());

        Date date=new Date();
        String aux = dateFormat.format(date);
        String[] fecha=aux.split("");
        for(int i=1;i<fecha.length;i++){
            int auxiliar=Integer.parseInt(fecha[i]);
            int  x=0;
            for( x=0;x<10;x++){

                if(x==auxiliar){

                    au+="-"+x;


                }

            }
        }
        String[] a=au.split("-");
        idFecha=a[1]+a[2]+a[3]+a[4]+a[5]+a[6]+a[7]+a[8]+a[9]+a[10]+a[11]+a[12]+a[13]+a[14];

    }


    public void getUser(){
        try {


            ModeloNumeroOrden mno=new ModeloNumeroOrden();
            mno.setUsuario(mu.getNombre());
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs=st.executeQuery("select numero, nombre from empleado where [user]='"+ mu.getNombre()+"'");

            while(rs.next()){
                numeroUsuario=rs.getString(1);
                NombreUsuario=(rs.getString( 2));
            }


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al optener usuario", Toast.LENGTH_SHORT).show();
        }
    }




    public static void buscarEnSku(){

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select saz from sku where sku ='"+VarEstilo+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                sku=rs.getString(1);

            }


        } catch (Exception e) {

        }
    }

    public static void validarSku(){
        String predeterminada = "SALIDA";

        //declaramos una palabra de entrada
        String   entrada = sku;

        //variable usada para verificar si las palabras son iguales
        String aux="";

        //se verifica que ambas palabras tengan la misma longitud
        //si no es asi no se pueden comparar
        if(sku!=null) {
            configurarBarcodeSku();
            consultarPuntoBar();
            scu=1;

        }else{

            configurarBarcode();

        }

    }

    public static void consultarPuntoBar(){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select  LEFT(saz,LEN(saz)-3) as barcode, cast(cast(RIGHT(saz,3)as decimal(4,1))/10 as decimal(4,1))  as talla from sku where sku = '"+VarEstilo+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                puntoBar=rs.getDouble(2);



            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (SQLException xe) {
            xe.getMessage();
            // Toast.makeText(getApplicationContext(), "Error obtener barcode", Toast.LENGTH_SHORT).show();
        }
    }

    public static void configurarBarcodeSku(){
        String aux=sku;
        String[] barcode=aux.split("");
        int tamaño=barcode.length;
        for(int i=0;i<tamaño-3;i++){
            BarCodeFIN+=barcode[i];
        }


      /*  String res=""+barcode[9]+barcode[10]+barcode[11];
        Double resint=Double.parseDouble(res);
        puntoBar=resint/10;
        CodigoBar=barcode[0]+barcode[1]+barcode[2]+barcode[3]+barcode[4]+barcode[5]+barcode[6]+barcode[7]+barcode[8];
      */


    }
    public static  void tallasBarcode(Double talla){

        try {

            double inicio, fin, medio;
            inicio = Double.valueOf(in);
            fin = Double.valueOf(finn);
            medio = Double.valueOf(inc);
            puntos.add(talla);

            for (double i = inicio; i < fin + medio; i = i + medio) {


                if(i!=talla){
                    puntos.add(i);
                }

            }
        }catch (Exception e){

        }
    }
    public static void configurarBarcode(){
        String aux=VarEstilo;
        String[] barcode=aux.split("");
        String res=""+barcode[9]+barcode[10]+barcode[11];
        Double resint=Double.parseDouble(res);
        puntoBar=resint/10;
        CodigoBar=barcode[0]+barcode[1]+barcode[2]+barcode[3]+barcode[4]+barcode[5]+barcode[6]+barcode[7]+barcode[8];


        //
    }

    public static void getBarcode(){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select p.barcode from precios p inner join articulo a on a.barcode = p.barcode where p.talla = "+puntoBar+" and a.id = "+CodigoBar);


            while (rs.next()) {
                BarCodeFIN=rs.getString(1);



            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (SQLException xe) {
            xe.getMessage();
            // Toast.makeText(getApplicationContext(), "Error obtener barcode", Toast.LENGTH_SHORT).show();
        }
    }

    public static void getEstilo(){
        scu=0;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select a.ESTILO from colores c inner join articulo a on c.numero = a.color where a.BARCODE='"+BarCodeFIN+"'");

            while (rs.next()) {
                estiloBar=rs.getString(1);

            }


        } catch (Exception e) {

        }

    }


    public void notification(String title, String message, Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = createID();
        String channelId = "channel-id";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_MAX;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.logotipo)//R.mipmap.ic_launcher
                .setContentTitle(title)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentText(message)
                .setVibrate(new long[]{100, 250})
                .setLights(Color.YELLOW, 500, 5000)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        Principal.location=2;
        stackBuilder.addNextIntent(new Intent(context, menu.class));

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

    public int createID() {
        Date now = new Date();
        int id = Integer.parseInt(new SimpleDateFormat("ddHHmmss", Locale.FRENCH).format(now));
        return id;
    }



    public String ultimaVez(){

        String numeroT=null;
        try {
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
            SQLiteDatabase db = conn.getReadableDatabase();

            String sql="SELECT nombreT FROM tienda where id=1";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                numeroT = (cursor.getString(0));
            }
        }catch (Exception e){

        }

        return numeroT;
    }




    public void limpiarListas(){
        listaCorrida.clear();
        listaMarca.clear();
        listaAcabado.clear();
        lista.clear();
    }

    public void limpiarCaja(){
        existenciasTXT.setText(null);
        totalTXT.setText(null);
        descuentoTXT.setText(null);
        precioTXT.setText(null);


    }

    public void ReinicarContadores(){
        colorCantidad=0;
        acabadoCantidad=0;
        marcaCantidad=0;
        corridaCantidad=0;

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

    public void ubicacion (){
        String mensaje="Producto no ubicado";
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select ubica from articulo where barcode='"+barcode+"'";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                mensaje=rs.getString(1);


            }

            ubicaTXT.setText(mensaje);
        } catch (Exception e) {

        }
    }
}
