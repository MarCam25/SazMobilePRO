package com.example.saz.saz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloNumeroOrden;
import com.example.saz.saz.Modelo.ModeloResumen;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.utilidades.AdaptadorListaComan;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ListaComandero extends AppCompatActivity {

    RecyclerView recyclerView;

    ArrayList<ModeloResumen> listaResumen;
    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_comandero);

        listaResumen=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.reId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        seleccionarOrden();
        AdaptadorListaComan adapter=new AdaptadorListaComan(listaResumen);
        recyclerView.setAdapter(adapter);




    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent=new Intent(getApplicationContext(),menu.class);

            startActivity(intent);
        }
        return true;
    }



    public void seleccionarOrden(){

        try {
            ModeloNumeroOrden mu=new ModeloNumeroOrden();
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="SELECT numero,estilo,color,talla,ubicacion,llave FROM comanderoDet WHERE numero= "+mu.getNumeroOrden()+" AND [status]="+0;
            ResultSet rs = st.executeQuery(sql);
            ModeloResumen mr=null;

            while (rs.next()) {
                mr=new ModeloResumen();

                mr.setId(rs.getString(1));
                mr.setEstilo(rs.getString(2));
                mr.setColor(rs.getString(3));
                mr.setPunto(rs.getString(4));
                mr.setUbicacion(rs.getString(5));
                mr.setLlave(rs.getString(6));








                listaResumen.add(mr);









            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "error al seleccionar ", Toast.LENGTH_SHORT).show();
        }

    }




}
