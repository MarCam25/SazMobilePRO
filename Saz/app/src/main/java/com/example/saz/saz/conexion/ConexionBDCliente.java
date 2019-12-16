package com.example.saz.saz.conexion;

import android.os.StrictMode;
import android.widget.Toast;

import com.example.saz.saz.Consulta;
import com.example.saz.saz.Modelo.ModeloEmpresa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionBDCliente {
    Connection conn=null;

    public Connection conexionBD(String server,String base,String usuario,String pass )
    {



        String serverVerificado=verificarServer(server);
        try
        {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection("jdbc:jtds:sqlserver://"+serverVerificado+";databaseName="+base+";user="+usuario+";password="+pass+";");


        }catch(SQLException e)
        {
            e.getMessage();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return conn;
    }

    public void cierraConexion() {
        try {
            conn.close();
        } catch (SQLException sqle) {


        }
    }

    public String verificarServer(String server) {
        String contenedor = "";
        String[] contServer = server.split("");
        try {
            for (int x = 0; x < contServer.length; x++) {
                if (!contServer[x].isEmpty()) {

                    String predeterminada = ",";


                    String entrada = contServer[x];


                    String aux = "";


                    if (predeterminada.length() == entrada.length()) {

                        for (int i = 0; i < predeterminada.length(); i++) {


                            if (predeterminada.charAt(i) == entrada.charAt(i)) {

                                aux += predeterminada.charAt(i);
                            }
                        }


                        if (aux.equals(predeterminada)) {
                            contServer[x] = ":";
                        } else {


                        }


                    } else {

                    }


                }

            }

            for (int i = 0; i < contServer.length; i++) {

                contenedor += contServer[i];

            }
        } catch (Exception e) {
            e.getMessage();
        }
        String verificado = "";
        verificado = contenedor;
        return verificado;
    }
}
