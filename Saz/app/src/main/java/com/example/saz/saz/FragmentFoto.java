package com.example.saz.saz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSqlServer;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Statement;


public class FragmentFoto extends Fragment {
    TextView estilo, color, acabado, marcas, linea, sublinea, temporada, descripcion, observaciones;
    TextView lineaProvedor, provedor, pagina, basico, comprador, departamento, tacon, plantilla, forro, clasificacion, corrida, suela, ubicacion;
    String style, valores, numero, marca, id;
    String [] separado;
    String idImagen;

    ImageView imagenView;

    String empress;
    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();
    ConexionSqlServer conex=new ConexionSqlServer();
View root;
Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_fragment_foto, container, false);


        imagenView=(ImageView)root.findViewById(R.id.imagenView);
        valores= getActivity().getIntent().getStringExtra("valores");
        separado=valores.split("-");
        style=separado[0];
        id=separado[1];
        numero=separado[2];
        marca=separado[3];
        empress=separado[4];
        obtenerLineaConexion();



        estilo=(TextView)root.findViewById(R.id.estilo);
        color=(TextView)root.findViewById(R.id.color);
        acabado=(TextView)root.findViewById(R.id.acabado);
        marcas=(TextView)root.findViewById(R.id.marca);
        linea=(TextView)root.findViewById(R.id.linea);
        sublinea=(TextView)root.findViewById(R.id.sublinea);
        temporada=(TextView)root.findViewById(R.id.temporada);
        descripcion=(TextView)root.findViewById(R.id.descripcion);
        observaciones=(TextView)root.findViewById(R.id.observaciones);
        lineaProvedor=(TextView)root.findViewById(R.id.lineaProveedor);
        provedor=(TextView)root.findViewById(R.id.proveedor);
        pagina=(TextView)root.findViewById(R.id.pagina);
        basico=(TextView)root.findViewById(R.id.basico);
        comprador=(TextView)root.findViewById(R.id.comprador);
        departamento=(TextView)root.findViewById(R.id.departamento);
        tacon=(TextView)root.findViewById(R.id.tacon);
        plantilla=(TextView)root.findViewById(R.id.plantilla);
        forro=(TextView)root.findViewById(R.id.forro);
        clasificacion=(TextView)root.findViewById(R.id.clasificacion);
        corrida=(TextView)root.findViewById(R.id.corrida);
        suela=(TextView)root.findViewById(R.id.suela);
        ubicacion=(TextView)root.findViewById(R.id.ubicacion);




        llenarTabla();
        getImagen();


        return root;
    }

    public void obtenerLineaConexion() {
        //  String empress = getIntent().getStringExtra("Empresa");


        me = new ModeloEmpresa();

        {
            try {
                Statement st = conex.conexionBD().createStatement();
                ResultSet rs = st.executeQuery("select  Server, Usuariosvr,PassSvr, basededatos, empresa from logins where idEmpresa= " + empress + " and status=1 and borrado=0 ");

                while (rs.next()) {

                    me.setServer(rs.getString("Server"));
                    me.setUsuario(rs.getString("Usuariosvr"));
                    me.setPass(rs.getString("PassSvr"));
                    me.setBase(rs.getString("basededatos"));
                    me.setEmpresa(rs.getString("empresa"));


                }

                // Toast.makeText(Principal.this,"Inicio de sesion Exitosa...!!!: " + empresa, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(context.getApplicationContext(), "Error en la linea de conexion", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void llenarTabla(){
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select a.estilo,c.color,ac.acabado,ma.marca, l.linea,sl.sublinea,t.temporad,a.Descri,a.EstChar,a.LP,p.Nombre as Proveedor,a.PAGINA,a.basico,e.nombre as comprador,d.departamento,ta.tacon,pl.plantilla,f.forro,a.clasific,co.Nombre as Corrida,Su.suela,a.ubica,a.BARCODE,co.inicial,co.final,co.incremento,im.id \n" +
                    " from articulo a inner join lineas l on a.LINEA=l.NUMERO inner join sublinea sl on a.SUBLINEA=sl.NUMERO inner join temporad t on a.TEMPORAD=t.NUMERO\n" +
                    "  inner join proveed p on a.PROVEED=p.numero\n" +
                    "  left join empleado e on a.comprador=e.numero inner join departamentos d on a.DEPARTAMENTO=d.NUMERO\n" +
                    "  inner join tacones ta on a.TACON=ta.NUMERO inner join plantillas pl on a.PLANTILLA=pl.NUMERO inner join forros f on a.FORRO=f.NUMERO \n" +
                    "  inner join corridas co on a.corrida=co.id inner join suelas su on a.SUELA=su.numero inner join colores c on a.color = c.numero\n" +
                    "  inner join acabados ac on a.ACABADO=ac.NUMERO inner join marcas ma on a.MARCA=ma.NUMERO inner join imagenes im on a.id = im.id where a.estilo = '"+style+"' and a.Color ="+id+" and a.acabado = "+numero+" and a.marca = "+marca+"");

            while (rs.next()) {

                estilo.setText(rs.getString(1));
                color.setText(rs.getString(2));
                acabado.setText(rs.getString(3));
                marcas.setText(rs.getString(4));
                linea.setText(rs.getString(5));
                sublinea.setText(rs.getString(6));
                temporada.setText(rs.getString(7));
                descripcion.setText(rs.getString(8));
                observaciones.setText(rs.getString(9));
                lineaProvedor.setText(rs.getString(10));
                provedor.setText(rs.getString(11));
                pagina.setText(rs.getString(12));
                basico.setText(rs.getString(13));
                comprador.setText(rs.getString(14));
                departamento.setText(rs.getString(15));
                tacon.setText(rs.getString(16));
                plantilla.setText(rs.getString(17));
                forro.setText(rs.getString(18));
                clasificacion.setText(rs.getString(19));
                corrida.setText(rs.getString(20));
                suela.setText(rs.getString(21));
                ubicacion.setText(rs.getString(22));
                String barcode=rs.getString(23);
                String in=rs.getString(24);
                String finn=rs.getString(25);
                String inc=rs.getString(26);
                idImagen=rs.getString(27);
            }

        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "Error en llenar la lista", Toast.LENGTH_SHORT).show();
        }

    }

    public void getImagen(){

        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select imagen from imagenes where id="+idImagen);


            while (rs.next()) {
                Blob test=rs.getBlob(1);
                int blobLength = (int) test.length();
                byte[] blobAsBytes = test.getBytes(1, blobLength);
                Bitmap bmap=BitmapFactory.decodeByteArray(blobAsBytes, 0 ,blobAsBytes.length);
                imagenView.setImageBitmap(bmap);

            }

        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "Error en llenar la lista", Toast.LENGTH_SHORT).show();
        }

    }


}
