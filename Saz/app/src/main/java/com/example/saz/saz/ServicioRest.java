package com.example.saz.saz;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ServicioRest {
Principal pri=new Principal();


    public void  obtenerDatosVolley(){
        // String url="https://api.androidhive.info/contacts/";
        String url= "http://secuenciaonline.com/svcCadEncrypt/cadEncrypt.svc/encjson/"+pri.en.getText();
        //los metodos de http cambian segun lo que necesitas en este caso se utiliza POST
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //aqui va el nombre del JsonArray en este caso no tenemos
                    //  JSONArray mJsonArray=response.getJSONArray(" ");
                    //este for recorre
                    // el objeto Json para tomar los valores
                    // for(int i=0;i<mJsonArray.length();i++){
                    //   JSONObject mJsonObjet=mJsonArray.getJSONObject(i);
                    //pides el nombre exacto del dato que se necesita
                    // String name=mJsonObjet.getString("EncriptaJSONResult");
                    String name=response.get("EncriptaJSONResult").toString();
                    pri.des.setText(name);

                    // }


                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override//Obtiene el error al no encontrar el resultado que se pide
            public void onErrorResponse(VolleyError error) {
            //    Toast.makeText(Principal.this,"Error2.0",Toast.LENGTH_LONG).show();
            }
        }

        );
      //  pri.queue.add(request);
    }


}
