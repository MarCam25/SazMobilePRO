package com.example.saz.saz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.saz.saz.Modelo.Zonas;


public class Ajustes extends Fragment {

   private Button btnConfiguraciones,altaArea,altaProducto,btnAreaR;
    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root=inflater.inflate(R.layout.fragment_ajustes, container, false);
        btnConfiguraciones=(Button)root.findViewById(R.id.btnConfiguraciones);
        altaArea=(Button)root.findViewById(R.id.idaltaArea);
        altaProducto=(Button)root.findViewById(R.id.altaProducto);
        btnAreaR=(Button)root.findViewById(R.id.btnAreaR);


        btnConfiguraciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent config=new Intent(getActivity(),Configuraciones.class);
                startActivity(config);
            }
        });

        altaArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orden = new Intent(getActivity(), Area.class);
                startActivity(orden);
            }
        });

        altaProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setOnclickListener;
                Intent orden = new Intent(getActivity(), AltaDeProducto.class);
                startActivity(orden);
            }
        });

        btnAreaR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orden = new Intent(getActivity(), RegistroArea.class);
                startActivity(orden);
            }
        });



        return root;
    }

}
