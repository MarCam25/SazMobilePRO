package com.example.saz.saz.utilidades;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.saz.saz.Modelo.Lupita;
import com.example.saz.saz.R;


import java.util.ArrayList;

public class AdaptadorLupita extends RecyclerView.Adapter<AdaptadorLupita.ViewHolderLupita> {

    ArrayList<Lupita> listaLupita;

    public AdaptadorLupita(ArrayList<Lupita> listaLupita) {
        this.listaLupita = listaLupita;
    }


    @Override
    public ViewHolderLupita onCreateViewHolder( ViewGroup parent , int i) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lupita,null,false);
        return new ViewHolderLupita(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderLupita holder, int position) {

        holder.tiendaTXT.setText(listaLupita.get(position).getTienda());
        holder.puntoTXT.setText(listaLupita.get(position).getPunto());

    }

    @Override
    public int getItemCount() {
        return listaLupita.size();
    }

    public class ViewHolderLupita extends RecyclerView.ViewHolder {
        TextView tiendaTXT, puntoTXT;
        public ViewHolderLupita(@NonNull View itemView) {
            super(itemView);

            tiendaTXT=(TextView)itemView.findViewById(R.id.tiendatxt);
            puntoTXT=(TextView)itemView.findViewById(R.id.puntotxt);
        }
    }


}
