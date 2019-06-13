package com.example.saz.saz;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.sql.Statement;


public class AddUser extends Fragment {
    public static ModeloEmpresa me=new ModeloEmpresa();
    public static ConexionBDCliente bdc=new ConexionBDCliente();
    View root;
EditText nombre, correo, contraseña, verificar;
Button agregar;




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
                      Toast toast =new  Toast(getActivity());
                      pl.droidsonroids.gif.GifImageView view=new  pl.droidsonroids.gif.GifImageView(getActivity());
                      view.setImageResource(R.drawable.loading);
                      toast.setView(view);
                      toast.show();

                      if(contraseña.getText().toString().isEmpty() || nombre.getText().toString().isEmpty() || correo.getText().toString().isEmpty() || verificar.getText().toString().isEmpty()  ){
                          Toast.makeText(getActivity(),"Todos los campos deben llenarse",Toast.LENGTH_LONG).show();
                      }else{

                          verificarContraseña();
                      }




                  }
              });
        contraseña.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        return root;
    }

    public void verificarContraseña(){
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
                            verificarExistencia();

                            addUser(id);

                            limpiarCajas();







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


            if(verificador==0){
                Toast.makeText(getActivity(), "Usuario agregado", Toast.LENGTH_LONG).show();

            }else if(verificador>=1){
                Toast.makeText(getActivity(), "El usuario que intentas agregar ya existe", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), "El usuario que intentas agregar ya existe Error No.344", Toast.LENGTH_LONG).show();
        }
    }

    public void addUser(int id){
        id=id+1;
        String mensaje;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            String sql="if not exists (select 1 from empleado where nombre = '"+nombre.getText()+"')  begin  INSERT INTO empleado(nombre, [user],[PASSWORD], [status], id)VALUES ('"+nombre.getText()+"','"+correo.getText()+"','"+contraseña.getText()+"',0,"+id+")    end  else  PRINT 'usuario no agregado'  ";
            st.executeUpdate(sql);



            } catch (Exception e) {
            Toast.makeText(getActivity(), "El usuario que intentas agregar ya existe", Toast.LENGTH_LONG).show();
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



    }


