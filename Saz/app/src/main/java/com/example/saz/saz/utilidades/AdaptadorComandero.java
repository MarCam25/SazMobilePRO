package com.example.saz.saz.utilidades;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.saz.saz.ListaComandero;
import com.example.saz.saz.Modelo.ModeloNumeroOrden;
import com.example.saz.saz.R;
import com.example.saz.saz.Resumen;
import com.example.saz.saz.entidades.Comandero;

import java.util.ArrayList;

public class AdaptadorComandero extends RecyclerView.Adapter<AdaptadorComandero.ViewHolderComandero> {
    ArrayList<Comandero> listComandero;


public AdaptadorComandero(ArrayList<Comandero> listComandero){
    this.listComandero=listComandero;
}
    @Override
    public ViewHolderComandero onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comandero, null, false);
        return new ViewHolderComandero(view);

    }


    @Override
    public void onBindViewHolder(final ViewHolderComandero holder,final int position) {

        holder.numero.setText(listComandero.get(position).getNumero());
        holder.cliente.setText(listComandero.get(position).getCliente());
        holder.total.setText(listComandero.get(position).getTotal());
        holder.pares.setText(listComandero.get(position).getPares());
        holder.empleado.setText(listComandero.get(position).getEmpleado());
        holder.idOrden.setText(listComandero.get(position).getIdOrden());

        holder.atender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ModeloNumeroOrden mu=new ModeloNumeroOrden();
                mu.setNumeroOrden(holder.numero.getText().toString());
                //Intent lista = new Intent(holder.context, ListaComandero.class);
                //holder.context.startActivity(lista);
                removeItem(position);
                notifyDataSetChanged();

            }
        });



    }

    public void removeItem(int position) {
        this.listComandero.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }




    @Override
    public int getItemCount() {
        return listComandero.size();
    }

    public class ViewHolderComandero extends RecyclerView.ViewHolder{
        Context context;
        TextView numero, cliente,total, pares,empleado, idOrden, editar;
        Button atender;
        public ViewHolderComandero(@NonNull View itemView) {
            super(itemView);
            numero = (TextView) itemView.findViewById(R.id.NumOrden);
            cliente = (TextView) itemView.findViewById(R.id.cliente);
            total=(TextView)itemView.findViewById(R.id.idTotal);
            pares=(TextView)itemView.findViewById(R.id.idpares);
            empleado=(TextView)itemView.findViewById(R.id.idEmpleado);
            idOrden=(TextView)itemView.findViewById(R.id.idOrden);
            atender=(Button)itemView.findViewById(R.id.idAtender);
            context=itemView.getContext();

        }


    }


}
