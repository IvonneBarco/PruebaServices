package com.sportscontrol.ennovic.pruebaservices;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class Detalle2 extends AppCompatActivity {
    ImageView foto;
    TextView titulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle2);

        foto=(ImageView)findViewById(R.id.fotodocente);
        titulo=(TextView) findViewById(R.id.nombredocente);


        Datos2 obj=(Datos2) getIntent().getExtras().getSerializable("obj");

        titulo.setText(obj.getNombre());


        CAFData data=CAFData.dataWithContentsOfFile(obj.getImagen());
        if(data!=null){
            Bitmap bitmap =data.toImage();

            if(bitmap != null){
                foto.setImageBitmap(bitmap);
            }
        }
    }
}
