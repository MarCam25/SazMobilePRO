package com.example.saz.saz.utilidades;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.saz.saz.existencia;

import java.util.ArrayList;

public class AdaptadorExistencias extends RecyclerView.Adapter<AdaptadorExistencias.ViewHolderExistencias> {

ArrayList<existencia> listaExistencia;

    public AdaptadorExistencias(ArrayList<existencia> listaExistencia) {
        this.listaExistencia = listaExistencia;
    }

    @NonNull
    @Override
    public AdaptadorExistencias.ViewHolderExistencias onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorExistencias.ViewHolderExistencias viewHolderExistencias, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolderExistencias extends RecyclerView.ViewHolder {
        public ViewHolderExistencias(@NonNull View itemView) {
            super(itemView);
        }
    }
}
