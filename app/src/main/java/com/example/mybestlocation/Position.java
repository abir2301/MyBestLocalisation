package com.example.mybestlocation;

public class Position {
    int id ;
    String longitude;
    String latitude;
    String description;

    public Position(int id, String longitude, String latitude, String description) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
