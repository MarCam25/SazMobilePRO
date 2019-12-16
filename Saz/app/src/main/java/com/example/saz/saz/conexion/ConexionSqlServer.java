package com.example.saz.saz.conexion;

import android.os.StrictMode;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionSqlServer {
    Connection conn=null;

    public Connection conexionBD()
    {



        try
        {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:jtds:sqlserver://Saz9.dnsalias.com;databaseName=hosting;user=sa;password=nyc2011@;");


        }catch(Exception e)
        {
           // Toast.makeText(Principal.this, "error en la conexion", Toast.LENGTH_SHORT).show();

        }



        return conn;
    }

    public void cierraConexion() {
        try {
         conn.close();
        } catch (SQLException sqle) {


        }
    }
}
