package com.example.datawork.model;

public class User {
    String  key, nombre, numero;
    public User() {
        // Constructor vacío requerido por Firebase
    }

    public User(String nombre, String numero) {
        this.nombre = nombre;
        this.numero = numero;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNumero() {
        return numero;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
