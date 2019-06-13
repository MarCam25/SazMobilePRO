package com.example.saz.saz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSqlServer;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class sas extends AppCompatActivity {
ConexionSqlServer conex=new ConexionSqlServer();

String NombreEmpresa, server, Usuario, base, passw;
TextView txt;
ModeloEmpresa me=new ModeloEmpresa();


ArrayList<String> listaDatos;
RecyclerView recycler;
ConexionBDCliente bdc=new ConexionBDCliente();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sas);

        obtenerDatos();
        recycler=(RecyclerView)findViewById(R.id.recyclerId);
        recycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        listaDatos=new ArrayList<String>();
        obtenerTienda();


    }



    public void obtenerDatos() {
        String empress = getIntent().getStringExtra("Empresa");


        {
            try {
                Statement st = conex.conexionBD().createStatement();
                ResultSet rs = st.executeQuery("select Server, Usuariosvr,PassSvr, basededatos, empresa from logins where idEmpresa= " + empress + " and status=1 and borrado=0");

                while (rs.next()) {

                    me.setServer(rs.getString("Server"));
                    me.setUsuario(rs.getString("Usuariosvr"));
                    me.setPass(rs.getString("PassSvr"));
                    me.setBase(rs.getString("basededatos"));
                    me.setEmpresa(rs.getString("empresa"));

                }


            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error en la consulta", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void obtenerTienda() {
        {
            try {
                Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
                ResultSet rs = st.executeQuery("SELECT Numero, Nombre FROM tiendas ORDER BY Nombre");


                while (rs.next()) {
                 listaDatos.add(rs.getString(1)+" "+rs.getString(2));

                    Toast.makeText(getApplicationContext(), NombreEmpresa, Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error en la consulta", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
