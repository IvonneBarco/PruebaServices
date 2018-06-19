package com.sportscontrol.ennovic.pruebaservices;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ExecutorService queue = Executors.newSingleThreadExecutor();
    public String dominio = "http://cendesoft.iucesmag.edu.co/cendesoft/zeus/evaluacion/service_con.php?programa=dise";
    public String dominio2 = "http://cendesoft.iucesmag.edu.co/cendesoft/zeus/evaluacion/service_con.php?programa=arq";
    ArrayList<Datos> lista;
    ArrayList<Datos> lista2;

    String localpatch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        localpatch = getApplicationContext().getFilesDir().getAbsolutePath();

        Button boton = (Button) findViewById(R.id.pasar);
        Button boton2 = (Button) findViewById(R.id.pasar2);
        Button boton3 = (Button) findViewById(R.id.pasar3);
        Button boton4 = (Button) findViewById(R.id.pasar4);

        if (!compruebaConexion(this)) {
            lista = searchdisesinconeccion();
            lista2 = searcharqusinconeccion();
            if ((lista == null) && (lista2 == null)) {
                Toast.makeText(getBaseContext(), "Necesaria conexión a internet por primera vez", Toast.LENGTH_SHORT).show();
            }

            if (lista == null) {
                Toast.makeText(getBaseContext(), "Error al cargar base de datos de Diseño, Conecte a internet pata sincronizar", Toast.LENGTH_SHORT).show();
                boton.setEnabled(false);
            }

            if (lista2 == null) {
                Toast.makeText(getBaseContext(), "Error al cargar base de datos de Arquitectura, Conecte a internet pata sincronizar", Toast.LENGTH_SHORT).show();
                boton2.setEnabled(false);
            }


        } else {
            lista = searchdise();
            lista2 = searcharqu();
        }

        boton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                Intent paso = new Intent(getApplicationContext(), adaptador1.class);
                paso.putExtra("arreglo", lista);
                startActivity(paso);
            }
        });

        boton2.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                Intent paso = new Intent(getApplicationContext(), adaptador1.class);
                paso.putExtra("arreglo", lista2);
                startActivity(paso);
            }
        });

        boton3.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                Intent paso = new Intent(getApplicationContext(), Docentes.class);
                paso.putExtra("opcion", "arqui");
                startActivity(paso);
            }
        });

        boton4.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                Intent paso = new Intent(getApplicationContext(), Docentes.class);
                paso.putExtra("opcion", "disem,,,");
                startActivity(paso);
            }
        });


    }

    public static boolean compruebaConexion(Context context) {

        boolean connected = false;

        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }

    public ArrayList<Datos> searchdise() {

        final ArrayList<Datos> listatmp = new ArrayList<Datos>();

        Runnable thread = new Runnable() {
            @Override
            public void run() {
                String surl = dominio;
                URL url = null;
                CAFData remotedata = null;
                try {
                    url = new URL(surl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (url != null) {
                    remotedata = CAFData.dataWithContentsOfURL(url);
                    //Log.d("datos", remotedata.toText());
                    final CAFData finalremotedata = remotedata;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalremotedata != null) {
                                finalremotedata.writeToFile(localpatch + "dise", true);
                            }
                        }
                    });
                    try {
                        JSONObject root = new JSONObject(remotedata.toText());
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
                                try {
                                    url = new URL(ruta);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }

                                remotedata = CAFData.dataWithContentsOfURL(url);

                                if (remotedata != null) {
                                    bitmap = remotedata.toImage();
                                } else {
                                    try {
                                        url = new URL("http://www.iucesmag.edu.co/wp-content/uploads/2015/10/Emojis2-compressor.jpg");
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    remotedata = CAFData.dataWithContentsOfURL(url);
                                    bitmap = remotedata.toImage();
                                }


                                final Bitmap bitmaptmp = bitmap;

                                final String finalSemestre = semestre;
                                final String finalprograma = programa;
                                final String finalAsignatura = asignatura;
                                final String finalNombre = nombre;
                                final String rutaguardar = localpatch + "/" + finalNombre + i + ".jpg";

                                Log.d("rutaguardar", rutaguardar);


                                final CAFData finalremotedata2 = remotedata;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (finalremotedata2 != null) {
                                            finalremotedata2.writeToFile(rutaguardar, true);
                                        }
                                    }
                                });

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //agregar imagenes
                                        listatmp.add(new Datos(1, finalNombre, finalAsignatura, rutaguardar, finalSemestre, finalprograma));

                                    }
                                });
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        queue.execute(thread);
        return listatmp;
    }

    public ArrayList<Datos> searcharqu() {

        final ArrayList<Datos> listatmp = new ArrayList<Datos>();

        Runnable thread = new Runnable() {
            @Override
            public void run() {
                String surl = dominio2;
                URL url = null;
                CAFData remotedata = null;
                try {
                    url = new URL(surl);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                if (url != null) {
                    remotedata = CAFData.dataWithContentsOfURL(url);
                    //Log.d("datos", remotedata.toText());

                    //tratar de grabar datos
                    final CAFData finalremotedata = remotedata;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finalremotedata != null) {
                                finalremotedata.writeToFile(localpatch + "arqui", true);
                            }
                        }
                    });

                    try {
                        JSONObject root = new JSONObject(remotedata.toText());
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
                                try {
                                    url = new URL(ruta);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }

                                remotedata = CAFData.dataWithContentsOfURL(url);

                                if (remotedata != null) {
                                    bitmap = remotedata.toImage();
                                } else {
                                    try {
                                        url = new URL("http://www.iucesmag.edu.co/wp-content/uploads/2015/10/Emojis2-compressor.jpg");
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                    remotedata = CAFData.dataWithContentsOfURL(url);
                                    bitmap = remotedata.toImage();
                                }


                                final Bitmap bitmaptmp = bitmap;

                                final String finalSemestre = semestre;
                                final String finalprograma = programa;
                                final String finalAsignatura = asignatura;
                                final String finalNombre = nombre;
                                final String rutaguardar = localpatch + "/" + finalNombre + i + ".jpg";

                                Log.d("rutaguardar", rutaguardar);


                                final CAFData finalremotedata2 = remotedata;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (finalremotedata2 != null) {
                                            finalremotedata2.writeToFile(rutaguardar, true);
                                        }
                                    }
                                });

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //agregar imagenes
                                        listatmp.add(new Datos(1, finalNombre, finalAsignatura, rutaguardar, finalSemestre, finalprograma));

                                    }
                                });
                            }


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        queue.execute(thread);
        return listatmp;
    }

    public ArrayList<Datos> searchdisesinconeccion() {

        ArrayList<Datos> listatmp = null;
        CAFData data = CAFData.dataWithContentsOfFile(localpatch + "dise");

        if (data != null) {
            listatmp = new ArrayList<Datos>();
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

                        final ArrayList<Datos> finalListatmp = listatmp;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //agregar imagenes
                                finalListatmp.add(new Datos(1, finalNombre, finalAsignatura, rutaguardar, finalSemestre, finalprograma));

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

    public ArrayList<Datos> searcharqusinconeccion() {

        ArrayList<Datos> listatmp = null;
        CAFData data = CAFData.dataWithContentsOfFile(localpatch + "arqui");

        if (data != null) {
            listatmp = new ArrayList<Datos>();
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

                        final ArrayList<Datos> finalListatmp = listatmp;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //agregar imagenes
                                finalListatmp.add(new Datos(1, finalNombre, finalAsignatura, rutaguardar, finalSemestre, finalprograma));

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