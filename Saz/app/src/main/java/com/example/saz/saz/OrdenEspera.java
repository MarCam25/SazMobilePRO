package com.example.saz.saz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.saz.saz.Modelo.ModeloDatos;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.entidades.Comandero;
import com.example.saz.saz.utilidades.AdapterDatos;
import com.example.saz.saz.utilidades.Utilidades;

import java.util.ArrayList;

public class OrdenEspera extends AppCompatActivity {
    ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();
ModeloDatos md=new ModeloDatos();
    String aux;
    Long idResultante;

    ArrayList<Comandero> listaComandero;
    RecyclerView recyclerView;



static boolean pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orden_espera);

        getSupportActionBar().setTitle("Órdenes en espera");


listaComandero=new ArrayList<>();
recyclerView=(RecyclerView)findViewById(R.id.recyclerId);
recyclerView.setLayoutManager(new LinearLayoutManager(this));


       // saveDate();
        mostrarOrdenEspera();
        AdapterDatos adapter=new AdapterDatos(listaComandero);
        recyclerView.setAdapter(adapter);



    }


    public void saveDate() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String valor=md.getNumero();


if(pass==true){
    db.execSQL("INSERT INTO  TIENDA (nombreT, datos, status) VALUES('prube', '"+md.getNumero() +"-"+md.getTienda()+"-"+md.getCliente()+"-"+md.getTotal()+"-"+md.getUbicacion()+"-"+md.getPares()+"-"+md.getEmpleado()+"',1)");
}



    }


    public void mostrarOrdenEspera() {

        String mensaje="No tienes órdenes en espera";

        SQLiteDatabase db = conn.getReadableDatabase();

        Comandero comandero=null;



           Cursor cursor = db.rawQuery("SELECT * FROM " + Utilidades.TABLA_ORDEN , null);
           while (cursor.moveToNext()) {
            comandero=new Comandero();
            mensaje="";
               comandero.setIdOrden(cursor.getString(0));
               comandero.setCliente(cursor.getString(2));
               comandero.setEmpleado(cursor.getString(3));
               comandero.setNumero(cursor.getString(4));
               comandero.setTotal(cursor.getString(5));
               comandero.setPares(cursor.getString(6));

            listaComandero.add(comandero);


           }
        Toast.makeText(this, ""+mensaje, Toast.LENGTH_LONG).show();
           cursor.close();
           db.close();




    }


    public static void GuardarEnSqlRemoto(){
      /*  try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="insert into comandero(numero,Tienda,Cliente,fecha,total,ubicacion,[status],pares,empleado,impreso,Llave) values ('"+idFecha+"',"+listado+",'"+clienteTXT.getText()+"',getDate(),"+pre+",'-1',1,"+up+","+numeroUsuario+",0,newId());";
            st.executeUpdate(sql);

        } catch (Exception e) {
         //   Toast.makeText(, "Error en comandero", Toast.LENGTH_SHORT).show();
        }*/
    }








    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(getApplicationContext(), Hamburguesa.class);


            startActivity(intent);
        }
        return true;
    }



}
