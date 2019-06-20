package com.example.saz.saz.utilidades;

import android.app.AlertDialog;
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

import com.example.saz.saz.OrdenEspera;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.ConsultaF;
import com.example.saz.saz.Imagen;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloImagen;
import com.example.saz.saz.Modelo.ModeloResumen;
import com.example.saz.saz.R;
import com.example.saz.saz.Resumen;
import com.example.saz.saz.conexion.ConexionSqlServer;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdapterResumen extends RecyclerView.Adapter<AdapterResumen.ViewHolderResumen>{

    ArrayList<ModeloResumen> listResumen;
    Context context;

    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();
    ConexionSqlServer conex=new ConexionSqlServer();
    int existencias=0;

    public AdapterResumen(ArrayList<ModeloResumen> listResumen) {
        this.listResumen = listResumen;
    }

    @NonNull
    @Override
    public AdapterResumen.ViewHolderResumen onCreateViewHolder( ViewGroup parent, int i) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resumen,null,false);
        return new ViewHolderResumen(view);
    }




    @Override
    public void onBindViewHolder(final ViewHolderResumen holder, int position) {
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
        holder.acabado.setText(listResumen.get(position).getAcabado());


    /*    holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModeloNumeroOrden mno=new ModeloNumeroOrden();
                mno.setEstilo(holder.estilo.getText().toString());
                regresarExistencias(holder);
                eliminarPedido(holder);


                Intent intent = new Intent(context, Consulta.class);
                context.startActivity(intent);
            }
        });*/

        holder.foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModeloImagen mi=new ModeloImagen();
                mi.setIdImagen(holder.imagen.getText().toString());
                Intent intent=new Intent(context,Imagen.class);
                context.startActivity(intent);
            }
        });


        holder.btnMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarCantidadReal(holder);
                try {
                    int cantidades=Integer.parseInt(holder.cant.getText().toString());

                    if(cantidades!=0 && cantidades>1) {
                        int cantidad = Integer.parseInt(holder.cant.getText().toString());
                        if(Integer.parseInt(holder.cant.getText().toString())>=1) {
                            double precioUnidad=(Double.parseDouble(holder.total.getText().toString())/cantidad);
                            cantidad--;
                            holder.cant.setText(String.valueOf(cantidad));
                            double precio = Double.parseDouble(holder.total.getText().toString()) - precioUnidad;
                            holder.total.setText(String.valueOf(precio));
                            upDate(holder, cantidad, precio);
                            upDateExistenciasMenos(holder);

                        }
                    }
                }catch(Exception e){
                    e.getMessage();
                    Toast.makeText(context, "error No.2_resumen ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.btnMas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultarCantidadReal(holder);
                int cantidad=Integer.parseInt(holder.cant.getText().toString());
                try {

                    if(existencias>0) {

                        double precioUnidad=(Double.parseDouble(holder.total.getText().toString())/cantidad);
                        cantidad++;
                        holder.cant.setText(String.valueOf(cantidad));

                        double  precio=Double.parseDouble(holder.total.getText().toString())+precioUnidad;
                        holder.total.setText(String.valueOf(precio));
                        upDate(holder,cantidad,precio);
                        upDateExistencias(holder);




                    }else{

                        Toast toast = Toast.makeText(context.getApplicationContext(), "No hay existencias disponible de este producto", Toast.LENGTH_LONG);
                        TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                        x.setTextColor(Color.YELLOW);
                        toast.show();

                    }


                }catch(Exception e){
                    e.getMessage();
                    Toast.makeText(context, "error No.1_resumen ", Toast.LENGTH_SHORT).show();
                }



            }
        });


        holder.eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder alerta = new AlertDialog.Builder(context );
                alerta.setMessage("SEGURO DE ELIMINAR ?")
                        .setCancelable(false)
                        .setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                int res,cantidadAnterior= ConsultaF.up ,cantidad=Integer.parseInt(holder.cant.getText().toString());
                                res=cantidadAnterior-cantidad;
                                ConsultaF.up=res;
                                ConsultaF.unidadesTXT.setText(String.valueOf(ConsultaF.up));

                                double resImporte, importeAnterior=ConsultaF.pre,ImporteActual=Double.parseDouble(holder.total.getText().toString());
                                resImporte=importeAnterior-ImporteActual;
                                ConsultaF.pre=resImporte;
                                ConsultaF.importeTXT.setText(String.valueOf(resImporte));

               /* int resExist, existenciaAnterior=ConsultaF.existencias,existenciasActual=Integer.parseInt(holder.cant.getText().toString());
                resExist=existenciaAnterior+existenciasActual;
                ConsultaF.existencias=resExist;
                ConsultaF.existenciasTXT.setText(String.valueOf(resExist));*/





                                regresarExistencias(holder);

                                String id= holder.id.getText().toString();
                                eliminar(holder);
                                Intent intent=new Intent(context,Resumen.class);
                                context.startActivity(intent);


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
    public String ultimaVez(){
        String numeroT=null;

        try {
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
            SQLiteDatabase db = conn.getReadableDatabase();

            String sql="SELECT nombreT FROM tienda where id=1";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                numeroT = (cursor.getString(0));
            }
        }catch (RuntimeException r){

        }catch (Exception e){

        }

        return numeroT;
    }


    public void upDateExistencias(final ViewHolderResumen holder){
        try {


            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql="update existen set cantreal = cantreal + 1 ,  pedido = pedido + 1  where barcode ='" + holder.bar.getText()+ "' and talla = " + holder.punto.getText() + " and tienda = " + ultimaVez();
            st.executeUpdate(sql);

        }catch (Exception e) {
            Toast.makeText(context, "Error al finalizar el pedido", Toast.LENGTH_SHORT).show();
        }

    }

    public void upDateExistenciasMenos(final ViewHolderResumen holder){
        try {


            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql="update existen set cantreal = cantreal - 1 ,  pedido = pedido - 1  where barcode ='" + holder.bar.getText()+ "' and talla = " + holder.punto.getText() + " and tienda = " + ultimaVez();
            st.executeUpdate(sql);

        }catch (Exception e) {
            Toast.makeText(context, "Error al finalizar el pedido", Toast.LENGTH_SHORT).show();
        }

    }

    public void existenciasEnMiTienda(final ViewHolderResumen holder,int real){


        try {

            List<Map<String, String>> data = null;
            data = new ArrayList<Map<String, String>>();
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String punto=holder.punto.getText().toString();
            String barcode=holder.bar.getText().toString();
            ResultSet rs = st.executeQuery("lupita'"+barcode+"',"+punto+","+ultimaVez());
            ResultSetMetaData rsmd=rs.getMetaData();
            while(rs.next()) {


                existencias=(rs.getInt(2)-real);




            }

        } catch (SQLException e1) {
            e1.printStackTrace();
            Toast.makeText(context.getApplicationContext(),"No hay existencias " , Toast.LENGTH_LONG).show();


        }


    }


    public void regresarExistencias(final ViewHolderResumen holder){
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




public void eliminar(final ViewHolderResumen holder){
    ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);


    SQLiteDatabase db = conn.getReadableDatabase();

    db.execSQL("DELETE FROM contenedor WHERE id="+holder.id.getText());


    db.close();
}

    public void eliminarPedido(final ViewHolderResumen holder){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);


        SQLiteDatabase db = conn.getReadableDatabase();

        db.execSQL("DELETE FROM pedido WHERE id="+holder.id.getText());


        db.close();
    }


    public void upDate(final  ViewHolderResumen holder, int cantidad,double precio){


        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);


        SQLiteDatabase db = conn.getReadableDatabase();
        String query="UPDATE contenedor set cantidad=" + cantidad + ", total="+ precio+" where id="+holder.id.getText();
        db.execSQL(query);
        db.close();


    }


    public void consultarCantidadReal(final  ViewHolderResumen holder){
        int real=0;
        try {


            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="SELECT (isnull(cantreal,0))   FROM existen WHERE barcode='"+holder.bar.getText()+ "' and talla="+holder.punto.getText()+" and tienda="+ultimaVez();
            ResultSet rs = st.executeQuery(sql);
            ResultSetMetaData rsmd=rs.getMetaData();
            while(rs.next()) {
                real =rs.getInt(1);

            }


        } catch (SQLException e1) {

        }
        existenciasEnMiTienda(holder,real);

    }






    @Override
    public int getItemCount() {
        return listResumen.size();
    }

    public class ViewHolderResumen extends RecyclerView.ViewHolder {

        TextView estilo, marca, color, punto, cant,sub,total,bar,id, imagen, acabado;

        Button foto,eliminar,editar,btnMas,btnMenos;

        public ViewHolderResumen(@NonNull View itemView) {
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
            acabado=(TextView)itemView.findViewById(R.id.idAcabado);
            btnMas=(Button)itemView.findViewById(R.id.btnMas);
            btnMenos=(Button)itemView.findViewById(R.id.btnMenos);
           // editar=(Button)itemView.findViewById(R.id.idEditar);
            context=itemView.getContext();


        }
    }
}
