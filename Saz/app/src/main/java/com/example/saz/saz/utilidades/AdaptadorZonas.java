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

import com.example.saz.saz.Area;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloNumeroOrden;
import com.example.saz.saz.Modelo.Zonas;
import com.example.saz.saz.R;

import java.sql.Statement;
import java.util.ArrayList;

public class AdaptadorZonas extends RecyclerView.Adapter<AdaptadorZonas.ViewHolderZonas> {


    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();


    ArrayList<Zonas> listaZonas;
    public AdaptadorZonas(ArrayList<Zonas> listaZonas){this.listaZonas=listaZonas;};


    @Override
    public ViewHolderZonas onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_zonas,null,false);
        return new ViewHolderZonas(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderZonas holder, final int position) {
        holder.idtienda.setText(listaZonas.get(position).getIdTienda());
        holder.nombre.setText(listaZonas.get(position).getNombre());
        holder.idArea.setText(listaZonas.get(position).getIdArea());
        holder.idZona.setText(listaZonas.get(position).getIdZona());
        holder.nombreArea.setText(listaZonas.get(position).getNombreArea());
        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alerta = new AlertDialog.Builder(holder.context );
                alerta.setMessage("Quieres Eliminar ?")
                        .setCancelable(false)
                        .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                eliminar(holder);
                                removeItem(position);
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog titulo=alerta.create();
                titulo.setTitle("Eliminar zona");
                titulo.show();
            }
        });
        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(holder);
                Area.zonaEdicion=holder.nombreArea.getText().toString();
                Area.mostarZonaEdicion();
            }
        });
    }
    public void eliminar(final ViewHolderZonas holder){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="DELETE FROM ZonasDeSurtido  WHERE idZona="+holder.idZona.getText().toString();
            st.executeUpdate(sql);
            Toast.makeText(holder.context, "Zona eliminada", Toast.LENGTH_SHORT).show();



        } catch (Exception e) {

            Toast.makeText(holder.context, "Uups...!!! Nose puede eliminar esta Zona", Toast.LENGTH_SHORT).show();
        }


    }

    public void editar(final ViewHolderZonas holder){
        ModeloNumeroOrden mno=new ModeloNumeroOrden();
        mno.setIdZona(holder.idZona.getText().toString());
        Area.guardar.setEnabled(false);
        Area.zona.setText(holder.nombre.getText());
        Area.actualizar.setEnabled(true);
        Area.zonaEdicion=holder.nombreArea.getText().toString();
    }


    public void removeItem(int position) {
        this.listaZonas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }
    @Override
    public int getItemCount() {
        return listaZonas.size();
    }

    public class ViewHolderZonas extends RecyclerView.ViewHolder {
        TextView nombre,idtienda,idArea,idZona,nombreArea;
        Button eliminar, editar;
        Context context;
        public ViewHolderZonas(@NonNull View itemView) {
            super(itemView);
            nombre=(TextView)itemView.findViewById(R.id.NombreZona);
            idtienda=(TextView)itemView.findViewById(R.id.idTiendaZona);
            idArea=(TextView)itemView.findViewById(R.id.idAreaZona);
            idZona=(TextView)itemView.findViewById(R.id.idZona);
            eliminar=(Button)itemView.findViewById(R.id.eliminarZona);
            editar=(Button)itemView.findViewById(R.id.editarZona);
            nombreArea=(TextView) itemView.findViewById(R.id.NombreArea);
            context=itemView.getContext();
        }
    }
}
