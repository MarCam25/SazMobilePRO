package com.example.saz.saz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.example.saz.saz.Modelo.ModeloDatos;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloResumen;
import com.example.saz.saz.Modelo.ModeloUsuario;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.utilidades.AdapterResumen;
import com.example.saz.saz.utilidades.Utilidades;

import java.util.ArrayList;

public class Resumen extends AppCompatActivity {
    String  contendedor;
    Button fin;
    String[] resumen,fila;
    public static ConexionBDCliente bdc=new ConexionBDCliente();
    GridView gridview;
    public static ModeloEmpresa me=new ModeloEmpresa();
    ModeloUsuario mu=new ModeloUsuario();
    ModeloDatos md=new ModeloDatos();
       ArrayList<String> arrayList;

    RecyclerView recyclerView;
    ArrayList<ModeloResumen> listaResumen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);
        listaResumen=new ArrayList<>();
        fin=(Button)findViewById(R.id.idFinalizar);
        gridview = (GridView) findViewById(R.id.gridview);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AdapterResumen adapter=new AdapterResumen(listaResumen);
        recyclerView.setAdapter(adapter);

mostrar();

fin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        crearOrden();
        getDatosPedido();
        Intent orden = new Intent(getApplicationContext(), OrdenEspera.class);
        startActivity(orden);
    }
});





}

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(getApplicationContext(), Hamburguesa.class);


            startActivity(intent);
        }
        return true;
    }

    public void mostrar(){

        ModeloResumen mr=null;

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_CONTENEDOR , null);
        while (cursor.moveToNext()) {

            mr=new ModeloResumen();

            mr.setId(cursor.getString(0));
            mr.setEstilo(cursor.getString(1));
            mr.setImagen(cursor.getString(2));
            mr.setPunto(cursor.getString(3));
            mr.setCantidad(cursor.getString(4));
            mr.setMarca(cursor.getString(5));
            mr.setColor(cursor.getString(6));
            mr.setSubtotal(cursor.getString(7));
            mr.setTotal(cursor.getString(8));
            mr.setBarcode(cursor.getString(9));
            mr.setAcabado(cursor.getString(10));







            listaResumen.add(mr);





        }
    }
    public void crearOrden(){
        String pares=sacarPares();
        String total=sacarTotal();

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db=conn.getWritableDatabase();


        String sql="INSERT INTO  orden  (status, cliente, empleado, norden, total, pares, impreso) VALUES('"+0+"', '"+ConsultaF.clienteTXT.getText().toString()+"', '"+mu.getNombre()+"', '"+ConsultaF.idFecha+"','"+total+"','"+pares+"','"+0+"')";
        db.execSQL(sql);



    }

    public String sacarPares(){
        String pares="";
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT SUM(cantidad) FROM " + Utilidades.TABLA_CONTENEDOR , null);
        while (cursor.moveToNext()) {
            pares=(cursor.getString(0));



        }
        return pares;
    }

    public String sacarTotal(){
        String total="";
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT SUM(total)FROM " + Utilidades.TABLA_CONTENEDOR , null);
        while (cursor.moveToNext()) {
            total=(cursor.getString(0));



        }
        return total;
    }


    public void getDatosPedido(){


        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
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
            String ubica=(cursor.getString(12));






            insertarPedidosEnOrden(estilo,imagen,punto,cantidad,marca,color,sub,total,bar,aca,ubica);

            deleteContenedor();

        }

    }

    public void deleteContenedor(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);


        SQLiteDatabase db = conn.getReadableDatabase();





        db.execSQL("DELETE FROM  " + Utilidades.TABLA_CONTENEDOR);


        db.close();
    }
    public void insertarPedidosEnOrden(String estilo, String imagen, String talla, String cant, String marca, String color, String sub, String total, String barcode,String acabado,String ubica){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db=conn.getWritableDatabase();


        int idOrden=getOrden();

        db.execSQL("INSERT INTO  pedido (estilo, imagen, talla, cantidad, marca, color, sub, total, barcode,acabado,idOrden) VALUES('"+estilo+"', '"+imagen+"', '"+talla+"', '"+cant+"','"+marca+"','"+color+"','"+sub+"','"+total+"','"+barcode+"','"+acabado+"',"+idOrden+")");


    }

    public int getOrden(){
        int idOrden = 0;

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT idOrden FROM " + Utilidades.TABLA_ORDEN , null);
        while (cursor.moveToNext()) {

            idOrden=cursor.getInt(0);
        }
        return idOrden;
    }


    }


