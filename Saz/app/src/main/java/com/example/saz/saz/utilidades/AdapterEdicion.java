package com.example.saz.saz.utilidades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.Consulta;
import com.example.saz.saz.ConsultaF;
import com.example.saz.saz.Imagen;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloImagen;
import com.example.saz.saz.Modelo.ModeloNumeroOrden;
import com.example.saz.saz.Modelo.ModeloResumen;
import com.example.saz.saz.Modelo.Zapato;
import com.example.saz.saz.OrdenEspera;
import com.example.saz.saz.OrdenesEditar;
import com.example.saz.saz.R;

import java.sql.Statement;
import java.util.ArrayList;

public class AdapterEdicion extends RecyclerView.Adapter<AdapterEdicion.ViewHolderEdicion> {

    ArrayList<ModeloResumen> listResumen;
    Context context;

    public AdapterEdicion(ArrayList<ModeloResumen> listResumen) {
        this.listResumen = listResumen;
    }

    @NonNull
    @Override
    public AdapterEdicion.ViewHolderEdicion onCreateViewHolder(ViewGroup parent, int i) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edicion,null,false);
        return new ViewHolderEdicion(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolderEdicion holder, final int position) {
        holder.estilo.setText(listResumen.get(position).getEstilo());
        holder.marca.setText(listResumen.get(position).getMarca());
        holder.color.setText(listResumen.get(position).getColor());
        holder.punto.setText(listResumen.get(position).getPunto());
        holder.cant.setText(listResumen.get(position).getCantidad());
        holder.sub.setText(listResumen.get(position).getSubtotal());
        holder.total.setText(listResumen.get(position).getTotal());
        holder.imagen.setText(listResumen.get(position).getImagen());
        holder.id.setText(listResumen.get(position).getId());
        holder.bar.setText(listResumen.get(position).getBarcode());
        holder.idOrden.setText(listResumen.get(position).getIdOrden());
        holder.acabado.setText(listResumen.get(position).getAcabado());
        holder.corrida.setText(listResumen.get(position).getCorrida());



        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                                Zapato zapato= new Zapato();
                                zapato.setEstiloE(holder.estilo.getText().toString());
                                zapato.setColorE(holder.color.getText().toString());
                                zapato.setAcabadoE(holder.acabado.getText().toString());
                                zapato.setMarcaE(holder.marca.getText().toString());
                                zapato.setCantidadE(holder.cant.getText().toString());
                                zapato.setPuntoE(holder.punto.getText().toString());
                                zapato.setCorridaE(holder.corrida.getText().toString());
                                zapato.setPrecio(holder.total.getText().toString());
                                zapato.setId(holder.id.getText().toString());

                                ModeloNumeroOrden mno=new ModeloNumeroOrden();
                                mno.setEstilo(holder.estilo.getText().toString());
                                mno.setNumeroOrden(holder.idOrden.getText().toString());



                                Intent intent = new Intent(context, Consulta.class);
                                context.startActivity(intent);





            }
        });

        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModeloImagen mi=new ModeloImagen();

                mi.setIdImagen(holder.imagen.getText().toString());
                Intent intent=new Intent(context,Imagen.class);
                context.startActivity(intent);
            }
        });

        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(context );
                alerta.setMessage("SEGURO DE ELIMINAR ?")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                regresarExistencias(holder);


                                eliminar(holder);
                                int id=verificar(holder);
                                if(id==0){
                                    eliminarOrdenCompleta(holder);
                                    Intent intent=new Intent(context,OrdenEspera.class);
                                    context.startActivity(intent);
                                }
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
                titulo.setTitle("ELIMINAR ORDEN");
                titulo.show();




            }
        });

    }

    public void removeItem(int position) {
        this.listResumen.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }

    public int verificar(final ViewHolderEdicion holder){
        int id=0;

    ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
    SQLiteDatabase db = conn.getReadableDatabase();

String sql="SELECT id FROM " + Utilidades.TABLA_PEDIDO +" WHERE idOrden="+holder.idOrden.getText();
    Cursor cursor = db.rawQuery(sql , null);
    while (cursor.moveToNext()) {
       id=cursor.getInt(0);


    }



        return id;
}

public void eliminarOrdenCompleta(final ViewHolderEdicion holder){
    ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);


    SQLiteDatabase db = conn.getReadableDatabase();
    String sql="DELETE FROM orden WHERE idOrden="+holder.idOrden.getText();
    db.execSQL(sql);


    db.close();
}


    public void regresarExistencias(final AdapterEdicion.ViewHolderEdicion holder){
        try {
            ModeloEmpresa me=new ModeloEmpresa();
            ConexionBDCliente bdc=new ConexionBDCliente();

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql="update existen set cantreal = cantreal - " + holder.cant.getText() + " ,  pedido = pedido - " + holder.cant.getText() + " where barcode ='" + holder.bar.getText() + "' and talla = " + holder.punto.getText() + " and tienda = " + ConsultaF.listado;
            st.executeUpdate(sql);

        }catch (Exception e) {
            Toast.makeText(context, "Error al finalizar el pedido", Toast.LENGTH_SHORT).show();
        }
    }




    public void eliminar(final AdapterEdicion.ViewHolderEdicion holder){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);


        SQLiteDatabase db = conn.getReadableDatabase();

        db.execSQL("DELETE FROM pedido WHERE id="+holder.id.getText());


        db.close();
    }








    @Override
    public int getItemCount() {
        return listResumen.size();
    }

    public class ViewHolderEdicion extends RecyclerView.ViewHolder {

        TextView estilo, marca, color, punto, cant,sub,total,bar,id, imagen, idOrden,acabado,corrida;

        Button foto,eliminar,editar;

        public ViewHolderEdicion(@NonNull View itemView) {
            super(itemView);

            estilo=(TextView)itemView.findViewById(R.id.idEstilo);
            marca=(TextView)itemView.findViewById(R.id.idMarca);
            color=(TextView)itemView.findViewById(R.id.idColor);
            punto=(TextView)itemView.findViewById(R.id.idPunto);
            cant=(TextView)itemView.findViewById(R.id.idCantidad);
            sub=(TextView)itemView.findViewById(R.id.idSubTotal);
            total=(TextView)itemView.findViewById(R.id.idTotal);
            bar=(TextView)itemView.findViewById(R.id.idBar);
            foto=(Button)itemView.findViewById(R.id.idFoto);
            id=(TextView)itemView.findViewById(R.id.id);
            eliminar=(Button)itemView.findViewById(R.id.idEliminar);
            imagen=(TextView)itemView.findViewById(R.id.idImagen);
            editar=(Button)itemView.findViewById(R.id.idEditar);
            idOrden=(TextView)itemView.findViewById(R.id.idOrden);
            acabado=(TextView)itemView.findViewById(R.id.idSubAcabadoE);
            corrida=(TextView)itemView.findViewById(R.id.idCorrida);
            context=itemView.getContext();

        }
    }
}
