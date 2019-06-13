package com.example.saz.saz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.example.saz.saz.Modelo.ModeloNumeroOrden;
import com.example.saz.saz.Modelo.ModeloResumen;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.utilidades.AdapterEdicion;

import java.util.ArrayList;

public class  OrdenesEditar extends AppCompatActivity {


    RecyclerView recyclerView;
    ArrayList<ModeloResumen> listaResumen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordenes_editar);
        listaResumen=new ArrayList<>();

        recyclerView=(RecyclerView)findViewById(R.id.recyclerId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AdapterEdicion adapter=new AdapterEdicion(listaResumen);
        recyclerView.setAdapter(adapter);



        mostrar();




    }
    public void mostrar(){

        ModeloResumen mr=null;

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        ModeloNumeroOrden mno=new ModeloNumeroOrden();
        String filtro=mno.getNumeroOrden();
        String sql="SELECT * FROM pedido WHERE idOrden=" + filtro;
        Cursor cursor = db.rawQuery(sql, null);
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
            mr.setCorrida(cursor.getString(11));
            mr.setIdOrden(cursor.getString(13));






            listaResumen.add(mr);





        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent=new Intent(this,OrdenEspera.class);
            startActivity(intent);
        }
       return true;
    }


}
