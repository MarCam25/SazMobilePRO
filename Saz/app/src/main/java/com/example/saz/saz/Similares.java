package com.example.saz.saz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saz.saz.Modelo.AdaptadorSimilares;
import com.example.saz.saz.Modelo.DatosLupita;
import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloSimilar;
import com.example.saz.saz.Modelo.Similar;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Similares extends AppCompatActivity {

    ModeloEmpresa me=new ModeloEmpresa();
    Similar similar=new Similar();
    DatosLupita dl=new DatosLupita();
    ConexionBDCliente bdc=new ConexionBDCliente();
    ArrayList<ModeloSimilar> listaSimilar;
    RecyclerView recyclerView;
    String tienda;
    int marcaV=0,  temporadaV=0, clasificacionV=0,  sublineaV=0, suelaV=0,  taconV=0,  colorV=0,  acabadoV=0,  corridaV=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similares);


        listaSimilar=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recicladorSimilar);
        ultimaVez();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        validar();
        if(marcaV==0 &&  temporadaV==0&& clasificacionV==0 &&  sublineaV==0 && suelaV==0 && taconV==0 &&  colorV==0 &&  acabadoV==0 && corridaV==0) {
            Toast.makeText(this, "No has configurado los similares", Toast.LENGTH_SHORT).show();
        }else{
            consultarSimilares();
        }

        AdaptadorSimilares adapter=new AdaptadorSimilares(listaSimilar);
        recyclerView.setAdapter(adapter);

    }






    public void getSimilares(String marca, String temporada, String clasificacion, String sublinea, String suela, String tacon, String color, String acabado, String corrida){
        try {
            String validar=null;
            ModeloSimilar similar=null;
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select DISTINCT a.estilo,ac.acabado, c.COLOR, ma.MARCA, ex.TALLA, a.BARCODE,co.Nombre \n" +
                    " from articulo a inner join lineas l on a.LINEA=l.NUMERO inner join sublinea sl on a.SUBLINEA=sl.NUMERO inner join temporad t on a.TEMPORAD=t.NUMERO\n" +
                    "  inner join proveed p on a.PROVEED=p.numero\n" +
                    "  left join empleado e on a.comprador=e.numero inner join departamentos d on a.DEPARTAMENTO=d.NUMERO\n" +
                    "  inner join tacones ta on a.TACON=ta.NUMERO inner join plantillas pl on a.PLANTILLA=pl.NUMERO inner join forros f on a.FORRO=f.NUMERO \n" +
                    "  inner join corridas co on a.corrida=co.id inner join suelas su on a.SUELA=su.numero inner join colores c on a.color = c.numero\n" +
                    "  inner join acabados ac on a.ACABADO=ac.NUMERO inner join marcas ma on a.MARCA=ma.NUMERO left join imagenes im on a.id=im.id inner join clasific clas on a.CLASIFIC=clas.numero inner join existen ex on a.barcode = ex.barcode and ex.CANTIDAD>0  where a.marca"+marca+" and a.temporad"+temporada+" and a.clasific"+clasificacion+" and  a.sublinea"+sublinea+" and a.suela"+suela+" and a.tacon"+tacon+" and a.color"+color+" and a.acabado"+acabado+" and a.corrida"+corrida;
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {

                similar=new ModeloSimilar();
                similar.setEstilo(rs.getString(1));
                similar.setAcabado(rs.getString(2));
                similar.setColor(rs.getString(3));
                similar.setMarca(rs.getString(4));
                similar.setCorrida(rs.getString(5));
                similar.setBarcode(rs.getString(6));
                similar.setPunto(rs.getString(7));
                listaSimilar.add(similar);

                validar="hola";

            }
            if(validar!=null){

            }else{
                Toast toast = Toast.makeText(getApplication(), "Este producto no cuenta con similares", Toast.LENGTH_LONG);
                TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                x.setTextColor(Color.YELLOW); toast.show();
            }

        } catch (SQLException xe) {
            xe.getMessage();

        }
    }

    public void ultimaVez(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();
        String sql="SELECT nombreT FROM tienda where id=1";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            String numeroT=(cursor.getString(0));
            tienda=numeroT;

        }
    }


    public void consultarSimilares(){
        int marca=0,  temporada=0, clasificacion=0,  sublinea=0, suela=0,  tacon=0,  color=0,  acabado=0,  corrida=0;
        String marcaS=">0",  temporadaS=">0", clasificacionS=">0",  sublineaS=">0", suelaS=">0",  taconS=">0",  colorS=">0",  acabadoS=">0",  corridaS=">0";

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        String sql="SELECT * FROM similar";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {

            marca=cursor.getInt(1);
            temporada=cursor.getInt(2);
            clasificacion=cursor.getInt(3);
            sublinea=cursor.getInt(4);
            suela=cursor.getInt(5);
            tacon=cursor.getInt(6);
            color=cursor.getInt(7);
            acabado=cursor.getInt(8);
            corrida=cursor.getInt(9);
        }

        if(marca==1){
            marcaS="="+consultarMarca();
        }

        if(temporada==1){
            temporadaS="="+similar.getTemporada();
        }

        if(clasificacion==1){
            clasificacionS="="+similar.getClasificacion();
        }

        if(sublinea==1){
            sublineaS="="+similar.getSubLinea();
        }

        if(suela==1){
            suelaS="="+similar.getSuela();
        }

        if(tacon==1){
            taconS="="+similar.getTacon();
        }

        if(color==1){
            colorS="="+consultarColor();
        }

        if(acabado==1){
            acabadoS="="+consultarAcabado();
        }

        if(corrida==1){
            corridaS="="+similar.getCorrida();
        }

        getSimilares(marcaS, temporadaS, clasificacionS, sublineaS, suelaS, taconS, colorS, acabadoS, corridaS);
    }


    public String  consultarMarca() {
        String marca=null;
        Similar simi=new Similar();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select numero from marcas where marca='"+simi.getMarca()+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {


                marca=rs.getString(1);


            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al consultar producto: Marca", Toast.LENGTH_SHORT).show();
        }

        return marca;
    }


    public String  consultarColor() {
        String color=null;
        Similar simi=new Similar();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select numero from colores where color='"+simi.getColor()+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {


                color=rs.getString(1);


            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al consultar producto: Color", Toast.LENGTH_SHORT).show();
        }

        return color;
    }


    public String  consultarAcabado() {
        String acabado=null;
        Similar simi=new Similar();
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select numero from acabados where acabado='"+simi.getAcabado()+"'";
            ResultSet rs = st.executeQuery(sql);


            while (rs.next()) {


                acabado=rs.getString(1);


            }

            // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error al consultar producto: acabado", Toast.LENGTH_SHORT).show();
        }

        return acabado;
    }


    public void validar(){



        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getReadableDatabase();

        String sql="SELECT * FROM similar";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {

            marcaV=cursor.getInt(1);
            temporadaV=cursor.getInt(2);
            clasificacionV=cursor.getInt(3);
            sublineaV=cursor.getInt(4);
            suelaV=cursor.getInt(5);
            taconV=cursor.getInt(6);
            colorV=cursor.getInt(7);
            acabadoV=cursor.getInt(8);
            corridaV=cursor.getInt(9);
        }


    }


}
