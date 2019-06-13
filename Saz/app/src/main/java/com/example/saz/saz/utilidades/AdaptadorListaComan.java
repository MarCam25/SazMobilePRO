package com.example.saz.saz.utilidades;

import android.app.ProgressDialog;
import android.content.Context;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloResumen;
import com.example.saz.saz.Principal;
import com.example.saz.saz.R;
import com.example.saz.saz.menu;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class AdaptadorListaComan extends RecyclerView.Adapter<AdaptadorListaComan.ViewHolder> {
    ArrayList<ModeloResumen> listResumen;
    Context context;
    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();
    public AdaptadorListaComan(ArrayList<ModeloResumen> listResumen){this.listResumen=listResumen;}



    @NonNull
    @Override
    public AdaptadorListaComan.ViewHolder onCreateViewHolder( ViewGroup parent, int i) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coman,null,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.estilo.setText(listResumen.get(position).getEstilo());
        holder.numero.setText(listResumen.get(position).getId());
        holder.color.setText(listResumen.get(position).getColor());
        holder.punto.setText(listResumen.get(position).getPunto());
        holder.ubica.setText(listResumen.get(position).getUbicacion());
        holder.llave.setText(listResumen.get(position).getLlave());
        holder.marca.setText(listResumen.get(position).getMarca());
        holder.acabado.setText(listResumen.get(position).getAcabado());

        holder.surtir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setIcon(R.mipmap.ic_launcher);
                progressDialog.setMessage("Surtiendo pedido...");
                progressDialog.show();
              surtir(holder);
              verificarOrden(holder);
              upDate(holder);
                Principal.location=1;
              Intent intent=new Intent(context,menu.class);
              context.startActivity(intent);


            }
        });

    }

    public void verificarOrden(final AdaptadorListaComan.ViewHolder holder){
        String numero=null;

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="SELECT numero FROM comanderoDet WHERE numero="+holder.numero.getText()+" and [status]=0";
            ResultSet rs=st.executeQuery(sql);

            while (rs.next()){
                 numero=rs.getString(1);

            }

            surtirOrdenCompleta(holder,numero);

        } catch (Exception e) {

        }

    }

public void surtirOrdenCompleta(final AdaptadorListaComan.ViewHolder holder, String  numero){
        if(numero==null){
            try {
                Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
                String sql="UPDATE comandero SET [status]=1 WHERE numero="+holder.numero.getText();
                st.executeUpdate(sql);



            } catch (Exception e) {

            }
        }else if(!numero.isEmpty()){

    }

}

    public void surtir(final AdaptadorListaComan.ViewHolder holder){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="UPDATE comanderoDet SET [status]=1 WHERE estilo='"+holder.estilo.getText()+"' AND llave='"+holder.llave.getText()+"'";
            st.executeUpdate(sql);


        } catch (Exception e) {

        }
        Intent lista = new Intent(context, menu.class);
        Principal.location=1;
        context.startActivity(lista);
    }

    @Override
    public int getItemCount() { return listResumen.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView estilo,numero,color,punto,ubica,llave,marca, acabado;
        Button surtir;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            estilo=(TextView)itemView.findViewById(R.id.idEstilo);
            color=(TextView)itemView.findViewById(R.id.idColor);
            punto=(TextView)itemView.findViewById(R.id.idPunto);
            numero=(TextView)itemView.findViewById(R.id.id);
            ubica=(TextView)itemView.findViewById(R.id.idUbica);
            surtir=(Button)itemView.findViewById(R.id.idSurtir);
            llave=(TextView)itemView.findViewById(R.id.idLlave);
            marca=(TextView)itemView.findViewById(R.id.idMarca);
            acabado=(TextView)itemView.findViewById(R.id.idAcabadoo);
            context=itemView.getContext();
        }
    }
    public void upDate(final AdaptadorListaComan.ViewHolder holder){
        try {


            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Update comanderoLog set hora=GETDATE() where numero="+holder.numero.getText();
            st.executeUpdate(sql);


        } catch (Exception e) {

        }
    }
}
