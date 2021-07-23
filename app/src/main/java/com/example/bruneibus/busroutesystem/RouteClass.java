package com.example.bruneibus.busroutesystem;


public class RouteClass {
    String ID;
    String place;
    String area;
    String type;
    String lat;
    String lng;

    public RouteClass(String ID, String place, String area, String type, String lat, String lng) {
        this.ID = ID;
        this.place = place;
        this.area = area;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
    }

    public String getID() {
        return ID;
    }

    public String getPlace() {
        return place;
    }

    public String getArea() {
        return area;
    }

    public String getType() {
        return type;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
