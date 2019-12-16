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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saz.saz.Modelo.DatosLupita;
import com.example.saz.saz.Modelo.ModeloDatos;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloNumeroOrden;
import com.example.saz.saz.Modelo.ModeloUsuario;
import com.example.saz.saz.Modelo.Similar;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.conexion.ConexionSqlServer;
import com.example.saz.saz.utilidades.ModeloTienda;
import com.example.saz.saz.utilidades.Utilidades;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class Consulta_Marca extends Fragment {



    int cantidadRegistros;
    static Timer temporizadorPedido;
    private TimerTask tarea;
    private Handler handler = new Handler();
    static int scu=0;
    static String colorBar, acabadoBar, marcaBar, corridaBar;
    public static boolean not=false;
    public static boolean vuelta=false;

    String empress;
    public static String NombreTienda;
    String idImagen;
    static Context context;
    String ubica=null;
    int  buscador=0;
    Boolean sinUbica=false;

    static int colorCantidad=0, acabadoCantidad=0, marcaCantidad=0, corridaCantidad=0,puntosCantidad=0;

    String dispositivo;



    double descuentoFinal;
    double auxiliarDes;

    Button btnDetalle, btnMas, btnMenos, btnAgregar,btnResumen,btnFinalizar;
    static Double puntoBar;
    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();
    ConexionSqlServer conex=new ConexionSqlServer();
    int cantidad=0;
    public static int up=0;
    public static String VarEstilo;
    static String clint, BarCodeFIN="";
    static  String estiloBar="";
    String variableS,descuento;
    String where=" where ";

    String fechaUlti, horaUlti;

    public static ArrayList lista=new ArrayList();
    ArrayList listaAcabado=new ArrayList();
    static String CodigoBar="";
    ArrayList listaMarca=new ArrayList();
    ArrayList listaCorrida=new ArrayList();
    static ArrayList puntos=new ArrayList();
    String numeroUsuario;
    TextView idPedido;
    boolean xcaner=false;
    ModeloTienda mt=new ModeloTienda();


    String barcode;
    boolean detallePass=false;
    TextView precioTXT;
    Button btnBardoce;
    double tot;
    static String sku;
    String intentResummen;
    public static double pre = 0;
    public static int   existencias;
    String point;
    public static Spinner spColor,spAcabado, spMarca, spCorrida, punto;
    public static TextView existenciasTXT, cantidadTXT, unidadesTXT, importeTXT,descuentoTXT,totalTXT;

    public static EditText sp2;
    static TextView clienteTXT;
    double r;
    static String in, finn, inc;
    String estilo, color, acabado, marcas;

    double precio=0.0;

    public static String listado;

    static String idFecha;
    ModeloUsuario mu=new ModeloUsuario();
    ModeloDatos md=new ModeloDatos();
    String NombreUsuario;
    static DatosLupita dl=new DatosLupita();

    Button btnSimilar , btnOtras;

    public static  String idColor=null,idAcabado=null,idMarca=null,idCorrida=null;


    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        root=inflater.inflate(R.layout.fragment_consulta__marca, container, false);



        listado=mt.getNombreTienda();

        ToolBarNombre();


        getUser();

        clienteTXT=(TextView)root.findViewById(R.id.clienteTXT);
        sp2=(EditText)root.findViewById(R.id.sp2);

        spColor =(Spinner)root. findViewById(R.id.sp);
        spAcabado=(Spinner)root.findViewById(R.id.sp3);
        spMarca=(Spinner)root.findViewById(R.id.sp4);
        spCorrida=(Spinner)root.findViewById(R.id.sp5);
        punto=(Spinner)root.findViewById(R.id.punto);
        precioTXT=(TextView)root.findViewById(R.id.precioTXT);
        cantidadTXT=(TextView)root.findViewById(R.id.cantidadTXT);
        importeTXT=(TextView)root.findViewById(R.id.importeTXT);
        unidadesTXT=(TextView)root.findViewById(R.id.unidadesTXT);
        descuentoTXT=(TextView)root.findViewById(R.id.descuentoTXT);
        totalTXT=(TextView)root.findViewById(R.id.txtTotal);
        btnSimilar=(Button)root.findViewById(R.id.btnSimilares);
        btnOtras=(Button)root.findViewById(R.id.btnOtras);





        btnMas=(Button)root.findViewById(R.id.btnMas);
        btnAgregar=(Button)root.findViewById(R.id.btnAgregar);
        btnMenos=(Button)root.findViewById(R.id.btnMenos);
        btnResumen=(Button)root.findViewById(R.id.btnResumen);
        btnFinalizar=(Button)root.findViewById(R.id.btnFinalizar);
        btnBardoce=(Button)root.findViewById(R.id.btnBarCode);

        idPedido=(TextView)root.findViewById(R.id.idPedido);


        btnDetalle=(Button)root.findViewById(R.id.btnDetalle);
        existenciasTXT=(TextView)root.findViewById(R.id.existenciasTXT);

        fechaFormato();

        retomarPedidos();
        ConsultarNuevosRegistros();

        inicializarMarca();

        if(Principal.similarPass==true){
            cargarDatosBarcodeSimilar(dl.getBarcode());
            Principal.similarPass=false;
        }




        btnOtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(punto.getSelectedItem()=="") {
                    Toast.makeText(getActivity(),"Debes seleccionar estilo y punto",Toast.LENGTH_LONG).show();
                }else{
                    try {
                        dl.setBarcode(barcode);
                        dl.setPunto(punto.getSelectedItem().toString());
                        Intent tienda = new Intent(getActivity(), Tiendas.class);
                        startActivity(tienda);
                    }catch (Exception ex){
                        Toast.makeText(getActivity(),"Debes Ingresar un  estilo ",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });


        btnSimilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(punto.getSelectedItem()=="") {
                    Toast.makeText(getActivity(),"Debes seleccionar estilo y punto ",Toast.LENGTH_LONG).show();
                }else{
                    try {


                        dl.setBarcode(barcode);
                        dl.setPunto(punto.getSelectedItem().toString());

                        Intent similar=new Intent(getActivity(),Similares.class);
                        startActivity(similar);
                    }catch (Exception ex){
                        Toast.makeText(getActivity(),"Debes Ingresar un  estilo ",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });




        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int cantiTxt=Integer.parseInt(unidadesTXT.getText().toString());
                if(cantiTxt>0) {

                    OrdenEspera.pass = true;
                    insertarComandero();
                    crearOrden();
                    getDatosPedido();
                    limpiarCajas();
                    sp2.setText(null);
                    lista.clear();
                    spColor.setAdapter(null);

                    listaAcabado.clear();
                    spAcabado.setAdapter(null);

                    listaMarca.clear();
                    spMarca.setAdapter(null);

                    listaCorrida.clear();
                    spCorrida.setAdapter(null);

                    limpiarCajas();

                    puntos.clear();
                    punto.setAdapter(null);

                    cantidadTXT.setText(null);
                    precioTXT.setText(null);

                    clienteTXT.setText(null);
                    unidadesTXT.setText("0");
                    importeTXT.setText("0");
                    precioTXT.setText(null);
                    descuentoTXT.setText(null);
                    totalTXT.setText(null);
                    up = 0;
                    pre = 0;
                    intentResummen = null;
                    tot = 0;
                    idFecha = null;
                    idPedido.setText("");
                    Intent orden = new Intent(getActivity(), OrdenEspera.class);
                    startActivity(orden);
                }else{
                    Toast.makeText(getActivity(), "No tienes productos agregados.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnResumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Integer.parseInt(unidadesTXT.getText().toString())>0) {
                    Intent resumen = new Intent(getActivity(), Resumen.class);
                    resumen.putExtra("resumen", intentResummen);
                    startActivity(resumen);
                }else{
                    Toast.makeText(getActivity(), "No tienes productos agregados.", Toast.LENGTH_SHORT).show();
                }

            }
        });


        btnBardoce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Principal.escan=1;
                xcaner=true;
                startActivity(new Intent(getActivity(),Scann.class));


            }
        });




        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(sinUbica==false) {
                        int cantiTxt = Integer.parseInt(cantidadTXT.getText().toString());
                        if (cantiTxt > 0) {
                            clint = clienteTXT.getText().toString();
                            if (!clint.isEmpty()) {
                                getIdPedido();
                                generarImporte();
                                generarCantidad();
                                obtenerRegistrosSQLite();
                                Finalizare();
                                cantidadTXT.setText("0");
                                Toast.makeText(getActivity(), "Producto agregado", Toast.LENGTH_LONG).show();
                            }
                            if (clint.isEmpty()) {
                                Toast.makeText(getActivity(), "Debes agregar un cliente...!!!", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getActivity(), "No hay Producto", Toast.LENGTH_SHORT).show();
                        }
                    } else if(sinUbica==true){
                        Toast.makeText(getActivity(), "Este producto no tiene ubicación por lo tanto no puede agregarlo", Toast.LENGTH_LONG).show();

                    }
                }catch(Exception e){
                    e.getMessage();
                    Toast.makeText(getActivity(),"hay un problema al agrgar los prodcutos ", Toast.LENGTH_LONG).show();
                }
            }

        });
        btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if(!sp2.getText().toString().equals("")) {
                        if (existencias > 0) {
                            existencias--;
                            existenciasTXT.setText(String.valueOf(existencias));
                            cantidad++;
                            String res = String.valueOf(cantidad);
                            cantidadTXT.setText(res);
                        }

                    }

                }catch(Exception e){

                }

            }
        });

        btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if(!sp2.getText().toString().equals("")) {
                        if (cantidad > 0) {
                            existencias++;
                            existenciasTXT.setText(String.valueOf(existencias));
                            cantidad--;
                            String res = String.valueOf(cantidad);
                            cantidadTXT.setText(res);

                        }
                    }
                }catch(Exception e){

                }
            }
        });



        btnDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(detallePass==true){

                        Intent detalle = new Intent(getActivity(), Detalle.class);
                        detalle.putExtra("valores", sp2.getText() + "-" + idColor + "-" + idAcabado + "-" + idMarca + "-" + empress);
                        startActivity(detalle);
                    }else{
                        Toast toast = Toast.makeText(getActivity(), "Debes ingresar un estilo", Toast.LENGTH_LONG);
                        TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                        x.setTextColor(Color.YELLOW); toast.show();
                    }

                }catch (Exception e){

                }
            }
        });

        punto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String validador="";
                validador=spCorrida.getSelectedItem().toString();
                if(validador.equals(" ")) {

                    puntos.clear();
                    punto.setAdapter(null);


                    Toast.makeText(getActivity(), "Debes seleccionar una corrida", Toast.LENGTH_SHORT).show();
                }else{
                    cantidadTXT.setText("0");
                    cantidad = 0;
                    Principal.scannPass = false;
                    consultarCantidadReal();
                    traerPrecio();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        sp2.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                lista.clear();
                spColor.setAdapter(null);

                listaAcabado.clear();
                spAcabado.setAdapter(null);



                listaCorrida.clear();
                spCorrida.setAdapter(null);

                limpiarCajas();

                puntos.clear();
                punto.setAdapter(null);

                cantidadTXT.setText(null);
                precioTXT.setText(null);

                detallePass=false;
                ubica=null;
                sinUbica=false;
                idColor=null;

                idAcabado=null;
                idCorrida=null;


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });







        sp2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

                    try {

                        buscador = 0;
                        detallePass = true;
                        buscador();


                        limpiarListas();
                        limpiarCajas();
                        ReinicarContadores();

                        Toast.makeText(getActivity(), "Cargando datos...", Toast.LENGTH_LONG).show();

                        puntos.clear();
                        mismoDispositivo();
                        punto.setAdapter(null);

                        contarSp();
                        if (colorCantidad > 0) {
                            if (Principal.scannPass == true) {
                                llenarSp();
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, lista);
                                spColor.setAdapter(adapter);
                                spColor.setSelection(obtenerPosicionItem(spColor, colorBar));
                            } else if (Principal.scannPass == false && colorCantidad == 1 && buscador == 0) {
                                llenarSp();
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, lista);
                                spColor.setAdapter(adapter);
                                spColor.setSelection(1);


                            } else if (Principal.scannPass == false && colorCantidad > 1 && buscador == 0) {

                                llenarSp();
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, lista);
                                spColor.setAdapter(adapter);

                            } else if (buscador == 1 && Principal.scannPass == false) {
                                buscarMarcas();

                                if (marcaCantidad == 1) {


                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaMarca);

                                    spMarca.setAdapter(adapter);
                                    spMarca.setSelection(1);


                                } else if (marcaCantidad > 1) {

                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaMarca);
                                    spMarca.setAdapter(adapter);



                                }

                            } else if (Principal.scannPass == false && buscador == 0) {
                                llenarSp();
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, lista);
                                spColor.setAdapter(adapter);

                            }
                        } else {


                        }

                    }catch (Exception e){
                        e.getMessage();
                        Toast.makeText(getActivity(), "Ocurrio un error 638", Toast.LENGTH_LONG).show();

                    }

                    return true;
                }
                return false;
            }
        });





        spColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try{

                    String validador="";
                    validador=spColor.getSelectedItem().toString();
                    if(validador.equals(" ")) {

                        puntos.clear();
                        punto.setAdapter(null);
                        listaCorrida.clear();
                        spCorrida.setAdapter(null);
                        listaMarca.clear();

                        listaAcabado.clear();
                        spAcabado.setAdapter(null);
                        idAcabado="";
                        idColor="";
                        idCorrida="";
                        idMarca="";



                    }else {


                        listaAcabado.clear();
                        listaMarca.clear();
                        listaCorrida.clear();
                        puntos.clear();
                        spAcabado.setAdapter(null);

                        spCorrida.setAdapter(null);
                        punto.setAdapter(null);
                        precioTXT.setText(null);
                        limpiarCajas();
                        ReinicarContadores();
                        spAcabado.setAdapter(null);
                        listaAcabado.clear();
                        contarSp3();

                        if (Principal.scannPass == true) {
                            llenarSp3();
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaAcabado);
                            spAcabado.setAdapter(adapter);
                            spAcabado.setSelection(obtenerPosicionItem(spAcabado, acabadoBar));

                        } else if (Principal.scannPass == false && acabadoCantidad == 1) {
                            llenarSp3();
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaAcabado);
                            spAcabado.setAdapter(adapter);
                            spAcabado.setSelection(1);
                        } else if (Principal.scannPass == false && acabadoCantidad > 1) {
                            llenarSp3();
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaAcabado);
                            spAcabado.setAdapter(adapter);
                        }
                    }
                }catch (Exception e){
                    e.getMessage();
                    Toast.makeText(getActivity(), "Ocurrio un error 703", Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAcabado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {


                    puntos.clear();
                    punto.setAdapter(null);
                    listaCorrida.clear();
                    spCorrida.setAdapter(null);


                    spCorrida.setAdapter(null);
                    punto.setAdapter(null);
                    listaCorrida.clear();
                    puntos.clear();
                    limpiarCajas();
                    ReinicarContadores();
                    spCorrida.setAdapter(null);
                    listaCorrida.clear();


                    contarSp5();
                    if (Principal.scannPass == true && buscador == 0) {
                        llenarSp5();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaCorrida);
                        spCorrida.setAdapter(adapter);
                        spCorrida.setSelection(obtenerPosicionItem(spCorrida, corridaBar));
                    } else if (Principal.scannPass == false && corridaCantidad == 1 && buscador == 0) {
                        llenarSp5();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaCorrida);
                        spCorrida.setAdapter(adapter);
                        spCorrida.setSelection(1);

                    } else if (Principal.scannPass == false && corridaCantidad > 1 && buscador == 0) {
                        llenarSp5();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaCorrida);
                        spCorrida.setAdapter(adapter);

                    } else if (buscador == 1 && Principal.scannPass == false) {
                        llenarSp5();
                        obtenerCorrida();
                        if (corridaCantidad == 1) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaCorrida);
                            spCorrida.setAdapter(adapter);
                            spCorrida.setSelection(1);
                        } else if (corridaCantidad > 1) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaCorrida);
                            spCorrida.setAdapter(adapter);
                        }

                    } else if (Principal.scannPass == false && buscador == 0) {
                        llenarSp5();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaCorrida);
                        spCorrida.setAdapter(adapter);

                    } else if (buscador == 1 && Principal.scannPass == true) {
                        llenarSp5();
                        obtenerCorrida();

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaCorrida);
                        spCorrida.setAdapter(adapter);
                        spCorrida.setSelection(obtenerPosicionItem(spCorrida, corridaBar));


                    }

                } catch (Exception e) {
                    e.getMessage();
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spCorrida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {


                    punto.setAdapter(null);
                    puntos.clear();
                    limpiarCajas();
                    ReinicarContadores();
                    puntos.clear();
                    punto.setAdapter(null);

                    traerDatosProducto();
                    llenarTabla();
                    contarPuntos();
                    if (Principal.scannPass == true && buscador == 0) {
                        llenarTabla();
                        llenarPuntos();

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_punto, puntos);
                        punto.setAdapter(adapter);
                        punto.setSelection(obtenerPosicionItem(punto, String.valueOf(puntoBar)));
                        Principal.scannPass = false;
                    } else if (Principal.scannPass == false && puntosCantidad == 1 && buscador == 0) {
                        llenarTabla();
                        llenarPuntos();

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_punto, puntos);
                        punto.setAdapter(adapter);


                    } else if (Principal.scannPass == false && puntosCantidad > 1 && buscador == 0) {
                        llenarTabla();
                        llenarPuntos();

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_punto, puntos);
                        punto.setAdapter(adapter);

                    } else if (buscador == 1 && Principal.scannPass == false) {
                        traerDatosProducto();
                        llenarPuntos();
                        if (puntosCantidad == 1) {
                            llenarTabla();

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_punto, puntos);
                            punto.setAdapter(adapter);


                        } else if (puntosCantidad > 1) {

                            llenarTabla();
                            llenarPuntos();

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_punto, puntos);
                            punto.setAdapter(adapter);


                        }


                    } else if (Principal.scannPass == false && buscador == 0) {
                        llenarTabla();
                        llenarPuntos();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_punto, puntos);
                        punto.setAdapter(adapter);

                    } else if (buscador == 1 && Principal.scannPass == true) {
                        Similar simi = new Similar();
                        llenarTabla();
                        traerDatosProducto();

                        llenarPuntos();

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item_punto, puntos);
                        punto.setAdapter(adapter);
                        punto.setSelection(obtenerPosicionItem(punto, String.valueOf(puntoBar)));
                        Principal.scannPass = false;
                    }

                } catch (Exception e) {
                    e.getMessage();
                    Toast.makeText(getActivity(), "Ocurrio un error 937", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return root;
    }


    private void generarCantidad() {
        up += cantidad;
        retomarUnidades();
        cantidad = 0;
    }

    public void retomarUnidades(){
        int id=0;
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT SUM(cantidad) FROM " + Utilidades.TABLA_CONTENEDOR , null);
        while (cursor.moveToNext()) {

            int contador=cursor.getInt(0);
            unidadesTXT.setText(String.valueOf(contador+cantidad));

        }


    }

    private void generarImporte() {

        double Total;
        auxiliarDes=Double.parseDouble(descuento)/100;
        descuentoFinal = Double.parseDouble(variableS)*auxiliarDes;
        Total=Double.parseDouble(variableS)- descuentoFinal;
        precioTXT.setText("$"+variableS);
        descuentoTXT.setText("%"+descuento);
        totalTXT.setText("$"+String.valueOf(Total));


        double resultado=Total * cantidad;
        retomarImporte(resultado);
        pre += resultado;
        precio+=resultado;


    }

    private void  generarDescuento() {
        double Total;
        auxiliarDes=Double.parseDouble(descuento)/100;
        descuentoFinal = Double.parseDouble(variableS)*auxiliarDes;
        Total=Double.parseDouble(variableS)- descuentoFinal;
        totalTXT.setText("$"+String.valueOf(Total));

    }


    public void insertarPedido(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
        SQLiteDatabase db=conn.getWritableDatabase();


        if( ubica==null)
        {
            ubica="Producto sin ubicación";
        }

        db.execSQL("INSERT INTO  contenedor (estilo, imagen, talla, cantidad, marca, color, sub, total, barcode,acabado,corrida, cliente,ubicacion) VALUES('"+sp2.getText().toString()+"', '"+idImagen+"', '"+punto.getSelectedItem()+"', '"+cantidadTXT.getText().toString()+"','"+marcas+"','"+color+"','"+totalTXT.getText()+"','"+pre+"','"+barcode+"','"+acabado+"','"+spCorrida.getSelectedItem()+"','"+clienteTXT.getText()+"','"+ubica+"')");

        pre=0;


    }

    public void retomarImporte(double resultado){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT SUM(total) FROM " + Utilidades.TABLA_CONTENEDOR , null);
        while (cursor.moveToNext()) {

            double contador=cursor.getDouble(0);
            importeTXT.setText(String.valueOf(contador+resultado));






        }


    }

    public void insertarRegistroIgual(int id){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
        SQLiteDatabase db=conn.getWritableDatabase();
        db.execSQL("UPDATE contenedor set cantidad=cantidad + " + cantidadTXT.getText().toString()+", total=total +"+pre+ " WHERE id = "+id);
        pre=0;
    }

    public void obtenerRegistrosSQLite(){
        int id=0;
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();


        String sql = "SELECT id FROM contenedor WHERE estilo LIKE'%" +sp2.getText().toString()+ "%' and talla="+punto.getSelectedItem();

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {

            id=cursor.getInt(0);

        }

        if(id==0){
            insertarPedido();
        }else{
            insertarRegistroIgual(id);
        }
    }



    public void getDatosPedido()
    {

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_CONTENEDOR , null);
        while (cursor.moveToNext()) {


            String idCont=(cursor.getString(0));
            String estilo=(cursor.getString(1));
            String imagen=(cursor.getString(2));
            String punto=(cursor.getString(3));
            String cantidad=(cursor.getString(4));
            String marca=(cursor.getString(5));
            String color=(cursor.getString(6));
            String sub=(cursor.getString(7));
            String total=(cursor.getString(8));
            String bar=(cursor.getString(9));
            String aca=(cursor.getString(10));
            String corr=(cursor.getString(11));
            String ubica=(cursor.getString(13));

            insertarPedidosEnOrden(estilo,imagen,punto,cantidad,marca,color,sub,total,bar,aca,corr,ubica);

            deleteContenedor();

        }

    }


    public void retomarPedidos()
    {
        try {
            int id = 0;
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
            SQLiteDatabase db = conn.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT id,SUM(cantidad),SUM(total),cliente FROM " + Utilidades.TABLA_CONTENEDOR, null);
            while (cursor.moveToNext()) {
                id = cursor.getInt(0);
                if (id != 0) {
                    unidadesTXT.setText(cursor.getString(1));
                    importeTXT.setText("$" + cursor.getString(2));
                    clienteTXT.setText(cursor.getString(3));
                }

            }
            db.close();
            conn.close();
        }catch (Exception e){

        }

    }



    public void deleteContenedor(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        db.execSQL("DELETE FROM  " + Utilidades.TABLA_CONTENEDOR);
        db.close();
        conn.close();
    }



    public String sacarPares(){
        String pares="";
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(cantidad) FROM " + Utilidades.TABLA_CONTENEDOR , null);
        while (cursor.moveToNext()) {
            pares=(cursor.getString(0));
        }
        db.close();
        conn.close();
        return pares;
    }



    public String sacarTotal(){
        String total="";
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(total)FROM " + Utilidades.TABLA_CONTENEDOR , null);
        while (cursor.moveToNext()) {
            total=(cursor.getString(0));
        }
        conn.close();
        db.close();
        return total;
    }


    public void crearOrden(){
        String pares=sacarPares();
        String total=sacarTotal();
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
        SQLiteDatabase db=conn.getWritableDatabase();
        String sql="INSERT INTO  orden  (status, cliente, empleado, norden, total, pares, impreso) VALUES('"+0+"', '"+clienteTXT.getText().toString()+"', '"+consultarEmpleado()+"', '"+idFecha+"','"+total+"','"+pares+"','"+0+"')";
        db.execSQL(sql);
        db.close();
        conn.close();
    }


    public void insertarPedidosEnOrden(String estilo, String imagen, String talla, String cant, String marca, String color, String sub, String total, String barcode,String acabado,String corr,String ubica){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
        SQLiteDatabase db=conn.getWritableDatabase();
        int idOrden=getOrden();
        String sql="INSERT INTO  pedido (estilo, imagen, talla, cantidad, marca, color, sub, total, barcode,acabado,corrida,ubicacion,idOrden) VALUES('"+estilo+"', '"+imagen+"', '"+talla+"', '"+cant+"','"+marca+"','"+color+"','"+sub+"','"+total+"','"+barcode+"','"+acabado+"','"+corr+"','"+ubica+"',"+idOrden+")";
        db.execSQL(sql);
        db.close();
        conn.close();
    }


    public int getOrden(){
        int idOrden = 0;

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT idOrden FROM " + Utilidades.TABLA_ORDEN , null);
        while (cursor.moveToNext())
        {

            idOrden=cursor.getInt(0);

        }
        db.close();
        conn.close();
        return idOrden;

    }


    public void Finalizare(){
        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql="update existen set cantreal= cantreal + " +cantidadTXT.getText().toString() + " ,  pedido = pedido + " + cantidadTXT.getText().toString() + " where barcode ='" + barcode+ "' and talla = " + punto.getSelectedItem() + " and tienda = " + listado;
            st.executeUpdate(sql);
            st.close();




        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error al finalizar el pedido", Toast.LENGTH_SHORT).show();
        }

    }




    public static void cargarDatosBarcodeSimilar(String barcode){
        Principal.scannPass=true;
        Similar simi=new Similar();
        puntoBar=Double.parseDouble(simi.getPunto());
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select ac.acabado, co.color, ma.MARCA ,cor.Nombre from articulo a inner join acabados ac  on a.ACABADO=ac.numero inner join colores co on a.COLOR=co.numero inner join marcas ma on a.MARCA=ma.NUMERO inner join corridas cor on a.corrida=cor.id where a.barcode='"+barcode+"';";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                acabadoBar=rs.getString(1);
                colorBar=rs.getString(2);
                marcaBar=rs.getString(3);
                corridaBar=rs.getString(4);
            }
            st.close();

            String estilo=getEstiloSimilar(barcode);
            sp2.setText(estilo);
            llenarSp();

        } catch (SQLException e) {
            e.getMessage();
        }
    }



    public static String getEstiloSimilar(String barcode){
        scu=0;
        String estilo="";
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select a.ESTILO from colores c inner join articulo a on c.numero = a.color where a.BARCODE='"+barcode+"'");

            while (rs.next()) {
                estilo=rs.getString(1);

            }
            st.close();
            rs.close();


        } catch (SQLException e) {
            e.getMessage();
        }
        return estilo;
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
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }
    public int  consultarAPartados(String barcode, String punto){

        int apartado=0;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select SUM(cantidad) from apartados a inner join detap d on a.numero=d.numero where BARCODE='"+barcode+"' and d.punto="+punto+" and d.tienda="+ultimaVez();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                apartado=rs.getInt(1);
            }
            st.close();
            rs.close();

        } catch (Exception e) {

        }
        return apartado;
    }

    public void buscarEnMitienda(int real){


        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String puntoSp=punto.getSelectedItem().toString();
            ResultSet rs = st.executeQuery("lupitaApartados'"+barcode+"',"+puntoSp+","+listado+",''");
            ResultSetMetaData rsmd=rs.getMetaData();
            while(rs.next()) {

                existencias=(rs.getInt(2));
                existenciasTXT.setText(String.valueOf(existencias));
                btnMas.setEnabled(true);
                btnMenos.setEnabled(true);
            }
            st.close();
            rs.close();

        } catch (SQLException e1) {
            e1.printStackTrace();
            Toast.makeText(getActivity(),"No hay existencias " , Toast.LENGTH_LONG).show();
            btnMas.setEnabled(false);
            btnMenos.setEnabled(false);
            existenciasTXT.setText("0");

        }
    }


    public void consultarCantidadReal(){
        int real=0;
        try {


            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="SELECT (isnull(cantreal,0))   FROM existen WHERE barcode='"+barcode+ "' and talla="+punto.getSelectedItem()+" and tienda="+listado;
            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData rsmd=rs.getMetaData();
            while(rs.next()) {
                real =rs.getInt(1);

            }
            st.close();
            rs.close();


        } catch (SQLException e1) {
            Toast.makeText(getActivity(),"Error cantidad Real",Toast.LENGTH_LONG).show();
        }
        buscarEnMitienda(real);

    }




    public void llenarPuntos(){

        try {
            puntos.clear();
            puntos.add("");
            puntos.clear();


            double inicio, fin, medio;
            inicio = Double.valueOf(in);
            fin = Double.valueOf(finn);
            medio = Double.valueOf(inc);

            for (double i = inicio; i < fin + medio; i = i + medio) {
                puntosCantidad++;
                puntos.add(i);
            }

        }catch (Exception e){

        }
    }

    public void contarPuntos(){

        try {
            puntos.clear();


            double inicio, fin, medio;
            inicio = Double.valueOf(in);
            fin = Double.valueOf(finn);
            medio = Double.valueOf(inc);

            for (double i = inicio; i < fin + medio; i = i + medio) {

                puntosCantidad++;

            }

        }catch (Exception e){

        }
    }

    public static int obtenerPosicionItem(Spinner spinner, String fruta) {

        int posicion = 0;

        for (int i = 0; i < spinner.getCount(); i++) {

            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(fruta)) {
                posicion = i;
            }
        }

        return posicion;
    }

    public  static void llenarSp() {
        lista.add(null);
        lista.clear();
        String add="";
        lista.add("");
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select DISTINCT c.color, c.numero from colores c inner join articulo a on c.numero = a.color inner join marcas m on m.numero=a.marca where a.estilo = '"+sp2.getText()+"' and a.marca="+idMarca;
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                add=rs.getString(1);
                colorCantidad++;
                lista.add(add);

            }
            st.close();
            rs.close();
        } catch (Exception e) {

        }
    }

    public  void contarSp() {
        String add="";
        idMarca=getMarca(spMarca.getSelectedItem().toString());
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select DISTINCT c.color, c.numero from colores c inner join articulo a on c.numero = a.color inner join marcas m on m.numero=a.marca where a.estilo = '"+sp2.getText()+"' and a.marca="+idMarca;
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                add=rs.getString(1);
                colorCantidad++;
            }
            st.close();
            rs.close();

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
            st.close();


        } catch (Exception e) {

        }

        return idColor;
    }



    public void llenarSp3() {
        listaAcabado.clear();
        listaAcabado.add("");
        idColor=getColor(spColor.getSelectedItem().toString());
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select DISTINCT ac.Acabado, numero from acabados ac inner join articulo a on ac.numero = a.acabado where a.estilo = '"+sp2.getText()+"' and a.Color = "+idColor+"";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                acabadoCantidad++;
                listaAcabado.add(rs.getString(1));




            }
            st.close();

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en sp3", Toast.LENGTH_SHORT).show();

        }

    }

    public void contarSp3() {
        idColor=getColor(spColor.getSelectedItem().toString());
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select DISTINCT ac.Acabado, numero from acabados ac inner join articulo a on ac.numero = a.acabado where a.estilo = '"+sp2.getText()+"' and a.Color = "+idColor+"";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                String add= (rs.getString(1));
                acabadoCantidad++;
            }
            st.close();


        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en sp3", Toast.LENGTH_SHORT).show();
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
            st.close();


        } catch (Exception e) {

        }
        return idAcabado;
    }

    public void llenarSp4() {
        listaMarca.clear();
        listaMarca.add(" ");

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select DISTINCT  marca, numero from marcas ";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                marcaCantidad++;
                listaMarca.add(rs.getString(1));
                spMarca.setId(rs.getInt(2));




            }
            st.close();


        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en sp4", Toast.LENGTH_SHORT).show();
        }

    }

    public void contarSp4() {
        listaMarca.clear();
        listaMarca.add(" ");

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select DISTINCT  marca, numero from marcas";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                marcaCantidad++;
                String add=(rs.getString(1));

            }
            st.close();


        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en sp4", Toast.LENGTH_SHORT).show();
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
            st.close();


        } catch (Exception e) {

        }
        return idMarca;
    }


    public void llenarSp5() {
        listaCorrida.clear();
        listaCorrida.add(" ");

        idAcabado=getAcabado(spAcabado.getSelectedItem().toString());

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select  co.Nombre as Corrida,  co.id from corridas co inner join articulo a on co.id = a.corrida where a.estilo = '"+sp2.getText()+"' and a.Color = "+idColor+" and a.acabado = "+idAcabado+" and a.marca =" +idMarca+"";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                corridaCantidad++;
                listaCorrida.add(rs.getString(1));
                spCorrida.setId(rs.getInt(2));

            }
            st.close();

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en sp5", Toast.LENGTH_SHORT).show();
        }

    }

    public void contarSp5() {



        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select  co.Nombre as Corrida from corridas co inner join articulo a on co.id = a.corrida where a.estilo = '"+sp2.getText()+"' and a.Color = "+idColor+" and a.acabado = "+idAcabado+" and a.marca =" +idMarca+"";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                corridaCantidad++;
                String add=(rs.getString(1));
            }

            st.close();
            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en sp5", Toast.LENGTH_SHORT).show();
        }

    }

    public String getCorrida(String corrida){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="SELECT id FROM corridas WHERE nombre='"+corrida+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {

                idCorrida=rs.getString(1);
            }
            st.close();


        } catch (Exception e) {

        }
        return idCorrida;
    }

    public void traerPrecio() {
        double Total;

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select precio,  DESCTO from precios where BARCODE="+"'"+barcode+"' and talla="+punto.getSelectedItem();
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                variableS=rs.getString(1);
                descuento=rs.getString(2);
                precioTXT.setText(variableS);
                descuentoTXT.setText("%"+descuento);
                generarDescuento();
            }
            st.close();

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en traer precio", Toast.LENGTH_SHORT).show();
        }

    }

    public void limpiarCajas(){
        existenciasTXT.setText(null);
    }


    public void llenarTabla(){
        String idCorrida=getIdCorrida();
        where=" where ";
        if(buscador==0){
            where+=" a.estilo = '"+sp2.getText()+"' and a.Color ="+idColor+" and a.acabado ="+idAcabado+" and a.marca ="+idMarca+" and a.corrida="+idCorrida;
        }else if(buscador==1){
            where+=" a.estilo = '"+sp2.getText()+"' and a.marca ="+idMarca+" and a.corrida="+idCorrida;
        }



        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();

            String sql="select a.BARCODE,co.inicial,co.final,co.incremento,c.color,ma.marca,ac.acabado,im.id,a.UBICA,t.NUMERO,clas.NUMERO,sl.NUMERO,su.numero,ta.NUMERO,co.id\n" +
                    " from articulo a inner join lineas l on a.LINEA=l.NUMERO inner join sublinea sl on a.SUBLINEA=sl.NUMERO inner join temporad t on a.TEMPORAD=t.NUMERO\n" +
                    "  inner join proveed p on a.PROVEED=p.numero\n" +
                    "  left join empleado e on a.comprador=e.numero inner join departamentos d on a.DEPARTAMENTO=d.NUMERO\n" +
                    "  inner join tacones ta on a.TACON=ta.NUMERO inner join plantillas pl on a.PLANTILLA=pl.NUMERO inner join forros f on a.FORRO=f.NUMERO \n" +
                    "  inner join corridas co on a.corrida=co.id inner join suelas su on a.SUELA=su.numero inner join colores c on a.color = c.numero\n" +
                    "  inner join acabados ac on a.ACABADO=ac.NUMERO inner join marcas ma on a.MARCA=ma.NUMERO left join imagenes im on a.id=im.id inner join clasific clas on a.CLASIFIC=clas.numero "+ where;
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                Similar simi=new Similar();
                barcode=rs.getString(1);
                in=rs.getString(2);
                finn=rs.getString(3);
                inc=rs.getString(4);
                color=rs.getString(5);
                marcas=rs.getString(6);
                acabado=rs.getString(7);
                idImagen=rs.getString(8);
                ubica=rs.getString(9);
                simi.setMarca(marcas);
                simi.setColor(color);
                simi.setAcabado(acabado);
                simi.setTemporada(rs.getString(10));
                simi.setClasificacion(rs.getString(11));
                simi.setSubLinea(rs.getString(12));
                simi.setSuela(rs.getString(13));
                simi.setTacon(rs.getString(14));
                simi.setCorrida(rs.getString(15));



                if(ubica.equals("")){
                    sinUbica=true;
                }

            }
            st.close();


        } catch (Exception e) {
            Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
        }

    }


    public void ToolBarNombre(){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select nombre from tiendas where numero="+listado;
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {

                NombreTienda=rs.getString(1);


            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en llenar encabezado", Toast.LENGTH_SHORT).show();
        }

    }


    public void insertarComandero(){

        String AuxTotal=String.valueOf(pre);
        String AuxPares=String.valueOf(up);
        md.setNumero(idFecha);
        md.setTienda(listado);
        md.setCliente(clienteTXT.getText().toString());
        md.setFecha("getDate()");
        md.setTotal(AuxTotal);
        md.setUbicacion(ubica);
        md.setStatus("1");
        md.setPares(AuxPares);
        md.setEmpleado(NombreUsuario);
        md.setImpreso("0");

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
            st.close();


        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error al optener usuario", Toast.LENGTH_SHORT).show();
        }
    }




    public static void buscarEnSku(){


        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="SELECT saz FROM sku WHERE sku ='"+VarEstilo+"'";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                sku=(rs.getString(1));
            }

            st.close();
        } catch (SQLException xe) {
            xe.getMessage();

        }
    }

    public static void validarSku(){
        String predeterminada = "SALIDA";
        String   entrada = sku;
        String aux="";

        if(sku!=null) {
            configurarBarcodeSku();
            consultarPuntoBar();
            scu=1;

        }else{

            configurarBarcode();

        }





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


    public static void configurarBarcode(){
        String aux=VarEstilo;
        String[] barcode=aux.split("");
        String res=""+barcode[9]+barcode[10]+barcode[11];
        Double resint=Double.parseDouble(res);
        puntoBar=resint/10;
        CodigoBar=barcode[0]+barcode[1]+barcode[2]+barcode[3]+barcode[4]+barcode[5]+barcode[6]+barcode[7]+barcode[8];
    }


    public static void consultarPuntoBar(){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select  LEFT(saz,LEN(saz)-3) as barcode, cast(cast(RIGHT(saz,3)as decimal(4,1))/10 as decimal(4,1))  as talla from sku where sku = '"+VarEstilo+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                puntoBar=rs.getDouble(2);
            }

            st.close();

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (SQLException xe) {
            xe.getMessage();
            // Toast.makeText(getApplicationContext(), "Error obtener barcode", Toast.LENGTH_SHORT).show();
        }
    }
    public static void getBarcode(){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select p.barcode from precios p inner join articulo a on a.barcode = p.barcode where p.talla = "+puntoBar+" and a.id = "+CodigoBar);


            while (rs.next()) {
                BarCodeFIN=rs.getString(1);

            }
            st.close();

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
            st.close();


        } catch (Exception e) {

        }

    }
    public void getIdPedido(){

        idPedido.setText("No. de pedido: " + idFecha);
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



    public void mandarNotificacion(){
        ModeloNumeroOrden mno=new ModeloNumeroOrden();
        int AreaActual=getAreaActual();
        try {
            String tienda=ultimaVez();
            ModeloUsuario mu=new ModeloUsuario();
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select top 1 comandero.cliente ,estilo, comanderoDet.numero from comanderoDet inner join comanderoLog on comanderoLog.numero=comanderoDet.numero inner join comandero on comandero.numero=comanderoDet.numero where comanderodet.status=1 and comandero.tienda="+tienda+" and comanderoLog.hora >'"+horaUlti+"' and comanderoLog.fecha >= '"+fechaUlti+"' and empleado='"+mu.getNombre()+"' order by hora desc ";
            ResultSet rs = st.executeQuery(sql);


            if(cantidadRegistros>Principal.hiloCantidad)
            {

                while (rs.next()) {
                    String cliente = rs.getString(1);
                    String estilo = rs.getString(2);
                    String numero = rs.getString(3);
                    if (cantidadRegistros>Principal.hiloCantidad || AreaActual== -1 ) {
                        notification("PEDIDO SURTIDO DEL CLIENTE: " + cliente, "Estilo: " + estilo, context);
                        Principal.hiloCantidad=cantidadRegistros;
                    }

                }

                Principal.hiloCantidad=cantidadRegistros;

            }


        } catch (RuntimeException r){

        }catch (Exception e) {

        }

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
        }catch (RuntimeException r){

        }catch (Exception e){

        }

        return numeroT;
    }


    public void ConsultarNuevosRegistros(){
        ModeloNumeroOrden mno=new ModeloNumeroOrden();
        try {

            String tienda=ultimaVez();
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="SELECT COUNT(numero) FROM comanderoDet where tienda="+tienda+" and [status]=1";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {

                cantidadRegistros=rs.getInt(1);
            }
            st.close();


        } catch (RuntimeException r){

        }catch (Exception e) {
            Toast.makeText(getActivity(),"",Toast.LENGTH_LONG).show();
        }
    }

    public void consultaUltimoRegistro(){

        try{
            String tienda =ultimaVez();
            Statement st=bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select top 1 fecha, hora from comanderoDet inner join comanderoLog on comanderoLog.numero=comanderoDet.numero where status=1 and tienda="+tienda+"  order by fecha, hora desc";
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                fechaUlti=rs.getString(1);
                horaUlti=rs.getString(2);
            }
            st.close();

        }catch (SQLException e){

        }catch (RuntimeException r){

        }

    }
    public String getIdCorrida(){
        String idCorrida=null;
        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql=" select DISTINCT corridas.id from corridas  inner join articulo on  corridas.id=articulo.corrida  where estilo='"+sp2.getText()+"' and marca="+idMarca +"and nombre='"+spCorrida.getSelectedItem()+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                idCorrida=rs.getString(1);

            }
            st.close();


        }catch (RuntimeException r){

        } catch (Exception e) {
            Toast.makeText(getActivity(),"************",Toast.LENGTH_LONG).show();
        }
        return  idCorrida;
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

    public int getAreaActual(){
        int area=0;
        String idEmpleado=getIdUsuario();
        try {
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "select top 1 idArea from AreasAsignadas where idEmpleado="+idEmpleado+" and  fecha=CONVERT(nCHAR(8), getDate() , 112) order by hora desc ";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                area=(rs.getInt(1));
            }
            st.close();
        } catch (RuntimeException r){

        } catch (Exception e) {
        }
        return area;
    }

    public void buscador(){
        try {
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getActivity(), "db tienda", null, 1);
            SQLiteDatabase db = conn.getReadableDatabase();

            String sql="SELECT "+Utilidades.CAMPO_BUSCADOR+" FROM "+Utilidades.TABLA_CHECKB;
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                buscador= Integer.parseInt(cursor.getString(0));
            }
        }catch (Exception e) {
        }
    }

    public void limpiarListas(){
        listaCorrida.clear();

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
            Toast.makeText(getActivity(), "No sé puede actualizar el dispositivo", Toast.LENGTH_SHORT).show();
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


                    if (aux.equals(predeterminada)) {

                        //no hay pedo

                    } else {

                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getActivity() );
                        alerta.setMessage("Se ha cerrado la sesión actual ya que otro usuario ha accedido con tus datos en otro dispositivo.")
                                .setCancelable(false).setIcon(R.drawable.aviso)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getActivity(), Principal.class);
                                        startActivity(intent);


                                    }
                                });

                        android.app.AlertDialog titulo=alerta.create();
                        titulo.setTitle("Aviso");
                        titulo.show();

                    }


                } else {

                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getActivity() );
                    alerta.setMessage("Se ha cerrado la sesión actual ya que otro usuario ha accedido con tus datos en otro dispositivo.")
                            .setCancelable(false).setIcon(R.drawable.aviso)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), Principal.class);
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
            Toast.makeText(getActivity(), "No sé puede consultar el id de la empresa", Toast.LENGTH_SHORT).show();
        }
        return id;
    }

    public  void buscarMarcas() {
        listaMarca.add(null);
        listaMarca.clear();
        String add="";
        listaMarca.add("");
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select DISTINCT  marcas.MARCA,articulo.MARCA   from articulo inner join marcas on articulo.MARCA=marcas.NUMERO where estilo='"+sp2.getText()+"' ";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {


                add=rs.getString(1);
                marcaCantidad ++;
                listaMarca.add(add);

            }
            st.close();

            //
        } catch (Exception e) {

        }



    }

    public void obtenerCorrida() {
        idMarca=getMarca(spMarca.getSelectedItem().toString());
        listaCorrida.clear();
        listaCorrida.add(" ");

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select DISTINCT  co.Nombre as Corrida,  co.id from corridas co inner join articulo a on co.id = a.corrida where a.estilo = '"+sp2.getText()+"' and  a.marca =" +idMarca+"";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {

                corridaCantidad++;
                listaCorrida.add(rs.getString(1));
                spCorrida.setId(rs.getInt(2));

            }
            st.close();


        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en sp5", Toast.LENGTH_SHORT).show();
        }

    }

    public void traerDatosProducto(){
        idCorrida=getIdCorrida();

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();

            String sql="select a.BARCODE,co.inicial,co.final,co.incremento,c.color,ma.marca,ac.acabado,im.id,a.UBICA\n" +
                    " from articulo a inner join corridas co on a.corrida=co.id inner join colores c on a.color = c.numero\n" +
                    "  inner join acabados ac on a.ACABADO=ac.NUMERO inner join marcas ma on a.MARCA=ma.NUMERO left join imagenes im on a.id=im.id where a.estilo = '"+sp2.getText()+"' and a.color="+idColor+" and a.marca = "+idMarca+" and a.acabado="+idAcabado+" and a.corrida="+idCorrida;
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {


                barcode=rs.getString(1);
                in=rs.getString(2);
                finn=rs.getString(3);
                inc=rs.getString(4);
                color=rs.getString(5);
                marcas=rs.getString(6);
                acabado=rs.getString(7);
                idImagen=rs.getString(8);
                ubica=rs.getString(9);

                if(ubica==null){
                    sinUbica=true;
                }
            }
            st.close();

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en llenar Tabla", Toast.LENGTH_SHORT).show();
        }

    }

    public String ubicacionProducto(){
        String ubicacion="";
        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "select nombre from UbicacionesProductos inner join ZonasDeSurtido on UbicacionesProductos.idZona=ZonasDeSurtido.idZona where barcode='"+barcode+"' and UbicacionesProductos.idtienda="+ultimaVez();
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                ubicacion=(rs.getString(1));

            }
            st.close();

        } catch (RuntimeException r){

        }catch (Exception e) {
        }

        return ubicacion;
    }

    public String consultarEmpleado(){
        String id="";
        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "select id from empleado where nombre='"+mu.getNombre()+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                id=(rs.getString(1));

            }
            st.close();




        }catch (SQLException e) {
        }
        return id;
    }


    public void fechaFormato(){

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

    public void inicializarMarca() {
        try {
            listaMarca.clear();
            listaCorrida.clear();
            puntos.clear();


            spCorrida.setAdapter(null);
            punto.setAdapter(null);
            limpiarCajas();

            ReinicarContadores();

            contarSp4();
            if (Principal.scannPass == true) {
                llenarSp4();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaMarca);
                spMarca.setAdapter(adapter);
                spMarca.setSelection(obtenerPosicionItem(spMarca, marcaBar));
            } else if (Principal.scannPass == false && marcaCantidad == 1) {
                llenarSp4();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaMarca);
                spMarca.setAdapter(adapter);
                spMarca.setSelection(1);


            } else if (Principal.scannPass == false && marcaCantidad > 1) {
                llenarSp4();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, listaMarca);
                spMarca.setAdapter(adapter);

            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

}
