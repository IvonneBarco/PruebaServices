package com.sportscontrol.ennovic.pruebaservices;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Detalle extends AppCompatActivity {
    ImageView foto;
    TextView titulo;
    TextView detalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        foto=(ImageView)findViewById(R.id.imgfoto);
        titulo=(TextView) findViewById(R.id.txttitulo);
        detalle=(TextView) findViewById(R.id.txtdetalle);

        Datos obj=(Datos) getIntent().getExtras().getSerializable("objeto");

        titulo.setText(obj.getTitulo());
        detalle.setText(obj.getDetalle());

        CAFData data=CAFData.dataWithContentsOfFile(obj.getImagen());
        if(data!=null){
            Bitmap bitmap =data.toImage();

            if(bitmap != null){
                foto.setImageBitmap(bitmap);
            }
        }
    }
}
