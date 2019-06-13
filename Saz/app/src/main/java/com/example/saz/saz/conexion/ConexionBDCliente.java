package com.example.saz.saz.conexion;

import android.os.StrictMode;
import android.widget.Toast;

import com.example.saz.saz.Consulta;
import com.example.saz.saz.Modelo.ModeloEmpresa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionBDCliente {

    public Connection conexionBD(String server,String base,String usuario,String pass )
    {
      ModeloEmpresa me=new ModeloEmpresa();
        Connection conn=null;


        try
        {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:jtds:sqlserver://"+server+";databaseName="+base+";user="+usuario+";password="+pass+";");


        }catch(SQLException e)
        {
            e.getMessage();
             //Toast.makeText(, "error en la conexion", Toast.LENGTH_SHORT).show();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return conn;
    }
}
