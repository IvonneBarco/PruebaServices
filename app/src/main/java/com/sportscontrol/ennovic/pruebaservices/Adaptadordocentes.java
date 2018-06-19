package com.sportscontrol.ennovic.pruebaservices;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class Adaptadordocentes extends BaseAdapter {

    Context contexto;
    List<Datos2> listaobjetos;

    public Adaptadordocentes(Context contexto, List<Datos2> listaobjetos) {
        this.contexto = contexto;
        this.listaobjetos = listaobjetos;
    }

    @Override
    public int getCount() {
        return listaobjetos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaobjetos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listaobjetos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vista = convertView;
        LayoutInflater inflate=LayoutInflater.from(contexto);
        vista=inflate.inflate(R.layout.itemistview2,null);
        ImageView imagen =(ImageView) vista.findViewById(R.id.fotodoc);
        TextView titulo=(TextView) vista.findViewById(R.id.nomdoc);


        titulo.setText(listaobjetos.get(position).getNombre().toString());

        //imagen.setImageResource();

        CAFData data=CAFData.dataWithContentsOfFile(listaobjetos.get(position).getImagen());
        if(data!=null){
            Bitmap bitmap =data.toImage();

            if(bitmap != null){
                imagen.setImageBitmap(bitmap);
            }
        }

        return vista;
    }
}
