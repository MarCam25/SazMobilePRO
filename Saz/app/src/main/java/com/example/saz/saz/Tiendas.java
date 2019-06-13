package com.example.saz.saz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.saz.saz.Modelo.DatosLupita;
import com.example.saz.saz.Modelo.Lupita;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.utilidades.AdaptadorLupita;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Tiendas extends AppCompatActivity {

    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();
    DatosLupita dl=new DatosLupita();
    ArrayList<Lupita> listaLupita;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiendas);

        listaLupita=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recyTiendasId);
        getSupportActionBar().setTitle("SazMobile Pro App  -Todas las tiendas-");


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        llenarRecycler();
        AdaptadorLupita adapterLupita=new AdaptadorLupita(listaLupita);
        recyclerView.setAdapter(adapterLupita);

    }



    public void llenarRecycler(){
        String puntoSp=dl.getPunto();
        String barcode=dl.getBarcode();
        Lupita lupita=null;
        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();

            String sql = "lupita '"+barcode+"',"+puntoSp+",0;";
            ResultSet rs=st.executeQuery(sql);

            while(rs.next()){
                lupita=new Lupita();
                String tienda=rs.getString(1);
                String punto=rs.getString(2);
                lupita.setTienda(tienda);
                lupita.setPunto(punto);
                listaLupita.add(lupita);

            }



        } catch (SQLException e){
            e.getMessage();
            Toast.makeText(this, "No hay este producto en otras tiendas", Toast.LENGTH_LONG).show();
        }
    }

}
