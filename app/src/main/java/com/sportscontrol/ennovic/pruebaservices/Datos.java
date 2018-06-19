package com.sportscontrol.ennovic.pruebaservices;

import java.io.Serializable;

public class Datos implements Serializable {

    private int id;
    private String titulo;
    private String detalle;
    private String imagen;
    private String semestre;
    private String programa;

    public Datos(int id, String titulo, String detalle, String imagen, String semestre, String programa) {
        this.id = id;
        this.titulo = titulo;
        this.detalle = detalle;
        this.imagen = imagen;
        this.semestre = semestre;
        this.programa = programa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }
}
