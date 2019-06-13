package com.example.saz.saz;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Scann extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    TextView codigo;
    ZXingScannerView scannerView;
    Button scann;
     ZXingScannerView vistaescaner;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_scann);


                scannerView = new ZXingScannerView(this);
                setContentView(scannerView);



            }


    @Override
    public void handleResult(Result result) {


        if(Principal.escan==1) {

            Principal.scannPass=true;
            ConsultaF.cargarDatos(result.getText());
            onBackPressed();

            }else if(Principal.escan==2){
            Principal.scannPass=true;
            AltaDeProducto.cargarDatos(result.getText());
            onBackPressed();
            }
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}
