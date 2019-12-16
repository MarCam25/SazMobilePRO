package com.example.saz.saz;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloUsuario;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.entidades.Comandero;

import com.example.saz.saz.utilidades.AdaptadorVentas;
import com.example.saz.saz.utilidades.Utilidades;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Venta extends Fragment {
    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();

    ArrayList<Comandero> listaComandero;
    RecyclerView recyclerView;

    Context context;
    View root;
    ModeloUsuario mu=new ModeloUsuario();
    int tipo=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       root=inflater.inflate(R.layout.fragment_venta, container, false);
        context=root.getContext();

        listaComandero=new ArrayList<>();
        recyclerView=(RecyclerView)root.findViewById(R.id.resicladorId);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        int area = getAreaActual();
        getArea();
        if(area==-1){
            mostrarVentasGeneral();
        }else if (tipo==1){
            mostrarVentas();
        }else{
             Toast.makeText(getActivity(), "No tienes acceso al contenido de esta pantalla", Toast.LENGTH_LONG).show();
        }


        AdaptadorVentas adapter=new AdaptadorVentas(listaComandero);
        recyclerView.setAdapter(adapter);

        return root;
    }


    public void mostrarVentasGeneral(){
        String mensaje ="No hay productos";
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select cd.numero, c.cliente, cast(Sum(pr.precio - (pr.precio * (pr.descto/100))) as decimal(8,2)) as total, count(cd.Llave) as pares, c.empleado from comanderoDet cd inner join comandero c on cd.numero = c.numero inner join precios pr on cd.barcode = pr.barcode and cd.talla =  pr.TALLA where c.empleado = '"+mu.getNombre()+"' and cd.status = 7 group by cd.numero, c.Cliente, c.empleado");

            Comandero comandero=null;
            while (rs.next()) {
                comandero=new Comandero();
                mensaje="";
                comandero.setNumero(rs.getString(1));
                comandero.setCliente(rs.getString(2));
                comandero.setTotal(rs.getString(3));
                comandero.setPares(rs.getString(4));
                comandero.setEmpleado(rs.getString(5));

                listaComandero.add(comandero);
            }
            st.close();

            Toast.makeText(getActivity(),""+mensaje,Toast.LENGTH_LONG).show();

        } catch (Exception e) {

        }
    }


    public void mostrarVentas(){
        String mensaje="No hay productos";
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select cd.numero, c.cliente, cast(Sum(pr.precio - (pr.precio * (pr.descto/100))) as decimal(8,2)) as total, count(cd.Llave) as pares, c.empleado from comanderoDet cd inner join comandero c on cd.numero = c.numero inner join precios pr on cd.barcode = pr.barcode and cd.talla =  pr.TALLA where c.empleado = '"+getIdUsuario()+"' and cd.status = 1 group by cd.numero, c.Cliente, c.empleado";
            ResultSet rs = st.executeQuery(sql);

            Comandero comandero=null;
            while (rs.next()) {
                mensaje="";
                comandero=new Comandero();

                comandero.setNumero(rs.getString(1));
                comandero.setCliente(rs.getString(2));
                comandero.setTotal(rs.getString(3));
                comandero.setPares(rs.getString(4));
                comandero.setEmpleado(rs.getString(5));

                listaComandero.add(comandero);
            }
            st.close();

            Toast.makeText(getActivity(),""+mensaje,Toast.LENGTH_LONG).show();

        } catch (Exception e) {

        }
    }

    public String getArea() {

        String area = null;
        try {
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context, "db tienda", null, 1);
            SQLiteDatabase db = conn.getReadableDatabase();

            String sql = "SELECT area,tipo FROM " + Utilidades.TABLA_AREA;
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                area = (cursor.getString(0));
                tipo=cursor.getInt(1);
            }
        } catch (RuntimeException r) {

        }
        return area;
    }

    public int getAreaActual() {
        int area = 0;
        String idEmpleado = getIdUsuario();

        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "select top 1 idArea from AreasAsignadas where idEmpleado=" + idEmpleado + " and  fecha=CONVERT(nCHAR(8), getDate() , 112) order by hora desc ";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                area = (rs.getInt(1));

            }
            st.close();

        } catch (SQLException e) {
        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }
        return area;
    }

    public String getIdUsuario() {
        String idUsuario = null;
        ModeloUsuario mu = new ModeloUsuario();
        try {

            Statement st = bdc.conexionBD(me.getServer(), me.getBase(), me.getUsuario(), me.getPass()).createStatement();
            String sql = "Select numero from empleado where nombre='" + mu.getNombre() + "'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {
                idUsuario = (rs.getString(1));

            }
            st.close();

        } catch (SQLException e) {
        } catch (NullPointerException nu) {

        } catch (RuntimeException r) {

        }
        return idUsuario;
    }

}
