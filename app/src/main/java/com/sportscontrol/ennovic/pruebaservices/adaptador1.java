package com.sportscontrol.ennovic.pruebaservices;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class adaptador1 extends AppCompatActivity {

    ListView listadatos;
    ArrayList<Datos> lista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adaptador1);

        listadatos = (ListView) findViewById(R.id.lstDatos);



        lista = (ArrayList<Datos>) getIntent().getExtras().getSerializable("arreglo");



        Adaptador miadaptador=new Adaptador(getApplicationContext(),lista);
        listadatos.setAdapter(miadaptador);

        listadatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Datos obj= (Datos) parent.getItemAtPosition(position);
                Intent paso = new Intent(getApplicationContext(),Detalle.class);
                paso.putExtra("objeto",(Serializable) obj);
                startActivity(paso);
            }
        });


    }


}
