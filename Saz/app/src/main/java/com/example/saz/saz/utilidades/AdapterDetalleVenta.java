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

import com.example.saz.saz.DetalleVenta;
import com.example.saz.saz.Modelo.DetalleVentas;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloResumen;
import com.example.saz.saz.Principal;
import com.example.saz.saz.R;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.menu;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AdapterDetalleVenta  extends RecyclerView.Adapter<AdapterDetalleVenta.ViewHolderDetalleVenta>  {
    ArrayList<DetalleVentas> listaDetalle;
    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();
    int num;


    public AdapterDetalleVenta(ArrayList<DetalleVentas> listaDetalle) {
        this.listaDetalle = listaDetalle;
    }

    @NonNull
    @Override
    public ViewHolderDetalleVenta onCreateViewHolder( ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detalleventa, null, false);
        return new ViewHolderDetalleVenta(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderDetalleVenta holder, int i) {
        holder.txtEstilo.setText(listaDetalle.get(i).getEstilo());
        holder.txtColor.setText(listaDetalle.get(i).getColor());
        holder.txtMarca.setText(listaDetalle.get(i).getMarca());
        holder.txtAcabado.setText(listaDetalle.get(i).getAcabado());
        holder.txtPunto.setText(listaDetalle.get(i).getPunto());
        holder.txtLlave.setText(listaDetalle.get(i).getLlave());


        holder.btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(holder.context );
                alerta.setMessage("SEGURO DE ELIMINAR ?")
                        .setCancelable(false)
                        .setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                consultarDatos(holder);
                rechazar(holder);
                verificar(holder);


                Intent intent=new Intent(holder.context,DetalleVenta.class);
                holder.context.startActivity(intent);


                            }
                        }).setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog titulo=alerta.create();
                titulo.setTitle("ELIMINAR ORDEN");
                titulo.show();
            }
        });



    }

    public void verificar(final ViewHolderDetalleVenta holder){
        int cont=0;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select numero from comanderodet where numero="+num;
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                cont=rs.getInt(1);
            }

            if(cont==0){
                rechazarComandero(holder);
                rechazarDetalle(holder);
                rechazarLog(holder);
                Principal.location=2;
                Intent intent=new Intent(holder.context,menu.class);
                holder.context.startActivity(intent);
            }

        } catch (SQLException e) {

        }
    }


    public void rechazar(final ViewHolderDetalleVenta holder){
        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="DELETE FROM comanderoDet where llave='"+holder.txtLlave.getText().toString()+"'";
            st.executeUpdate(sql);


        } catch (SQLException e) {

        }
    }

    public void rechazarComandero(final  ViewHolderDetalleVenta holder){
        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="DELETE FROM comandero WHERE numero="+num;
            ResultSet rs = st.executeQuery(sql);
            ModeloResumen mr=null;




        } catch (Exception e) {

        }

    }

    public void rechazarDetalle(final ViewHolderDetalleVenta holder){
        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="DELETE FROM comanderoDet WHERE numero="+num;
            st.executeUpdate(sql);
            ModeloResumen mr=null;




        } catch (Exception e) {

        }
    }

    public void rechazarLog( final ViewHolderDetalleVenta holder){
        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="DELETE FROM comanderoLog WHERE numero="+num;
            st.executeUpdate(sql);
        } catch (Exception e) {

        }
    }

    public void consultarDatos(final ViewHolderDetalleVenta holder ){

        int barcode=0;
        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select barcode,numero from comanderoDet where llave='"+holder.txtLlave.getText().toString()+"'";
            ResultSet rs=st.executeQuery(sql);
            ModeloResumen mr=null;
            while(rs.next()){
                barcode=rs.getInt(1);
                num=rs.getInt(2);
                regresarPedido(barcode, holder);
            }



        } catch (Exception e) {

        }
    }
    public void regresarPedido(int pares,final ViewHolderDetalleVenta holder){
        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="UPDATE existen cantidad=cantidad+1, pedido-1  where llave='"+holder.txtLlave.getText().toString()+"'";
            st.executeUpdate(sql);




        } catch (Exception e) {

        }
    }


    @Override
    public int getItemCount() {
        return listaDetalle.size();
    }


    public class ViewHolderDetalleVenta extends RecyclerView.ViewHolder {
        TextView txtEstilo, txtColor, txtMarca, txtAcabado, txtPunto, txtLlave;
        Button btnRechazar;
        Context context;
        public ViewHolderDetalleVenta(@NonNull View itemView) {
            super(itemView);
            txtEstilo=(TextView)itemView.findViewById(R.id.txtEstilo);
            txtColor=(TextView)itemView.findViewById(R.id.txtColor);
            txtMarca=(TextView)itemView.findViewById(R.id.txtMarca);
            txtAcabado=(TextView)itemView.findViewById(R.id.txtAcabado);
            txtPunto=(TextView)itemView.findViewById(R.id.txtPunto);
            txtLlave=(TextView)itemView.findViewById(R.id.txtLlave);
            btnRechazar=(Button)itemView.findViewById(R.id.btnRechazar);
            context=itemView.getContext();
        }
    }
}
