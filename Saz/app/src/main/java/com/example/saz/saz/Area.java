package com.example.saz.saz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloNumeroOrden;
import com.example.saz.saz.Modelo.Zonas;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.utilidades.AdaptadorZonas;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Area extends AppCompatActivity {


    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();

    public static Spinner Areas;


    public static Button guardar,actualizar;
    public static EditText zona;
    public static ArrayList listaArea=new ArrayList();
    public static String zonaEdicion;

    ArrayList<Zonas> listaZonas;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        guardar=(Button)findViewById(R.id.idGuardar);
        zona=(EditText) findViewById(R.id.idZonaEd);
        Areas=(Spinner)findViewById(R.id.ListaArea);
        actualizar=(Button)findViewById(R.id.idActualizarZona);

        actualizar.setEnabled(false);
        guardar.setEnabled(true);


        listaZonas=new ArrayList();

        recyclerView=(RecyclerView)findViewById(R.id.recyZonas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getZonas();
        AdaptadorZonas adaptador=new AdaptadorZonas(listaZonas);

        recyclerView.setAdapter(adaptador);






        Areas.setAdapter(null);
        listaArea.clear();
        llenarListaAreas();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spinner_item, listaArea);
        Areas.setAdapter(adapter);
        Areas.setSelection(obtenerPosicionItem(Areas,zonaEdicion));

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zona.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(getApplication(), "El campo esta vacío", Toast.LENGTH_LONG);
                    TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                    x.setTextColor(Color.YELLOW); toast.show();
                }else{
                    veificar();
                    zona.setText(null);

                }






            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(zona.getText().toString().isEmpty()){
                    Toast toast = Toast.makeText(getApplication(), "El campo esta vacío", Toast.LENGTH_LONG);
                    TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                    x.setTextColor(Color.YELLOW); toast.show();

                }else{
                    veificarUpdate();
                    zona.setText(null);

                }
            }
        });
    }





    public void update(){
        ModeloNumeroOrden mno=new ModeloNumeroOrden();
        String tienda=getTienda();
        String idArea =getNumeroArea(Areas.getSelectedItem().toString());
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="UPDATE ZonasDeSurtido SET nombre='"+zona.getText()+"', idArea="+idArea+ " WHERE idTienda="+tienda+ " and idZona="+mno.getIdZona() ;
            st.executeUpdate(sql);
            st.close();


        } catch (Exception e) {

            Toast toast = Toast.makeText(getApplication(), "Error al Registrar Zona", Toast.LENGTH_LONG);
            TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
            x.setTextColor(Color.YELLOW); toast.show();
        }
    }


    public void RegistrarZona(){

        String tienda=getTienda();
        String idArea =getNumeroArea(Areas.getSelectedItem().toString());
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="INSERT INTO ZonasDeSurtido  (idTienda,nombre,idArea) VALUES ('"+tienda+"','"+zona.getText()+"',"+idArea+");";
            st.executeUpdate(sql);
            st.close();


        } catch (Exception e) {


            Toast toast = Toast.makeText(getApplication(), "Error al Registrar Zona", Toast.LENGTH_LONG);
            TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
            x.setTextColor(Color.YELLOW); toast.show();
        }
    }

    public String getNumeroArea(String nombreArea){
        String numeroArea=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select idArea from AreasDeControl WHERE nombre='"+nombreArea+"'";
            ResultSet rs=st.executeQuery(sql);

            while(rs.next()){
                numeroArea=rs.getString(1);
            }
            st.close();

        } catch (Exception e) {

            Toast.makeText(this, "Error al obtener id del área", Toast.LENGTH_SHORT).show();
            Toast toast = Toast.makeText(getApplication(), "", Toast.LENGTH_LONG);
            TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
            x.setTextColor(Color.YELLOW); toast.show();
        }

        return numeroArea;
    }
    public void llenarListaAreas(){
        String tienda=getTienda();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="SELECT nombre FROM areasdecontrol WHERE idtienda="+tienda;
            ResultSet rs=st.executeQuery(sql);

            while(rs.next()){

                listaArea.add(rs.getString(1));

            }
            st.close();


        } catch (Exception e) {

            Toast toast = Toast.makeText(getApplication(), "Error al llenar la lista de áreas", Toast.LENGTH_LONG);
            TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
            x.setTextColor(Color.YELLOW); toast.show();


        }
    }


    public void veificar(){
        String zonas=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select nombre from Zonasdesurtido where nombre='"+zona.getText()+"' ";
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){


                zonas=rs.getString(1);

            }
            st.close();


        } catch (Exception e) {


            Toast toast = Toast.makeText(getApplication(), "Error al verificar", Toast.LENGTH_LONG);
            TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
            x.setTextColor(Color.YELLOW); toast.show();
        }
        if(zonas!=null){

            Toast toast = Toast.makeText(getApplication(), "No se puede guardar el registro porque ya existe una ZONA con el mismo nombre. ", Toast.LENGTH_LONG);
            TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
            x.setTextColor(Color.YELLOW); toast.show();
        }else{
            RegistrarZona();
            Toast toast = Toast.makeText(getApplication(), "Se ha creado un nuevo registro", Toast.LENGTH_LONG);
            TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
            x.setTextColor(Color.YELLOW); toast.show();
            actualizarPagina();
           // adaptador.notifyDataSetChanged();

        }
    }
    public void veificarUpdate() {
        String zonas = null;
        int idZona = 0;

        String tienda = getTienda();

        try {
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "select nombre,idZona from Zonasdesurtido where nombre='" + zona.getText() + "' and idtienda=" + tienda;
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {


                zonas = rs.getString(1);
                idZona = rs.getInt(2);


            }
            st.close();


        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplication(), "Error al verificar", Toast.LENGTH_LONG);
            TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
            x.setTextColor(Color.YELLOW); toast.show();
        }
        ModeloNumeroOrden mno=new ModeloNumeroOrden();

        if(Integer.parseInt(mno.getIdZona())==idZona){
            update();

            Toast toast = Toast.makeText(getApplication(), "Zona actualizada", Toast.LENGTH_LONG);
            TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
            x.setTextColor(Color.YELLOW); toast.show();

           actualizarPagina();
           // adaptador.notifyDataSetChanged();
            }else{
            if (zonas != null) {

                Toast toast = Toast.makeText(getApplication(), "No se puede guardar el registro porque ya existe una ZONA con el mismo nombre. ", Toast.LENGTH_LONG);
                TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                x.setTextColor(Color.YELLOW); toast.show();
            } else {

                update();
                Toast toast = Toast.makeText(getApplication(), "Zona actualizada", Toast.LENGTH_LONG);
                TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                x.setTextColor(Color.YELLOW); toast.show();


               actualizarPagina();

            }
        }
    }


    public String getTienda(){
        String tienda =null;
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);

        SQLiteDatabase db = conn.getReadableDatabase();




        String sql = "SELECT nombreT from tienda where id=1";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
          tienda=cursor.getString(0);


        }

        return tienda;
    }



    public void getZonas(){
        String tienda=getTienda();
        Zonas zonas=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="  select idZona, ZonasDeSurtido.idTienda, ZonasDeSurtido.nombre, ZonasDeSurtido.idArea,AreasDeControl.nombre from ZonasDeSurtido inner join AreasDeControl on ZonasDeSurtido.idArea=AreasDeControl.idArea where ZonasDeSurtido.idTienda= "+tienda;
            ResultSet rs = st.executeQuery(sql);


            while(rs.next()){
                zonas=new Zonas();
                zonas.setIdZona(rs.getString(1));
                zonas.setIdTienda(rs.getString(2));
                zonas.setNombre(rs.getString(3));
                zonas.setIdArea(rs.getString(4));
                zonas.setNombreArea(rs.getString(5));


                listaZonas.add(zonas);
            }
            st.close();


        } catch (Exception e) {

            Toast.makeText(this, "Error al Mostrar Zonas", Toast.LENGTH_SHORT).show();
            Toast toast = Toast.makeText(getApplication(), "Erroal  mostrar zonas", Toast.LENGTH_LONG);
            TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
            x.setTextColor(Color.YELLOW); toast.show();
        }
    }
    public void actualizarPagina(){
        Intent orden = new Intent(this, Area.class);
        startActivity(orden);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Principal.location=3;
            Intent intent = new Intent(getApplicationContext(), menu.class);



            startActivity(intent);
        }
        return true;
    }

    public static void mostarZonaEdicion(){

        Areas.setSelection(obtenerPosicionItem(Areas,zonaEdicion));

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

}
