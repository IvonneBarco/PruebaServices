package com.sportscontrol.ennovic.pruebaservices;

import java.io.Serializable;

public class Datos2 implements Serializable {
    private int id;
    private String Nombre;
    private String imagen;

    public Datos2(int id, String nombre, String imagen) {
        this.id = id;
        Nombre = nombre;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}