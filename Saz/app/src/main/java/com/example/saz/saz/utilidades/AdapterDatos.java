package com.example.saz.saz.utilidades;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import com.example.saz.saz.ConsultaF;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloNumeroOrden;
import com.example.saz.saz.OrdenEspera;
import com.example.saz.saz.OrdenesEditar;
import com.example.saz.saz.R;
import com.example.saz.saz.entidades.Comandero;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> {
    String idNUmero;
    String[] file;
    ArrayList<Comandero> listComandero;
    String norden;
    String hora;


    public AdapterDatos(ArrayList<Comandero> listComandero) {

        this.listComandero = listComandero;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null, false);

        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolderDatos holder, int position) {
        holder.numero.setText(listComandero.get(position).getNumero());
        holder.cliente.setText(listComandero.get(position).getCliente());
        holder.total.setText(listComandero.get(position).getTotal());
        holder.pares.setText(listComandero.get(position).getPares());
        holder.empleado.setText(listComandero.get(position).getEmpleado());
        holder.idOrden.setText(listComandero.get(position).getIdOrden());




        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(holder.context);
                progressDialog.setIcon(R.mipmap.ic_launcher);
                progressDialog.setMessage("Cargando...");
                progressDialog.show();
                ModeloNumeroOrden mno=new ModeloNumeroOrden();
                mno.setNumeroOrden(holder.idOrden.getText().toString());

                Intent intent = new Intent(holder.context, OrdenesEditar.class);

                holder.context.startActivity(intent);
            }
        });

        holder.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(holder.context);
                progressDialog.setIcon(R.mipmap.ic_launcher);
                progressDialog.setMessage("Cargando...");
                progressDialog.show();

                Toast toast = Toast.makeText(holder.context, "La orden finalizó y pasó al comandero", Toast.LENGTH_LONG);
                TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                x.setTextColor(Color.YELLOW); toast.show();

                //updateUbicacion(holder);
                ConsultarHora();
                ConexionSQLiteHelper conn = new ConexionSQLiteHelper(holder.context, "db tienda", null, 1);

                SQLiteDatabase db = conn.getReadableDatabase();

                Comandero comandero = null;


                String sql = "SELECT * FROM orden WHERE norden LIKE'%" + holder.numero.getText() + "%';";
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    String status=cursor.getString(1);
                    String cliente=cursor.getString(2);
                    String empleado=cursor.getString(3);
                    norden=cursor.getString(4);
                    String total=cursor.getString(5);
                    String pares=cursor.getString(6);
                    String impreso=cursor.getString(7);
                    insertComandero(status, cliente, empleado, norden, total, pares, impreso);




                }
                insertarComanderoLog();
               consultarPedidos(holder);
                update(holder);
                cursor.close();
                db.close();

                Intent intent = new Intent(holder.context, OrdenEspera.class);
                holder.context.startActivity(intent);


            }


        });


        holder.cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(holder.context );
                alerta.setMessage("SEGURO DE ELIMINAR ?")
                        .setCancelable(false)
                        .setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                consultarDevoluciones(holder);
                                cancelar(holder);
                                Intent intent = new Intent(holder.context, OrdenEspera.class);
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
    public void consultarDevoluciones(final ViewHolderDatos holder){

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(holder.context, "db tienda", null, 1);

        SQLiteDatabase db = conn.getReadableDatabase();




        String sql = "SELECT cantidad, barcode, talla FROM pedido WHERE idOrden=" + holder.idOrden.getText();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {

          String cant=cursor.getString(0);
          String barcode=cursor.getString(1);
          String talla=cursor.getString(2);
          devolver(cant, barcode, talla);

        }

    }

