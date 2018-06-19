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
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Docentes extends AppCompatActivity {

    ListView listadatos;
    ArrayList<Datos2> lista;
    private ExecutorService queue= Executors.newSingleThreadExecutor();
    String localpatch="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docentes);

        localpatch=getApplicationContext().getFilesDir().getAbsolutePath();
        listadatos = (ListView) findViewById(R.id.lstDatos2);

        String estado=(String) getIntent().getExtras().getSerializable("opcion");

        lista = searchsinconeccion(estado);


        Adaptadordocentes miadaptador=new Adaptadordocentes(getApplicationContext(),lista);
        listadatos.setAdapter(miadaptador);

        listadatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Datos2 obj= (Datos2) parent.getItemAtPosition(position);
                Intent paso = new Intent(getApplicationContext(),Detalle2.class);
                paso.putExtra("obj",(Serializable) obj);
                startActivity(paso);
            }
        });
    }

    public ArrayList<Datos2> searchsinconeccion(String valor) {

        ArrayList<Datos2> listatmp = null;
        CAFData data=CAFData.dataWithContentsOfFile(localpatch+valor);

        if(data != null) {
            listatmp=new ArrayList<Datos2>();
            try {
                JSONObject root = new JSONObject(data.toText());
                //JSONObject coord = root.getJSONObject("coord");
                final JSONArray usuarios = root.getJSONArray("usuario");

                String nombre = "";
                String asignatura = "";
                String semestre = "";
                String programa = "";
                String ruta = "";
                Bitmap bitmap = null;

                if (usuarios.length() > 0) {

                    for (int i = 0; i < usuarios.length(); i++) {
                        JSONObject usuario = usuarios.getJSONObject(i);
                        nombre = usuario.getString("nombre");
                        asignatura = usuario.getString("asignatura");
                        semestre = usuario.getString("semestre");
                        programa = usuario.getString("programa");
                        ruta = usuario.getString("ruta");
                        ruta = ruta.replace(" ", "%20");

                        //surl = IMGDOMAIN + icon + ".png";
                        Log.d("nombres", nombre + " " + asignatura + " " + semestre + " " + programa + " " + ruta);

                        final String finalSemestre = semestre;
                        final String finalprograma = programa;
                        final String finalAsignatura = asignatura;
                        final String finalNombre = nombre;
                        final String rutaguardar = localpatch + "/" + finalNombre + i + ".jpg";
                        Log.e("tamano",""+listatmp.size());

                        final ArrayList<Datos2> finalListatmp = listatmp;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //agregar imagenes
                                boolean repetido=false;
                                for (int j=0;j<finalListatmp.size();j++){
                                    if(finalNombre.equals(finalListatmp.get(j).getNombre().toString())) {
                                        repetido=true;
                                        break;
                                    }
                                }
                                if(!repetido){
                                    finalListatmp.add(new Datos2(1, finalNombre, rutaguardar));
                                }

                            }
                        });
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listatmp;
    }



}
