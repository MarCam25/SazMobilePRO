package com.example.saz.saz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.utilidades.Utilidades;

import java.sql.ResultSet;
import java.sql.Statement;

public class PrimerRegistroArea extends AppCompatActivity {


    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();

    Button guardar,altaArea,altaProducto;
    EditText zona;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer_registro_area);


        zona=(EditText)findViewById(R.id.idZona);
        guardar=(Button)findViewById(R.id.idGuardar);
        altaArea=(Button)findViewById(R.id.idaltaArea);
        altaProducto=(Button)findViewById(R.id.altaProducto);

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(zona.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"El campo esta vacio",Toast.LENGTH_LONG).show();
                }else{

                    veificar();
                    zona.setText(null);

                }


            }
        });


    }



    public void veificar(){
        String zonas=null;
        String idTienda=getTienda();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select nombre from AreasDeControl where nombre='"+zona.getText()+"' and idTienda="+idTienda;
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){


                zonas=rs.getString(1);

            }
            st.close();


        } catch (Exception e) {

            Toast.makeText(this, "Error al verificar", Toast.LENGTH_SHORT).show();
        }
        if(zonas!=null){
            Toast.makeText(this, "Ya esta esa Area con el mismo nombre ", Toast.LENGTH_SHORT).show();
        }else{
            darAlta();
            Toast.makeText(this, "Se a creado un nuevo registro", Toast.LENGTH_SHORT).show();
            Intent inte= new Intent(getApplicationContext(), ActPrincipal.class);
            startActivity(inte);

        }
    }
    public void darAlta(){

        String tienda=getTienda();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="insert into AreasDeControl (idTienda,nombre)values('"+tienda+"','"+zona.getText()+"');";
            st.executeUpdate(sql);
            st.close();


        } catch (Exception e) {

            Toast.makeText(this, "Error al registrar areax", Toast.LENGTH_SHORT).show();
        }

    }


    public String  getTienda(){
        String tienda="";
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();


        Cursor cursor = db.rawQuery("SELECT nombreT FROM " + Utilidades.TABLA_TIENDA , null);
        while (cursor.moveToNext()) {
            tienda =(cursor.getString(0));



        }
        return tienda;
    }
}
