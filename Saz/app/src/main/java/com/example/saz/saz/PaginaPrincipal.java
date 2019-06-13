package com.example.saz.saz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSqlServer;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;





public class PaginaPrincipal extends Fragment{




    ConexionSqlServer conex=new ConexionSqlServer();
    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();
    TextView idTienda;
    ArrayList listaTiendas=new ArrayList();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View root;
    public String numeroTienda;

    private Spinner spTiendas;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_pagina_principal, container, false);
        spTiendas=(Spinner)root.findViewById(R.id.spTiendas);
        idTienda=(TextView)root.findViewById(R.id.idTienda);

        obtenerLineaConexion();
        obtenerTiendas();
        ArrayAdapter<String> adapterTiendas = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listaTiendas);
        spTiendas.setAdapter(adapterTiendas);

        spTiendas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idTienda.setText(spTiendas.getSelectedItem().toString());
                obtenerNumero();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;



    }


    public void obtenerNumero(){
    try {
        Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
        ResultSet rs = st.executeQuery("select numero from tiendas where nombre='"+spTiendas.getSelectedItem()+"'");


        while (rs.next()) {


       numeroTienda=rs.getString(1);


        }

        // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
    } catch (Exception e) {
        Toast.makeText(getActivity(), "Error en sp", Toast.LENGTH_SHORT).show();
    }

}

    private void obtenerTiendas() {
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select  nombre from tiendas");


            while (rs.next()) {

                listaTiendas.add(rs.getString(1));



            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error en sp", Toast.LENGTH_SHORT).show();
        }
    }

    public void obtenerLineaConexion() {
        String empress = getActivity().getIntent().getStringExtra("Empresa");
        me = new ModeloEmpresa();

        {
            try {
                Statement st = conex.conexionBD().createStatement();
                ResultSet rs = st.executeQuery("select  Server, Usuariosvr,PassSvr, basededatos, empresa from logins where idEmpresa= " + empress + " and status=1 and borrado=0 ");

                while (rs.next()) {

                    me.setServer(rs.getString("Server"));
                    me.setUsuario(rs.getString("Usuariosvr"));
                    me.setPass(rs.getString("PassSvr"));
                    me.setBase(rs.getString("basededatos"));
                    me.setEmpresa(rs.getString("empresa"));

                }

                // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Error en la linea de conexion", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
