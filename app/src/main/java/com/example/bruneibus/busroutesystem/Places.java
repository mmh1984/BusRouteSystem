package com.example.bruneibus.busroutesystem;


public class Places {
    String id;
    String landmark;
    String type;
    String area;
    String lat;
    String lng;

    public Places(String id, String landmark, String type, String area, String lat, String lng) {
        this.id = id;
        this.landmark = landmark;
        this.type = type;
        this.area = area;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public String getLandmark() {
        return landmark;
    }

    public String getType() {
        return type;
    }

    public String getArea() {
        return area;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }
}
