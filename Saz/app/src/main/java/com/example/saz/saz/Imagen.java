package com.example.saz.saz;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloImagen;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSqlServer;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Statement;

public class Imagen extends AppCompatActivity {

    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();
    ConexionSqlServer conex=new ConexionSqlServer();
    ModeloImagen mi=new ModeloImagen();
    String idImagen;
ImageView imagenView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen);
        imagenView=(ImageView)findViewById(R.id.idImagen);
        idImagen=mi.getIdImagen();
        getImagen();

    }


    public void getImagen(){

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String query="select imagen from imagenes where id="+idImagen;
            ResultSet rs = st.executeQuery(query);


            while (rs.next()) {
                Blob test=rs.getBlob(1);
                int blobLength = (int) test.length();
                byte[] blobAsBytes = test.getBytes(1, blobLength);
                Bitmap bmap=BitmapFactory.decodeByteArray(blobAsBytes, 0 ,blobAsBytes.length);
                imagenView.setImageBitmap(bmap);
            }

            st.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al mostrar la imagen ", Toast.LENGTH_SHORT).show();
        }

    }


}
