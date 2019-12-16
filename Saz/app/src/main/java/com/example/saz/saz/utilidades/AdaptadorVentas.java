package com.example.saz.saz.utilidades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saz.saz.DetalleVenta;
import com.example.saz.saz.Modelo.Numero;
import com.example.saz.saz.Principal;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloResumen;
import com.example.saz.saz.R;
import com.example.saz.saz.entidades.Comandero;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AdaptadorVentas extends RecyclerView.Adapter<AdaptadorVentas.ViewHolderVentas> {
    ArrayList<Comandero> listComandero;
    String contenedor;

    String numero;
    String talla;
    String barcode;
    String empleado;

    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();
    public  static  Numero numeros=new Numero();

public AdaptadorVentas (ArrayList<Comandero> listComandero){
    this.listComandero=listComandero;
}

    @Override
    public ViewHolderVentas onCreateViewHolder( ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_venta, null, false);
        return new ViewHolderVentas(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolderVentas holder, final int position) {
        holder.numero.setText(listComandero.get(position).getNumero());
        holder.cliente.setText(listComandero.get(position).getCliente());
        holder.total.setText(listComandero.get(position).getTotal());
        holder.pares.setText(listComandero.get(position).getPares());
        holder.empleado.setText(listComandero.get(position).getEmpleado());
        holder.idOrden.setText(listComandero.get(position).getIdOrden());

        holder.btnDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.context,DetalleVenta.class);
                numeros.setNumero(holder.numero.getText().toString());
                holder.context.startActivity(intent);
            }
        });

        holder.QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast =new  Toast(holder.context);
                pl.droidsonroids.gif.GifImageView view=new  pl.droidsonroids.gif.GifImageView(holder.context);
                view.setImageResource(R.drawable.loading);
                toast.setView(view);
                toast.show();

              traerVentas(holder);

              generarQR(holder);


            }
        });

        holder.btnRechasar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alerta = new AlertDialog.Builder(holder.context );
                alerta.setMessage("SEGURO DE ELIMINAR ?")
                        .setCancelable(false)
                        .setPositiveButton("ELIMINAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                consultarDatos(holder);
                                rechazarComandero(holder);
                                rechazarDetalle(holder);
                                Principal.location=2;
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
        this.listComandero.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount() - position);
    }


    public void consultarDatos(final ViewHolderVentas holder){

    String  barcode=null;
        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select barcode from comanderoDet where numero="+holder.numero.getText();
            ResultSet rs=st.executeQuery(sql);
            ModeloResumen mr=null;
            while(rs.next()){
                barcode=rs.getString (1);
                regresarCantidad(barcode, holder);
            }
            st.close();



        } catch (SQLException e) {
            e.getMessage();
        }
    }




    public void regresarCantidad(String  barcode,final ViewHolderVentas holder){


        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="UPDATE existen set pedido=pedido-1 , CANTREAL=CANTREAL-1 where barcode='"+barcode+"'";
            st.executeUpdate(sql);
            st.close();




        } catch (SQLException e) {
            e.getMessage();
        }


    }

    public void rechazarDetalle(final ViewHolderVentas holder){
        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="UPDATE  comanderoDet SET status=10 WHERE numero="+holder.numero.getText();
            st.executeUpdate(sql);
            st.close();
            ModeloResumen mr=null;




        } catch (Exception e) {

        }
    }
    public void traerVentas(final ViewHolderVentas holder){
        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="SELECT comandero.numero, talla,barcode,empleado FROM  comandero INNER JOIN comanderoDet ON comandero.numero=comanderoDet.numero WHERE comandero.[status]=1 AND comandero.numero="+holder.numero.getText();
            ResultSet rs = st.executeQuery(sql);
            ModeloResumen mr=null;

            while (rs.next()) {

                 numero=(rs.getString(1));
                 talla=(rs.getString(2));
                 barcode=(rs.getString(3));
                 String idEmpleado=getEmpleado( empleado=(rs.getString(4)));
                 contenedor+="//"+numero+","+talla+","+barcode+","+idEmpleado;

            }
            st.close();


        } catch (Exception e) {
            Toast.makeText(holder.context, "error al seleccionar ", Toast.LENGTH_SHORT).show();
        }
    }

    public void rechazarComandero(final ViewHolderVentas holder){
        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="update comandero set status=10 WHERE numero="+holder.numero.getText();
            st.executeUpdate(sql);
            st.close();
            ModeloResumen mr=null;




        } catch (SQLException e) {
            e.getMessage();
        }

    }

    public String getEmpleado(String empleado){
        String idEmpleado=null;
        try {

            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select id from empleado where nombre='"+empleado+"';";
            ResultSet rs = st.executeQuery(sql);
            st.close();
            ModeloResumen mr=null;

            while (rs.next()) {
                 idEmpleado= rs.getString(1);


            }


        } catch (Exception e) {

        }
return idEmpleado;

    }



    public void generarQR(final ViewHolderVentas holder){
        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
        try {
            BitMatrix bitMatrix=multiFormatWriter.encode(contenedor,BarcodeFormat.QR_CODE,1000,1000);
            BarcodeEncoder barcodeEncoder=new BarcodeEncoder();
            Bitmap bitmap=barcodeEncoder.createBitmap(bitMatrix);
            holder.codigo.setImageBitmap(bitmap);
            holder.QR.setVisibility(View.INVISIBLE);
        }catch (WriterException e){


        }
    }


    @Override
    public int getItemCount() {

    return listComandero.size();
    }

    public class ViewHolderVentas extends RecyclerView.ViewHolder {
        Context context;
        TextView numero, cliente,total, pares,empleado, idOrden, editar;
        Button QR, btnDetalle,btnRechasar;
        ImageView codigo;

        public ViewHolderVentas(@NonNull View itemView) {
            super(itemView);
            numero = (TextView) itemView.findViewById(R.id.NumOrden);
            cliente = (TextView) itemView.findViewById(R.id.cliente);
            total=(TextView)itemView.findViewById(R.id.idTotal);
            pares=(TextView)itemView.findViewById(R.id.idpares);
            empleado=(TextView)itemView.findViewById(R.id.idEmpleado);
            idOrden=(TextView)itemView.findViewById(R.id.idOrden);
            QR=(Button)itemView.findViewById(R.id.idAtender);
            codigo=(ImageView)itemView.findViewById(R.id.codigo);
            btnDetalle=(Button)itemView.findViewById(R.id.btnDetalle);
            btnRechasar=(Button)itemView.findViewById(R.id.btnEliminar);
            context=itemView.getContext();
        }

    }
}
