package com.example.bruneibus.busroutesystem;


public class BusClass {

    String busno;

    String routes;

    public BusClass(String busno, String routes) {
        this.busno = busno;
        this.routes = routes;
    }

    public String getBusno() {
        return busno;
    }

    public String getRoutes() {
        return routes;
    }
}
