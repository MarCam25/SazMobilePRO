package com.example.saz.saz;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saz.saz.Modelo.Areas;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloNumeroOrden;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.utilidades.AdaptadorAreas;
import com.example.saz.saz.utilidades.Utilidades;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class RegistroArea extends AppCompatActivity {
    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();

    public static Button guardar,actualizar;
    public static EditText zona;

    ArrayList<Areas> listaAreas;
    RecyclerView recyclerView;
    public static Context context;
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_area);
        zona=(EditText)findViewById(R.id.idZona);
        actualizar=(Button)findViewById(R.id.idActualizar);
        guardar=(Button)findViewById(R.id.idGuardar);

        context=getApplicationContext();
        actualizar.setEnabled(false);

        listaAreas=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recyAreas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getAreas();
        AdaptadorAreas adapter=new AdaptadorAreas(listaAreas);
        recyclerView.setAdapter(adapter);




        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(zona.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"El campo esta vacío",Toast.LENGTH_LONG).show();
                }else{

                    veificarActualizacion();
                    zona.setText(null);
                }

            }
        });



        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(zona.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"El campo esta vacío",Toast.LENGTH_LONG).show();
                }else{

                    veificar();
                    zona.setText(null);
                    actualizarPagina();
                }


            }
        });


    }


    public void veificarActualizacion(){
        String zonas=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select nombre from AreasDeControl where nombre='"+zona.getText()+"' ";
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){


                zonas=rs.getString(1);

            }


        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Error al verificar", Toast.LENGTH_SHORT).show();
        }
        if(zonas!=null){
            Toast.makeText(getApplicationContext(), "No se puede guardar el registro porque ya existe un área con el mismo nombre", Toast.LENGTH_LONG).show();
        }else{
            update();
            guardar.setEnabled(true);
            actualizar.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Área actualizada ", Toast.LENGTH_SHORT).show();
            actualizarPagina();

        }
    }
    public void veificar(){
        String zonas=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select nombre from AreasDeControl where nombre='"+zona.getText()+"' ";
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){


                zonas=rs.getString(1);

            }


        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Error al verificar", Toast.LENGTH_LONG).show();
        }
        if(zonas!=null){
            Toast.makeText(getApplicationContext(), "No se puede guardar el registro porque ya existe un área con el mismo nombre", Toast.LENGTH_LONG).show();
        }else{
            darAlta();
            Toast.makeText(getApplicationContext(), "Se a creado un nuevo registro", Toast.LENGTH_LONG).show();



        }
    }
    public void darAlta(){

        String tienda=getTienda();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="insert into AreasDeControl (idTienda,nombre)values('"+tienda+"','"+zona.getText()+"');";
            st.executeUpdate(sql);


        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Error al registrar areax", Toast.LENGTH_SHORT).show();
        }

    }
    public void update(){
        ModeloNumeroOrden mno=new ModeloNumeroOrden();
        String tienda=getTienda();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="UPDATE AreasDeControl SET nombre='"+zona.getText()+"'where idArea="+mno.getIdArea()+" and idTienda="+tienda;
            st.executeUpdate(sql);


        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Error al registrar areax", Toast.LENGTH_SHORT).show();
        }

    }



    public String  getTienda(){
        String tienda="";
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext(), "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT nombreT FROM " + Utilidades.TABLA_TIENDA , null);
        while (cursor.moveToNext()) {
            tienda =(cursor.getString(0));



        }
        return tienda;
    }


    public void getAreas(){
        String tienda=getTienda();
        Areas areas=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="  select * from AreasDeControl where idTienda="+tienda;
            ResultSet rs = st.executeQuery(sql);


            while(rs.next()){
                areas=new Areas();
                areas.setIdArea(rs.getString(1));
                areas.setIdTienda(rs.getString(2));
                areas.setNombreArea(rs.getString(3));
                listaAreas.add(areas);
            }


        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Error al registrar areax", Toast.LENGTH_SHORT).show();
        }
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Principal.location=3;
            Intent intentE=new Intent(this,menu.class);
            startActivity(intentE);
        }
        return true;
    }

    public  void actualizarPagina(){
        Principal.location=3;
        Intent intent = new Intent(context, RegistroArea.class);
        startActivity(intent);
    }

}
