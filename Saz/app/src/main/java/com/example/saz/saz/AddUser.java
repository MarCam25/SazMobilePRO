package com.example.saz.saz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.conexion.ConexionBDCliente;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class AddUser extends Fragment {
    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();
    View root;
    EditText nombre, correo, contraseña, verificar;
    Button agregar;
    boolean insert=true,mensaje=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root= inflater.inflate(R.layout.fragment_add_user, container, false);
              nombre=(EditText)root.findViewById(R.id.idNombre);
              correo=(EditText)root.findViewById(R.id.idCorreo);
              contraseña=(EditText)root.findViewById(R.id.idContraseña);
              verificar=(EditText)root.findViewById(R.id.idVerificar);
              agregar=(Button)root.findViewById(R.id.idAgregar);





              agregar.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {



                      if(contraseña.getText().toString().isEmpty() || nombre.getText().toString().isEmpty() || correo.getText().toString().isEmpty() || verificar.getText().toString().isEmpty()  ){
                          Toast.makeText(getActivity(),"Todos los campos deben llenarse",Toast.LENGTH_LONG).show();
                      }else{

                          verificarContraseña();
                          if(mensaje==true){

                          }


                      }




                  }
              });


        return root;
    }

    public void verificarContraseña(){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIcon(R.mipmap.ic_launcher);
        progressDialog.setMessage("Cargando...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if(!contraseña.getText().toString().isEmpty() || !verificar.getText().toString().isEmpty())
        {

            String predeterminada = contraseña.getText().toString();


            String entrada = verificar.getText().toString();


            String aux="";


            if(entrada!=null) {
                if (predeterminada.length() == entrada.length()) {

                    for (int i = 0; i < predeterminada.length(); i++) {


                        if (predeterminada.charAt(i) == entrada.charAt(i)) {

                            aux += predeterminada.charAt(i);
                        }
                    }


                    if (aux.equals(predeterminada)) {


                            int id=getMaxId();
                            //verificarExistencia();
                         verificarUsuarios();
                            verificarNombre();
                            if(insert==true){
                                addUser(id);
                                limpiarCajas();
                                Toast.makeText(getActivity(), "Usuario agregado", Toast.LENGTH_LONG).show();
                                progressDialog.dismiss();
                            }else if(insert==false){
                                progressDialog.dismiss();
                            }


                    } else {
                        Toast.makeText(getActivity(), "La contraseña no conincide", Toast.LENGTH_LONG).show();

                    }


                } else {
                    Toast.makeText(getActivity(), "La contraseña no conincide", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getActivity(), "La contraseña no conincide", Toast.LENGTH_LONG).show();
            }

        }

    }


    public void verificarExistencia(){
        int verificador=0;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="Select id from empleado where nombre='"+nombre.getText().toString()+"'";
            ResultSet rs=st.executeQuery(sql);
            while (rs.next()){
                verificador=rs.getInt(1);
            }
            st.close();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "El usuario que intentas agregar ya existe Error No.344", Toast.LENGTH_LONG).show();
        }
    }

    public void addUser(int id){
        id=id+1;

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="INSERT INTO empleado(nombre, [user],[PASSWORD], [status], id)VALUES ('"+nombre.getText()+"','"+correo.getText()+"','"+contraseña.getText()+"',0,"+id+")   ";
            int resultado=st.executeUpdate(sql);
            mensaje=true;

            if(resultado>0){
                String[] TO = {"ayuda@secuencia.com"}; //Direcciones email  a enviar.
                String[] CC = {""}; //Direcciones email con copia.

                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Activación de usuario ");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "ayuda@secuencia.com"); // * configurar email aquí!

                try {
                    startActivity(Intent.createChooser(emailIntent, "Enviar email de activacion."));
                    Log.i("EMAIL", "Enviando email...");
                }
                catch (android.content.ActivityNotFoundException e) {

                }
            }
            st.close();
            } catch (SQLException e) {
            e.getMessage();
        }
    }


    public int getMaxId(){
        int id=0;

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select max(id) from empleado ";
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                id=rs.getInt(1);
            }

            st.close();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error al verificar la contraseña", Toast.LENGTH_LONG).show();
        }
        return id;
    }

    public void limpiarCajas(){

        nombre.setText(null);
        contraseña.setText(null);
        verificar.setText(null);
        correo.setText(null);

    }


    public void verificarUsuarios(){
        int id=0;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select id from empleado where user='"+correo.getText()+"' ";
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                id=rs.getInt(1);
            }
            st.close();

            if(id>0){
                insert=false;
                Toast.makeText(getActivity(),"Este correo ya existe",Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error al verificar la contraseña", Toast.LENGTH_LONG).show();
        }
    }

    public void verificarNombre(){
        int id=0;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="select id from empleado where nombre='"+nombre.getText()+"' ";
            ResultSet rs=st.executeQuery(sql);
            while(rs.next()){
                id=rs.getInt(1);
            }
            st.close();
            if(id>0){
                insert=false;
                Toast.makeText(getActivity(),"Este empleado ya existe",Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            Toast.makeText(getActivity(), "Error al verificar la contraseña", Toast.LENGTH_LONG).show();
        }
    }

    }


