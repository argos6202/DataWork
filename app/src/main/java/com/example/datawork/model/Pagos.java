package com.example.datawork.model;

public class Pagos {
    String key, nombre;
    Double pago;

    public Pagos() {

    }

    public Pagos(String key, String nombre, Double pago) {
        this.key = key;
        this.nombre = nombre;
        this.pago = pago;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPago() {
        return pago;
    }

    public void setPago(Double pago) {
        this.pago = pago;
    }
}
