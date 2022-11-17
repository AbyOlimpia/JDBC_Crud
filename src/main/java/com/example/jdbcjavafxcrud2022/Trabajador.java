package com.example.jdbcjavafxcrud2022;

public class Trabajador {
    int idTrabajador;
    String nombre;
    public Trabajador(){

    }

    public Trabajador(int idTrabajador, String nombre) {
        this.idTrabajador = idTrabajador;
        this.nombre = nombre;
    }

    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
