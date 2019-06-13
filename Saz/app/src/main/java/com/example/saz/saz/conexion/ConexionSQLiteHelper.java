package com.example.saz.saz.conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.saz.saz.utilidades.Utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {



    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_TIENDA);
        db.execSQL(Utilidades.CREAR_TABLA_CONTENEDOR);
        db.execSQL(Utilidades.CREAR_TABLA_PEDIDO);
        db.execSQL(Utilidades.CREAR_TABLA_ORDEN);
        db.execSQL(Utilidades.CREAR_TABLA_LOGIN);
        db.execSQL(Utilidades.CREAR_TABLA_NOTIFY);
        db.execSQL(Utilidades.CREAR_TABLA_AREA);
        db.execSQL(Utilidades.CREAR_TABLA_CHECKB);
        db.execSQL(Utilidades.CREAR_TABLA_SIMILAR);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Utilidades.TABLA_TIENDA);
        db.execSQL("DROP TABLE IF EXISTS "+ Utilidades.TABLA_CONTENEDOR);
        db.execSQL("DROP TABLE IF EXISTS "+ Utilidades.TABLA_PEDIDO);
        db.execSQL("DROP TABLE IF EXISTS "+ Utilidades.TABLA_ORDEN);
        db.execSQL("DROP TABLE IF EXISTS "+ Utilidades.TABLA_LOGIN);
        db.execSQL("DROP TABLE IF EXISTS "+ Utilidades.TABLA_NOTIFY);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_AREA);
        db.execSQL("DROP TABLE IF EXISTS "+ Utilidades.TABLA_CHECKB);
        db.execSQL("DROP TABLE IF EXISTS " + Utilidades.TABLA_SIMILAR);

        onCreate(db);
    }
}