/*
    public void updateUbicacion(final ViewHolderDatos holder){
        ubicacionEstilo()

        try {
            ConexionBDCliente bdc=new ConexionBDCliente();
            ModeloEmpresa me=new ModeloEmpresa();

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String query="update comanderoDet set ubicacion='' where numero=";
            st.executeUpdate(query);



        } catch (Exception e) {

        }
    }


    public String ubicacionEstilo(String estilo){
        try {
            ConexionBDCliente bdc=new ConexionBDCliente();
            ModeloEmpresa me=new ModeloEmpresa();

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String query="update comanderoDet set ubicacion='' where numero=";
            st.executeUpdate(query);



        } catch (Exception e) {

        }


*/




    public void devolver(String cant, String barcode, String talla){
        try {
            ConexionBDCliente bdc=new ConexionBDCliente();
            ModeloEmpresa me=new ModeloEmpresa();

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
          String query="update existen set cantreal = cantreal - " +cant + " ,  pedido = pedido - " + cant + " where barcode ='" + barcode+ "' and talla = " + talla + " and tienda = " + ConsultaF.listado;
            st.executeUpdate(query);



        } catch (Exception e) {

        }

    }

    public void update(final ViewHolderDatos holder) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(holder.context, "db tienda", null, 1);


        SQLiteDatabase db = conn.getReadableDatabase();





       db.execSQL("DELETE FROM orden WHERE norden LIKE '%" + holder.numero.getText() + "%'");


        db.close();

}


    public void insertComandero(String status,String cliente,String empleado,String norden,String total,String pares,String impreso){

        ConexionBDCliente bdc=new ConexionBDCliente();
        ModeloEmpresa me=new ModeloEmpresa();

            try {
                Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
                String query="INSERT INTO comandero(numero,Tienda,Cliente,fecha,total,[status],pares,empleado,impreso,Llave) VALUES ('"+norden+"',"+ConsultaF.listado+",'"+cliente+"',getDate(),"+total+","+status+","+pares+",'"+empleado+"',"+impreso+",newId())";
                ResultSet rs = st.executeQuery(query);


                while (rs.next()) {





                }


            } catch (Exception e) {


            }



    }
    public void insertarComanderoLog(){
        try {
            ConexionBDCliente bdc=new ConexionBDCliente();
            ModeloEmpresa me=new ModeloEmpresa();
            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();

            String sql="insert into ComanderoLog(numero, fecha, hora, llave) values ('"+norden+"',getDate(),getDate(),newId())";
            st.executeUpdate(sql);


        } catch (SQLException e) {

        }

    }
    public void insertarComanderoDeta(final ViewHolderDatos holder,String numero,String tienda,String barcode,String estilo,String color,String acabado,String talla,String status,String marca,String ubicacion){

        ConexionBDCliente bdc=new ConexionBDCliente();
        ModeloEmpresa me=new ModeloEmpresa();

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql= "insert into comanderoDet (numero,tienda,barcode,estilo,color,marca,acabado,talla,[status],ubicacion,llave) values ('"+numero+"',"+tienda+",'"+barcode+"','"+estilo+"','"+color+"','"+marca+"','"+acabado+"',"+talla+","+status+",'"+ubicacion+"',newId())";
                    st.executeUpdate(sql);


        } catch (Exception e) {
           Toast.makeText(holder.context, "Error comanderoDet", Toast.LENGTH_SHORT).show();
        }

    }
    private void ConsultarHora() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());

        Date date=new Date();
        hora=dateFormat.format(date);


    }
    public void consultarPedidos(final ViewHolderDatos holder){

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(holder.context, "db tienda", null, 1);

        SQLiteDatabase db = conn.getReadableDatabase();




        String sql = "SELECT estilo, talla, barcode, acabado, color,cantidad, marca,ubicacion FROM pedido WHERE idOrden=" + holder.idOrden.getText();
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String estilo=cursor.getString(0);
            String talla=cursor.getString(1);
            String barcode=cursor.getString(2);
            String acabado=cursor.getString(3);
            String color=cursor.getString(4);
            int cantidad=cursor.getInt(5);
            String marca=cursor.getString(6);
            String ubicacion=cursor.getString(7);


for(int i=0;i<cantidad;i++) {
    insertarComanderoDeta(holder, norden, ConsultaF.listado, barcode, estilo, color, acabado, talla, "0",marca,ubicacion);

}
        }

    }



    public void cancelar(final ViewHolderDatos holder){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(holder.context, "db tienda", null, 1);


        SQLiteDatabase db = conn.getReadableDatabase();





        db.execSQL("DELETE FROM orden WHERE norden LIKE '%" + holder.numero.getText() + "%'");


        db.close();

    }




    @Override
    public int getItemCount()
    {
        return listComandero.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        Context context;
        TextView numero, cliente,total, pares,empleado, idOrden, editar;
        Button finish, cancelar;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            numero = (TextView) itemView.findViewById(R.id.NumOrden);
            cliente = (TextView) itemView.findViewById(R.id.cliente);
            total=(TextView)itemView.findViewById(R.id.idTotal);
            pares=(TextView)itemView.findViewById(R.id.idpares);
            finish=(Button)itemView.findViewById(R.id.idFinal);
            empleado=(TextView)itemView.findViewById(R.id.idEmpleado);
            cancelar=(Button)itemView.findViewById(R.id.idCancelar);
            idOrden=(TextView)itemView.findViewById(R.id.idOrden);
            editar=(Button)itemView.findViewById(R.id.idEditar);
            context=itemView.getContext();


        }

    }
}
