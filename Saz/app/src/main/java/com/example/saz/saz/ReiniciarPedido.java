package com.example.saz.saz;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.saz.saz.Modelo.ModeloEmpresa;
import com.example.saz.saz.Modelo.ModeloTime;
import com.example.saz.saz.conexion.ConexionBDCliente;
import com.example.saz.saz.utilidades.NotificationUtils;

import java.sql.Time;
import java.util.ArrayList;

import static android.app.Notification.DEFAULT_SOUND;
import static android.content.Context.NOTIFICATION_SERVICE;


public class ReiniciarPedido extends Fragment {
    private PendingIntent pendingIntent;
    private final static String CHANNERL_ID="NOTIFICACION";
    private final static int NOTIFICACION=9;
    private NotificationUtils mNotificationUtils;

    public static ConexionBDCliente bdc=new ConexionBDCliente();
    public static ModeloEmpresa me=new ModeloEmpresa();
    View root;
    public static ArrayList listaTiempo=new ArrayList();
    Spinner spTiempo;
static Context context;
    Time actual;
    Button aceptar;
    ModeloTime time=new ModeloTime();
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        root= inflater.inflate(R.layout.fragment_reiniciar_pedido, container, false);
        spTiempo=(Spinner)root.findViewById(R.id.spHora);
        aceptar=(Button)root.findViewById(R.id.idaceptar);
        context=root.getContext();
        mNotificationUtils = new NotificationUtils(context);
        listaTiempo.clear();
        spTiempo.setAdapter(null);
        asicnarTiempo();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item, listaTiempo);
        spTiempo.setAdapter(adapter);
        spTiempo.setSelection(obtenerPosicionItem(spTiempo,time.getTime()));
      aceptar.setOnClickListener(new View.OnClickListener() {
          @RequiresApi(api = Build.VERSION_CODES.O)
          @Override
          public void onClick(View v) {
              Toast toast =new  Toast(getActivity());
              pl.droidsonroids.gif.GifImageView view=new  pl.droidsonroids.gif.GifImageView(getActivity());
              view.setImageResource(R.drawable.loading);
              toast.setView(view);
              toast.show();
              time.setTime(spTiempo.getSelectedItem().toString());

              view.setImageResource(R.drawable.loading);
              toast.setView(view);
              toast.show();
              Toast.makeText(getActivity(), "Listo, minutos configurados", Toast.LENGTH_LONG).show();
          }
      });

        return root;

    }

    public void asicnarTiempo(){
        listaTiempo.add("5");
        listaTiempo.add("10");
        listaTiempo.add("15");
        listaTiempo.add("30");
        listaTiempo.add("45");
        listaTiempo.add("60");


    }

    public static int obtenerPosicionItem(Spinner spinner, String fruta) {

        int posicion = 0;

        for (int i = 0; i < spinner.getCount(); i++) {

            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(fruta)) {
                posicion = i;
            }
        }

        return posicion;
    }
    public void crateNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel=new NotificationChannel(CHANNERL_ID,name,NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager=(NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

        }

    }



    public void pruebao(){
        NotificationCompat.Builder mBuilder;
        NotificationManager mNotifyMgr =(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);

        int icono = R.mipmap.ic_launcher;
        Intent i=new Intent(getActivity(), Principal.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, i, 0);

        mBuilder =new NotificationCompat.Builder(getActivity())
                .setContentIntent(pendingIntent)
                .setSmallIcon(icono)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentTitle("Titulo")
                .setContentText("Hola que tal?")
                .setVibrate(new long[] {100, 250, 100, 500})
                .setAutoCancel(true);
        mNotifyMgr.notify(1, mBuilder.build());
    }




}
