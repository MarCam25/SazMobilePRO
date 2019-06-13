package com.example.saz.saz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;

import com.example.saz.saz.Modelo.DetalleVentas;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloResumen;
import com.example.saz.saz.Modelo.Numero;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.entidades.Comandero;
import com.example.saz.saz.utilidades.AdaptadorVentas;
import com.example.saz.saz.utilidades.AdapterDetalleVenta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DetalleVenta extends AppCompatActivity {
    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();
    Numero numero=new Numero();

    ArrayList<DetalleVentas> listaDetalle;
    RecyclerView recyclerViewDet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_venta);

        listaDetalle=new ArrayList<>();
        recyclerViewDet=(RecyclerView)findViewById(R.id.resicladorDetalle);
        recyclerViewDet.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getDatosProductos();
        AdapterDetalleVenta adapter=new AdapterDetalleVenta(listaDetalle);
        recyclerViewDet.setAdapter(adapter);
    }


    public void getDatosProductos(){

        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select acabado, estilo, talla, color, marca, llave from comanderoDet where numero="+numero.getNumero()+" and [status]=7";
            ResultSet rs = st.executeQuery(sql);
            ModeloResumen mr=null;

            DetalleVentas detalleVentas=null;
            while (rs.next()) {
                detalleVentas=new DetalleVentas();

                detalleVentas.setAcabado(rs.getString(1));
                detalleVentas.setEstilo(rs.getString(2));
                detalleVentas.setPunto(rs.getString(3));
                detalleVentas.setColor(rs.getString(4));
                detalleVentas.setMarca(rs.getString(5));
                detalleVentas.setLlave(rs.getString(6));

                listaDetalle.add(detalleVentas);
            }


        } catch (SQLException e) {

        }


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Principal.location=2;
            Intent intent = new Intent(getApplicationContext(), menu.class);


            startActivity(intent);
        }
        return true;
    }

}
