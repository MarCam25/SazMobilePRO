package com.example.saz.saz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saz.saz.Modelo.Config;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.utilidades.Utilidades;

public class Configuraciones extends AppCompatActivity {

    private static  CheckBox CheckBusqueda;




    Button guardar;

    int  buscador=0;

    CheckBox checkMarca, checkTemporada, checkClasificacion, checkSubLinea, checkSuela, checkTacon, checkColor, checkAcabado, checkCorrida;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuraciones);

        CheckBusqueda=(CheckBox)findViewById(R.id.CheckBusqueda);
        guardar=(Button)findViewById(R.id.btnGuardar);
        checkMarca=(CheckBox)findViewById(R.id.CheckMarca);
        checkTemporada=(CheckBox)findViewById(R.id.CheckTemporada);
        checkClasificacion=(CheckBox)findViewById(R.id.CheckClas);
        checkSubLinea=(CheckBox)findViewById(R.id.CheckSubLinea);
        checkSuela=(CheckBox)findViewById(R.id.CheckSuela);
        checkTacon=(CheckBox)findViewById(R.id.CheckTacón);
        checkColor=(CheckBox)findViewById(R.id.CheckColor);
        checkAcabado=(CheckBox)findViewById(R.id.CheckAcabado);
        checkCorrida=(CheckBox)findViewById(R.id.CheckCorrida);

        getSupportActionBar().setTitle("SazMobile Lite -Configuraciones-");

        buscador();
        verificarSimilar();


        if(buscador==1){
            CheckBusqueda.setChecked(true);
        }



        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropCheckBuscador();

                deleteSimilares();

                if(CheckBusqueda.isChecked() == true){
                    insertCheckBuscador();

                }


                if(checkMarca.isChecked() == true){
                    consultarSimilares("marca");
                }else  if(checkMarca.isChecked()==false){
                    deleteSimilar("marca");
                }


                if(checkTemporada.isChecked() == true){
                    consultarSimilares("temporada");
                } else if(checkTemporada.isChecked()==false){
                    deleteSimilar("temporada");
                }


                if(checkClasificacion.isChecked() == true){
                    consultarSimilares("clasificacion");
                } else  if(checkClasificacion.isChecked()==false){
                    deleteSimilar("clasificacion");
                }


                if(checkSubLinea.isChecked() == true){
                    consultarSimilares("sublinea");
                } else  if(checkSubLinea.isChecked()==false){
                    deleteSimilar("sublinea");
                }


                if(checkSuela.isChecked() == true){
                    consultarSimilares("suela");
                } else  if(checkSuela.isChecked()==false){
                    deleteSimilar("suela");
                }


                if(checkTacon.isChecked() == true){
                    consultarSimilares("tacon");
                }else  if(checkTacon.isChecked()==false){
                    deleteSimilar("tacon");
                }





                if(checkColor.isChecked() == true){
                    consultarSimilares("color");
                } else if(checkColor.isChecked()==false){
                    deleteSimilar("color");
                }


                if(checkAcabado.isChecked() == true){
                    consultarSimilares("acabado");
                }else if(checkAcabado.isChecked()==false){
                    deleteSimilar("acabado");
                }


                if(checkCorrida.isChecked() == true){
                    consultarSimilares("corrida");
                } else if(checkCorrida.isChecked()==false){
                    deleteSimilar("corrida");
                }





                Toast toast = Toast.makeText(getApplication(), "Configuración guardada", Toast.LENGTH_LONG);
                TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
                x.setTextColor(Color.YELLOW); toast.show();
            }
        });




    }


    public void insertCheckBuscador() {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();


        db.execSQL("INSERT INTO  "+Utilidades.TABLA_CHECKB +" ("+Utilidades.CAMPO_BUSCADOR+") VALUES('1')");


    }





    public void dropCheckBuscador() {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();


        db.execSQL("DELETE FROM "+ Utilidades.TABLA_CHECKB);


    }


    public void buscador(){

        try {
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext(), "db tienda", null, 1);
            SQLiteDatabase db = conn.getReadableDatabase();

            String sql="SELECT "+Utilidades.CAMPO_BUSCADOR+" FROM "+Utilidades.TABLA_CHECKB;
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                buscador= Integer.parseInt(cursor.getString(0));
            }
        }catch (Exception e){

            Toast toast = Toast.makeText(getApplicationContext(), "La versión nueva de SazMobile LITE se ha instalado", Toast.LENGTH_LONG);
            TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
            x.setTextColor(Color.YELLOW); toast.show();
            Intent intent = new Intent(getApplicationContext(), Principal.class);
            getApplicationContext().deleteDatabase("db tienda");
            startActivity(intent);

        } finally {

        }


    }






    public void insertSimilar(String similar) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();


        db.execSQL("INSERT INTO  "+Utilidades.TABLA_SIMILAR+" ("+similar+") VALUES('1')");


    }


    public void deleteSimilar(String similar) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();


        db.execSQL("UPDATE "+ Utilidades.TABLA_SIMILAR+ " SET "+similar+"=0" );


    }

    public void consultarSimilares(String similar){
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
            checkMarca.setChecked(true);
        }

        if(temporada==1){
            checkTemporada.setChecked(true);
        }

        if(clasificacion==1){
            checkClasificacion.setChecked(true);
        }

        if(sublinea==1){
            checkSubLinea.setChecked(true);
        }

        if(suela==1){
            checkSuela.setChecked(true);
        }

        if(tacon==1){
            checkTacon.setChecked(true);
        }

        if(color==1){
            checkColor.setChecked(true);
        }

        if(acabado==1){
            checkAcabado.setChecked(true);
        }

        if(corrida==1){
            checkCorrida.setChecked(true);
        }

        if(marca==0 && temporada==0 && clasificacion==0 && sublinea==0 && suela==0 && tacon==0 && color==0 && acabado==0 && corrida==0){
            insertSimilar(similar);
        }else{
            updateSimilar(similar);
        }


    }


    public void updateSimilar(String similar){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();


        db.execSQL("UPDATE "+ Utilidades.TABLA_SIMILAR+ " SET "+similar+"=1" );

    }

    public void verificarSimilar(){
        int marca=0,  temporada=0, clasificacion=0,  sublinea=0, suela=0,  tacon=0,  color=0,  acabado=0,  corrida=0;

        try {
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
        }catch (Exception e){
            Toast toast = Toast.makeText(getApplication(), "La versión nueva de SazMobile POS se ha instalado", Toast.LENGTH_LONG);
            TextView x = (TextView) toast.getView().findViewById(android.R.id.message);
            x.setTextColor(Color.YELLOW); toast.show();
            Intent intent = new Intent(getApplicationContext(), Principal.class);
            startActivity(intent);
            getApplicationContext().deleteDatabase("db tienda");



        } finally {




        }


        if(marca==1){
            checkMarca.setChecked(true);
        }

        if(temporada==1){
            checkTemporada.setChecked(true);
        }

        if(clasificacion==1){
            checkClasificacion.setChecked(true);
        }

        if(sublinea==1){
            checkSubLinea.setChecked(true);
        }

        if(suela==1){
            checkSuela.setChecked(true);
        }

        if(tacon==1){
            checkTacon.setChecked(true);
        }

        if(color==1){
            checkColor.setChecked(true);
        }

        if(acabado==1){
            checkAcabado.setChecked(true);
        }

        if(corrida==1){
            checkCorrida.setChecked(true);
        }




    }


    public void deleteSimilares(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "db tienda", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        String sql="DELETE FROM "+ Utilidades.TABLA_SIMILAR;
        db.execSQL(sql );

    }



}
