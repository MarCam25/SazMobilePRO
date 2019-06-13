package com.example.saz.saz.utilidades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.saz.saz.RegistroArea;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.Configuracion;
import com.example.saz.saz.Modelo.Areas;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloNumeroOrden;
import com.example.saz.saz.R;

import java.sql.Statement;
import java.util.ArrayList;

public class AdaptadorAreas extends  RecyclerView.Adapter<AdaptadorAreas.ViewHolderAreas> {
    public static boolean update=false;
    ArrayList<Areas> listaAreas;
    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();

   public AdaptadorAreas(ArrayList<Areas> listaAreas){ this.listaAreas=listaAreas; };


    @Override
    public ViewHolderAreas onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_areas, null, false);
        return new ViewHolderAreas(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderAreas holder, int position) {
        holder.idtienda.setText(listaAreas.get(position).getIdTienda());
        holder.nombre.setText(listaAreas.get(position).getNombreArea());
        holder.idArea.setText(listaAreas.get(position).getIdArea());

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(holder.context );
                alerta.setMessage("¿Seguro que quieres eliminar esta área? Si lo haces, se eliminarán las zonas asignadas a esta área.")
                        .setCancelable(false)
                        .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                eliminar(holder);
                                Intent intent=new Intent(holder.context,RegistroArea.class);
                                holder.context.startActivity(intent);

                            }
                        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog titulo=alerta.create();
                titulo.setTitle("Eliminar");
                titulo.show();


            }
        });


        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(holder);
            }
        });


    }

    public void eliminar(final ViewHolderAreas holder){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="DELETE FROM AreasDeControl WHERE idArea="+holder.idArea.getText().toString();
            st.executeUpdate(sql);
            Toast.makeText(holder.context, "Área eliminada", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {

            Toast.makeText(holder.context, "Uups...!!! Nose puede eliminar esta Area", Toast.LENGTH_SHORT).show();
        }
    }

    public void eliminarZonas(final ViewHolderAreas holder){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="DELETE FROM ZonasDeSurtido WHERE idArea="+holder.idArea.getText().toString();
            st.executeUpdate(sql);
            Toast.makeText(holder.context, "Área eliminada", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {

            Toast.makeText(holder.context, "Uups...!!! Nose puede eliminar esta Area", Toast.LENGTH_SHORT).show();
        }
    }

    public void editar(final ViewHolderAreas holder){
        ModeloNumeroOrden mno=new ModeloNumeroOrden();
        update=true;
        RegistroArea.guardar.setEnabled(false);
        RegistroArea.zona.setText(holder.nombre.getText());
        RegistroArea.actualizar.setEnabled(true);
        mno.setIdArea(holder.idArea.getText().toString());


    }


        public void actualizarPagina(final ViewHolderAreas holder){
            Intent orden = new Intent(holder.context, Configuracion.class);
            holder.context.startActivity(orden);
        }





    @Override
    public int getItemCount() {
        return listaAreas.size();
    }

    public class ViewHolderAreas extends RecyclerView.ViewHolder {
        TextView nombre,idtienda,idArea;
        Button  eliminar, editar;
        Context context;

        public ViewHolderAreas(@NonNull View itemView) {
            super(itemView);
            nombre=(TextView)itemView.findViewById(R.id.NombreArea);
            idtienda=(TextView)itemView.findViewById(R.id.idTienda);
            idArea=(TextView)itemView.findViewById(R.id.idArea);
            eliminar=(Button)itemView.findViewById(R.id.eliminarArea);
            editar=(Button)itemView.findViewById(R.id.editarArea);
            context=itemView.getContext();



        }
    }
}
