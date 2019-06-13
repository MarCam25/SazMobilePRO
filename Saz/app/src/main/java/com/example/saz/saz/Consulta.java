package com.example.saz.saz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toolbar;

import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloNumeroOrden;
import com.example.saz.saz.Modelo.ModeloUsuario;
import com.example.saz.saz.Modelo.Zapato;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.conexion.ConexionSqlServer;
import com.example.saz.saz.entidades.Comandero;
import com.example.saz.saz.utilidades.Utilidades;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Consulta extends AppCompatActivity  {
    GridView gridview;
    String empress;
    String NombreTienda;

    double descuentoFinal;
    double auxiliarDes;

    private ZXingScannerView vistaescaner;
    ArrayList<String> arrayList;
    double precio=0.0;
    Button btnDetalle, btnMas, btnMenos, btnAgregar,btnResumen,btnFinalizar;
    static int puntoBar;
    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();
    ConexionSqlServer conex=new ConexionSqlServer();
    int cantidad=0, up=0;
    public static String VarEstilo;
    static String clint, BarCodeFIN;
    static  String estiloBar="";
    TextView edtCodigo;
    Double precioPedido;
    String variableS,descuento;
    public static ArrayList lista=new ArrayList();
    ArrayList listaAcabado=new ArrayList();
    static String CodigoBar;
    ArrayList listaMarca=new ArrayList();
    ArrayList listaCorrida=new ArrayList();
    ArrayList puntos=new ArrayList();
    String numeroUsuario;

    boolean xcaner=false;
    ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"db tienda",null,1);

    ArrayList listaTiendas=new ArrayList();
    private String numeroTienda;
    long time =System.currentTimeMillis();
    String barcode;
    String estiloTem,puntoTem,precioTem, cantidadTem;
    String idImagen;
    static  String idColor=null,idAcabado=null,idMarca=null,idCorrida=null;
    TextView precioTXT;
    int index=0;
    private Button btnBardoce;
    double tot;
    String user, scaneado;
    static String sku;
    String totalOrden, cantidadOrden;
    String intentResummen;
    double pre = 0, contenedor;
    String  point;
    int existencias;
    Spinner sp,sp3, sp4, sp5, punto;
    String num, str,mar;
    TextView existenciasTXT, cantidadTXT, unidadesTXT, importeTXT,descuentoTXT,totalTXT;

    public static EditText sp2;

    TextView tiendaTxt,puntoTxt,totalTxt,dato1Txt,dato2Txt,dato3Txt;
    double r;
    String in, finn, inc;
    String estilo, color, acabado, marcas, linea, sublinea, temporada, descripcion, observaciones;
    TextView lineaProvedor, provedor, pagina, basico, comprador, departamento, tacon, plantilla, forro, clasificacion, corrida, suela, ubicacion;

    String listado;

    java.sql.Timestamp timestamp=new java.sql.Timestamp(time);

    String idFecha;
    ModeloNumeroOrden mno=new ModeloNumeroOrden();
    ModeloUsuario mu=new ModeloUsuario();
    String NombreUsuario;

    Zapato zapato=new Zapato();


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_consulta);
            String[] ar;
            consultarT();

        Toast.makeText(getApplicationContext(), ""+mno.getEstilo(), Toast.LENGTH_SHORT).show();



      getSupportActionBar().setTitle("Editar Pedido sazmobile.com");
            //obtenerLineaConexion();
            ToolBarNombre();

            getUser();


            sp2=(EditText)findViewById(R.id.sp2);

        sp2.setText(mno.getEstilo());
            sp =(Spinner) findViewById(R.id.sp);
            sp3=(Spinner)findViewById(R.id.sp3);
            sp4=(Spinner)findViewById(R.id.sp4);
            sp5=(Spinner)findViewById(R.id.sp5);
            punto=(Spinner)findViewById(R.id.punto);
            precioTXT=(TextView)findViewById(R.id.precioTXT);
            cantidadTXT=(TextView)findViewById(R.id.cantidadTXT);
            importeTXT=(TextView)findViewById(R.id.importeTXT);
            unidadesTXT=(TextView)findViewById(R.id.unidadesTXT);
            descuentoTXT=(TextView)findViewById(R.id.descuentoTXT);
            totalTXT=(TextView)findViewById(R.id.totalTXT);



            btnMas=(Button)findViewById(R.id.btnMas);
            btnAgregar=(Button)findViewById(R.id.btnAgregar);
            btnMenos=(Button)findViewById(R.id.btnMenos);
            btnFinalizar=(Button)findViewById(R.id.btnFinalizar);
            btnBardoce=(Button)findViewById(R.id.btnBarCode);


            btnDetalle=(Button)findViewById(R.id.btnDetalle);
            existenciasTXT=(TextView)findViewById(R.id.existenciasTXT);
        /*    tiendaTxt=(TextView)findViewById(R.id.tiendaCol);
            puntoTxt=(TextView)findViewById(R.id.puntoCol);
            totalTxt=(TextView)findViewById(R.id.totalCol);

            dato1Txt=(TextView)findViewById(R.id.tiendaFil);
            dato2Txt=(TextView)findViewById(R.id.puntoFil);
            dato3Txt=(TextView)findViewById(R.id.totalFil);


*/





     /*   btnBardoce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xcaner=true;
                Intent intent=new Intent(getApplicationContext(),Scann.class);
                startActivity(intent);


            }
        });*/


     btnDetalle.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent=new Intent(getApplicationContext(),Imagen.class);
             startActivity(intent);
         }
     });

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrdenEspera.class);
                startActivity(intent);

            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try{
                   int cantiTxt=Integer.parseInt(cantidadTXT.getText().toString());
                   if (cantiTxt > 0) {

                    enviarADetalle();
                   generarImporte();
                  generarCantidad();
                  pedido();
                  regresarExistencias();
                  eliminarPedido();
                  insertarPedido();

                    if(up!=0) {
                        FormatoFecha();
                         //  insertarComandero();
                        // insertarComanderoDet();

                        limpiarCajas();
                        Finalizar();
                        sp2.setText(null);
                        lista.clear();
                        sp.setAdapter(null);

                        listaAcabado.clear();
                        sp3.setAdapter(null);

                        listaMarca.clear();
                        sp4.setAdapter(null);

                        listaCorrida.clear();
                        sp5.setAdapter(null);

                        limpiarCajas();

                        puntos.clear();
                        punto.setAdapter(null);

                        cantidadTXT.setText(null);
                        precioTXT.setText(null);


                        unidadesTXT.setText(null);
                        importeTXT.setText(null);
                        precioTXT.setText(null);
                        descuentoTXT.setText(null);
                        totalTXT.setText(null);
                        up = 0;
                        pre = 0;
                        intentResummen = null;
                        tot = 0;


                        Intent intent = new Intent(getApplicationContext(), OrdenEspera.class);
                        startActivity(intent);
                    }
                    }else{


                    }


                }catch (Exception e){
                   e.getMessage();
                }




            }
        });




    btnMas.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            try {
                if(existencias>0) {
                    existencias--;
                    existenciasTXT.setText(String.valueOf(existencias));
                    cantidad++;
                    String res = String.valueOf(cantidad);
                    cantidadTXT.setText(res);
                }


            }catch(Exception e){

            }

        }
    });

                btnMenos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {


                            if (cantidad > 0) {
                                existencias++;
                                existenciasTXT.setText(String.valueOf(existencias));
                                cantidad--;
                                String res = String.valueOf(cantidad);
                                cantidadTXT.setText(res);
                            }
                        }catch(Exception e){

                        }
                    }
                });



            sp2.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {

                        Toast toast =new  Toast(getApplicationContext());
                        pl.droidsonroids.gif.GifImageView view=new  pl.droidsonroids.gif.GifImageView(getApplicationContext());
                        view.setImageResource(R.drawable.loading);
                        toast.setView(view);
                        toast.show();
                        puntos.clear();
                        cantidad=Integer.parseInt(zapato.getCantidadE());
                        unidadesTXT.setText(zapato.getCantidadE());
                        importeTXT.setText(zapato.getPrecio());
                        cantidadTXT.setText(String.valueOf(cantidad));
                        punto.setAdapter(null);
                        llenarSp();


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Consulta.this, R.layout.spinner_item, lista);
                        sp.setAdapter(adapter);
                        String colorZap=String.valueOf(zapato.getColorE());
                        sp.setSelection(obtenerPosicionItem(sp,colorZap));


                        return true;
                    }
                    return false;
                }
            });








            punto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        cantidadTXT.setText("0");
                        consultarCantidadReal();


                        traerPrecio();

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
                        sp.setAdapter(null);

                        listaAcabado.clear();
                        sp3.setAdapter(null);

                        listaMarca.clear();
                        sp4.setAdapter(null);

                        listaCorrida.clear();
                        sp5.setAdapter(null);

                        limpiarCajas();

                        puntos.clear();
                        punto.setAdapter(null);

                        cantidadTXT.setText(null);
                        precioTXT.setText(null);



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

                        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(Consulta.this, R.layout.spinner_item, listaAcabado);
                        sp3.setAdapter(adapter3);
                        sp3.setSelection(obtenerPosicionItem(sp3,zapato.getAcabadoE()));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                sp3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sp4.setAdapter(null);
                    listaMarca.clear();
                    llenarSp4();

                        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(Consulta.this, R.layout.spinner_item, listaMarca);
                        sp4.setAdapter(adapter4);
                        sp4.setSelection(obtenerPosicionItem(sp4,zapato.getMarcaE()));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                sp4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sp5.setAdapter(null);
                    listaCorrida.clear();
                    llenarSp5();

                        ArrayAdapter<String> adapter5 = new ArrayAdapter<String>(Consulta.this, R.layout.spinner_item, listaCorrida);
                        sp5.setAdapter(adapter5);
                        String corrida=zapato.getCorridaE();
                        sp5.setSelection(obtenerPosicionItem(sp5,corrida));



                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



                sp5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                     llenarTabla();
                        // contarColumnas();
                        //llenarExistencias();
                        llenarPuntos();
                        //   llenarExistencias();
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Consulta.this, R.layout.spinner_item_punto, puntos);
                        punto.setAdapter(adapter);
                        punto.setSelection(obtenerPosicionItem(punto,zapato.getPuntoE()));



                        //              AdapterDatos adapter= new AdapterDatos(listaDatos);
    //                recycler.setAdapter(adapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }


        private void consultarT() {
            SQLiteDatabase db=conn.getReadableDatabase();
            String[] parametros={"1"};
            String[] campos={Utilidades.CAMPOS_NOMBRE};
            try {
                Cursor cursor = db.query(Utilidades.TABLA_TIENDA, campos, Utilidades.CAMPO_ID + "=?", parametros, null, null, null);
                cursor.moveToFirst();
                listado = cursor.getString(0);
                cursor.close();

            }catch (Exception e){
                Toast.makeText(getApplicationContext(),"no pos no" , Toast.LENGTH_LONG).show();
            }

        }

        private void generarCantidad() {
            up += cantidad;
            cantidad = 0;


            unidadesTXT.setText(String.valueOf(up));
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

            pre += resultado;
            precio+=resultado;
            importeTXT.setText(String.valueOf(precio));


        }


        private void enviarADetalle() {
            double tem;
            puntoTem = punto.getSelectedItem().toString();
            tem = ((Double.parseDouble(variableS)-Double.parseDouble(descuento)) * cantidad);
            precioTem = String.valueOf(tem);
            estiloTem = sp2.getText().toString();
            cantidadTem = String.valueOf(cantidad);
           // Resumen(estiloTem, puntoTem, cantidadTem, precioTem);
            estiloTem = null;
            puntoTem = null;
            cantidadTem = null;
            precioTem = null;
        }
       /* public void Resumen(String estilo,String punto, String cantidad, String precio){
            clint=clienteTXT.getText().toString();

            intentResummen+="-"+clint+","+estilo+","+punto+","+cantidad+","+precio+","+id[0]+","+marca[0]+","+variableS+","+barcode+","+numero[0];
        }*/



        public static void cargarDatos(String var){
            VarEstilo=var;
            buscarEnSku();
            validarSku();
            getBarcode();
            getEstilo();
            sp2.setText(estiloBar);
            llenarSp();
        }


        public void insertarPedido(){
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
            SQLiteDatabase db=conn.getWritableDatabase();


    String sql="INSERT INTO  pedido (estilo, imagen, talla, cantidad, marca, color, sub, total, barcode,acabado, idOrden ) VALUES('"+sp2.getText().toString()+"', '"+idImagen+"', '"+punto.getSelectedItem()+"', '"+cantidadTXT.getText().toString()+"','"+sp4.getSelectedItem()+"','"+sp.getSelectedItem()+"','"+variableS+"','"+pre+"','"+barcode+"','"+sp3.getSelectedItem()+"',"+mno.getNumeroOrden()+")";
            db.execSQL(sql);


    contarTotal();
    contarPares();
    upDateOrden();

        }


    public void contarTotal(){

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);

        SQLiteDatabase db = conn.getReadableDatabase();

        Comandero comandero = null;


        String sql = "SELECT SUM(total) FROM pedido WHERE idOrden="+mno.getNumeroOrden();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {

    totalOrden=cursor.getString(0);


        }

        cursor.close();
        db.close();

    }


        public void consultarCantidadReal(){
            int real=0;
            try {

                List<Map<String, String>> data = null;
                data = new ArrayList<Map<String, String>>();
                Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
                String sql="SELECT cantreal FROM existen WHERE barcode='"+barcode+ "' and talla="+punto.getSelectedItem()+" and tienda="+listado;
                ResultSet rs = st.executeQuery(sql);
                ResultSetMetaData rsmd=rs.getMetaData();
                while(rs.next()) {
                    real =rs.getInt(1);



                }


            } catch (SQLException e1) {

                Toast.makeText(getApplicationContext(),"Error cantidad Real",Toast.LENGTH_LONG).show();

            }
            buscarEnMitienda(real);

        }


    public void contarPares(){

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);

        SQLiteDatabase db = conn.getReadableDatabase();

        Comandero comandero = null;


        String sql = "SELECT SUM(cantidad) FROM pedido WHERE idOrden="+mno.getNumeroOrden();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {

            cantidadOrden=cursor.getString(0);


        }

        cursor.close();
        db.close();
        Toast.makeText(getApplicationContext(),""+totalOrden , Toast.LENGTH_LONG).show();
    }


        public void upDateOrden(){
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
            SQLiteDatabase db=conn.getWritableDatabase();


    String sql="UPDATE orden set total="+totalOrden+ ",pares="+cantidadTXT.getText() + " WHERE idOrden="+mno.getNumeroOrden();
           db.execSQL(sql);

        }


        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if ((keyCode == KeyEvent.KEYCODE_BACK)) {


            }
            return true;
        }


        public void buscarEnMitienda(int  real){
            try {
              /*  String numero;
                String[] n;
                numero=spT.getSelectedItem().toString();
                n=numero.split("-");*/

                // Toast.makeText(getApplicationContext(),spT.getSelectedItem().toString() , Toast.LENGTH_LONG).show();
                List<Map<String, String>> data = null;
                data = new ArrayList<Map<String, String>>();
                Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
                ResultSet rs = st.executeQuery("lupita'"+barcode+"',"+punto.getSelectedItem()+","+listado);
                ResultSetMetaData rsmd=rs.getMetaData();
                while(rs.next()) {


                    existencias=(rs.getInt(2)-real);
                    existenciasTXT.setText(String.valueOf(existencias));


                }

            } catch (SQLException e1) {
                e1.printStackTrace();
                Toast.makeText(getApplicationContext(),"error en llenar Existencias " , Toast.LENGTH_LONG).show();

            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        }



        public void llenarPuntos(){
            if(in!=null || finn!=null || inc!=null) {
                double inicio, fin, medio;
                inicio = Double.valueOf(in);
                fin = Double.valueOf(finn);
                medio = Double.valueOf(inc);

                for (double i = inicio; i < fin + medio; i = i + medio) {


                    puntos.add(i);
                }
            }else{
                Toast.makeText(getApplicationContext(),"No hay existencias de este producto",Toast.LENGTH_LONG).show();

            }
        }



        public  static void llenarSp() {
            String add="";
            try {
                Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
                ResultSet rs = st.executeQuery("select DISTINCT c.color from colores c inner join articulo a on c.numero = a.color where a.estilo = '"+sp2.getText()+"'");


                while (rs.next()) {

                    add=rs.getString(1);

                    lista.add(add);




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
                ResultSet rs = st.executeQuery("select DISTINCT ac.Acabado from acabados ac inner join articulo a on ac.numero = a.acabado where a.estilo = '"+sp2.getText()+"' and a.Color = "+idColor+"");


                while (rs.next()) {

                    listaAcabado.add(rs.getString(1));



                }

                // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error en sp3", Toast.LENGTH_SHORT).show();
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
                ResultSet rs = st.executeQuery("select DISTINCT  m.marca from marcas m inner join articulo a on m.numero = a.marca where a.estilo = '"+sp2.getText()+"' and a.Color = "+idColor+" and a.acabado = "+idAcabado+"");


                while (rs.next()) {

                    listaMarca.add(rs.getString(1));



                }

                // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error en sp4", Toast.LENGTH_SHORT).show();
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
                String sql="select  co.Nombre as Corrida from corridas co inner join articulo a on co.id = a.corrida where a.estilo = '"+sp2.getText()+"' and a.Color = "+idColor+" and a.acabado = "+idAcabado+" and a.marca =" +idMarca+"";
                ResultSet rs = st.executeQuery(sql);
    //articulo.barcode

                while (rs.next()) {

                    listaCorrida.add(rs.getString(1));

                }

                // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error en sp5", Toast.LENGTH_SHORT).show();
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

                // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error en sp5", Toast.LENGTH_SHORT).show();
            }

        }



        private void  generarDescuento() {
            double Total;
            auxiliarDes=Double.parseDouble(descuento)/100;
            descuentoFinal = Double.parseDouble(variableS)*auxiliarDes;
            Total=Double.parseDouble(variableS)- descuentoFinal;
            totalTXT.setText("$"+String.valueOf(Total));

        }



        public void limpiarCajas(){
            existenciasTXT.setText(null);
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
                Toast.makeText(this,"********",Toast.LENGTH_LONG).show();
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


                    barcode=rs.getString(1);
                    in=rs.getString(2);
                    finn=rs.getString(3);
                    inc=rs.getString(4);
                    color=rs.getString(5);
                    marcas=rs.getString(6);
                    acabado=rs.getString(7);
                    idImagen=rs.getString(8);
                }

                // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error en llenar Tabla", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Error en llenar encabezado", Toast.LENGTH_SHORT).show();
            }

        }



        public void Finalizar(){
            try {

                        Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
                       String sql="update existen set cantreal= cantreal + " +cantidadTXT.getText().toString() + " ,  pedido = pedido + " + cantidadTXT.getText().toString() + " where barcode ='" + barcode+ "' and talla = " + punto.getSelectedItem() + " and tienda = " + listado;
                        st.executeUpdate(sql);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error al finalizar el pedido", Toast.LENGTH_SHORT).show();
            }

        }

        public void insertarComanderoDet(){
            try {
                String[] columna=null,fila=null;

                columna=intentResummen.split("-");
                for(int i=1;i<columna.length;i++) {
                    fila=columna[i].split(",");
                    for(int x=0;x<1;x++) {
                        Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();

                        st.executeUpdate("insert into comanderoDet (numero,tienda,barcode,estilo,color,acabado,talla,[status],llave) values ('"+idFecha+"',"+listado+",'"+fila[8]+"','"+fila[1]+"',"+fila[5]+","+fila[9]+","+fila[2]+",1,newId())");
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error comanderoDet", Toast.LENGTH_SHORT).show();
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
                Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
                ResultSet rs=st.executeQuery("select numero, nombre from empleado where [user]='"+mno.getUsuario()+"'");

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
                ResultSet rs = st.executeQuery("select saz from sku where sku ='"+VarEstilo+"'" );


                while (rs.next()) {
                    sku=rs.getString(1);

                }


            } catch (Exception e) {

            }
        }

        public static void validarSku(){
            String predeterminada = "SALIDA";


            String   entrada = sku;


            String aux="";


            if(sku!=null) {
                if (predeterminada.length() == entrada.length()) {

                    for (int i = 0; i < predeterminada.length(); i++) {


                        if (predeterminada.charAt(i) == entrada.charAt(i)) {

                            aux += predeterminada.charAt(i);
                        }
                    }

                    if (aux.equals(predeterminada)) {



                    } else {


                    }


                }
            }else{

                configurarBarcode();

            }





        }

        public static void configurarBarcode(){
                String aux=VarEstilo;
                String[] barcode=aux.split("");
                String res=""+barcode[9]+barcode[10]+barcode[11];
                int resint=Integer.parseInt(res);
                puntoBar=resint/10;
                CodigoBar=barcode[0]+barcode[1]+barcode[2]+barcode[3]+barcode[4]+barcode[5]+barcode[6]+barcode[7]+barcode[8];




        }

    public static void getBarcode(){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select p.barcode from precios p inner join articulo a on a.barcode = p.barcode where p.talla = "+puntoBar+" and a.id = "+CodigoBar);


            while (rs.next()) {
            BarCodeFIN=rs.getString(1);



            }


        } catch (Exception e) {

        }

    }

        public static void getEstilo(){
            try {
                Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
                ResultSet rs = st.executeQuery("select a.ESTILO from colores c inner join articulo a on c.numero = a.color where a.BARCODE='"+BarCodeFIN+"'");

                while (rs.next()) {
                    estiloBar=rs.getString(1);



                }


            } catch (Exception e) {

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
        public void eliminarPedido(){

            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);


            SQLiteDatabase db = conn.getReadableDatabase();

            db.execSQL("DELETE FROM pedido WHERE id="+zapato.getId());


            db.close();


        }

    public void pedido(){
            String cantidad=null,precio = null;
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        String sql="SELECT cantidad, total FROM pedido WHERE  id="+zapato.getId();
        Cursor cr=db.rawQuery(sql,null);
        while(cr.moveToNext()){
            cantidad=cr.getString(0);
            precio=cr.getString(1);
        }

        db.close();
        restarDatos(precio,cantidad);
    }


    public void restarDatos(String total, String cant){
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);


            SQLiteDatabase db = conn.getReadableDatabase();
            String sql="UPDATE orden SET total=total-"+total+", pares=pares-"+cant + " WHERE idOrden="+mno.getNumeroOrden();
            db.execSQL(sql);


            db.close();
        }




        public void regresarExistencias(){
            try {
                ModeloEmpresa me=new ModeloEmpresa();
                ConexionBDCliente bdc=new ConexionBDCliente();

                Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
                String sql="update existen set cantreal = cantreal - " + zapato.getCantidadE()+ " ,  pedido = pedido - " + zapato.getCantidadE()+ " where barcode ='" + barcode+ "' and talla = " + zapato.getPuntoE()+ " and tienda = " + ConsultaF.listado;
                st.executeUpdate(sql);

            }catch (Exception e) {
                Toast.makeText(this, "Error al finalizar el pedido", Toast.LENGTH_SHORT).show();
            }
        }

}
