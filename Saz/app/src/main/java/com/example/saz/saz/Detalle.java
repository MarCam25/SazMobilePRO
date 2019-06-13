package com.example.saz.saz;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.conexion.ConexionSQLiteHelper;
import com.example.saz.saz.conexion.ConexionSqlServer;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Statement;

public class Detalle extends AppCompatActivity {
    TextView estilo, color, acabado, marcas, linea, sublinea, temporada, descripcion, observaciones;
    TextView lineaProvedor, provedor, pagina, basico, comprador, departamento, tacon, plantilla, forro, clasificacion, corrida, suela, ubicacion;
String style, valores, numero, marca, id;
String [] separado;
String idImagen;
TextView imagen;
    String foto;
ImageView imagenView;

String empress;
    ModeloEmpresa me=new ModeloEmpresa();
    ConexionBDCliente bdc=new ConexionBDCliente();
    ConexionSqlServer conex=new ConexionSqlServer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        imagenView=(ImageView) findViewById(R.id.imagenView);
        valores= getIntent().getStringExtra("valores");
        separado=valores.split("-");
        style=separado[0];
        id=separado[1];
        numero=separado[2];
        marca=separado[3];
        empress=separado[4];
        obtenerLineaConexion();



        estilo=(TextView)findViewById(R.id.estilo);
        color=(TextView)findViewById(R.id.color);
        acabado=(TextView)findViewById(R.id.acabado);
        marcas=(TextView)findViewById(R.id.marca);
        linea=(TextView)findViewById(R.id.linea);
        sublinea=(TextView)findViewById(R.id.sublinea);
        temporada=(TextView)findViewById(R.id.temporada);
        descripcion=(TextView)findViewById(R.id.descripcion);
        observaciones=(TextView)findViewById(R.id.observaciones);
        lineaProvedor=(TextView)findViewById(R.id.lineaProveedor);
        provedor=(TextView)findViewById(R.id.proveedor);
        pagina=(TextView)findViewById(R.id.pagina);
        basico=(TextView)findViewById(R.id.basico);
        comprador=(TextView)findViewById(R.id.comprador);
        departamento=(TextView)findViewById(R.id.departamento);
        tacon=(TextView)findViewById(R.id.tacon);
        plantilla=(TextView)findViewById(R.id.plantilla);
        forro=(TextView)findViewById(R.id.forro);
        clasificacion=(TextView)findViewById(R.id.clasificacion);
        corrida=(TextView)findViewById(R.id.corrida);
        suela=(TextView)findViewById(R.id.suela);
        ubicacion=(TextView)findViewById(R.id.ubicacion);




        llenarTabla();
        getImagen();

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
                Toast.makeText(getApplicationContext(), "Error en la linea de conexion", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "Error en llenar la lista", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "Error en llenar la lista", Toast.LENGTH_SHORT).show();
        }

    }


    public String ubicacion(String ubica){
        String ubicar=null;
        try {
            Statement st = bdc.conexionBD(me.getServer(),me.getBase(),me.getUsuario(),me.getPass()).createStatement();
            ResultSet rs = st.executeQuery("select nombre from ZonasDeSurtido where idZona="+ubica+ "and idTienda="+ultimaVez());


            while (rs.next()) {

                ubicar=rs.getString(1);
            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en llenar la lista", Toast.LENGTH_SHORT).show();
        }

        return ubicar;
    }

    public String ultimaVez(){
        String numeroT=null;
        try {
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext(), "db tienda", null, 1);
            SQLiteDatabase db = conn.getReadableDatabase();

            String sql="SELECT nombreT FROM tienda where id=1";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                numeroT = (cursor.getString(0));
            }
        }catch (RuntimeException r){

        }catch (Exception e){

        }

        return numeroT;
    }

}
