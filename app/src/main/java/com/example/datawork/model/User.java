package com.example.datawork.model;

public class User {
    String nombre, numero;
    public User() {
        // Constructor vacío requerido por Firebase
    }

    public User(String nombre, String numero) {
        this.nombre = nombre;
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNumero() {
        return numero;
    }
}
