package com.burkow.sykler;

import java.io.Serializable;

/**
 * Created by aleksanb on 5/30/13.
 */
public class Stations implements Comparable<Stations>, Serializable {
    private int id;
    private String name;
    private int availableBikes;
    private int availableSlots;
    private Boolean operational;
    private Boolean online;
    private float lat;
    private float lng;

    public Stations(int id, String name, int availableBikes, int availableSlots, Boolean operational, Boolean online, float lat, float lng) {
        this.id = id;
        this.name = name;
        this.availableBikes = availableBikes;
        this.availableSlots = availableSlots;
        this.operational = operational;
        this.online = online;
        this.lat = lat;
        this.lng = lng;
    }

    public int getSize() {
        return availableBikes + availableSlots;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAvailableBikes() {
        return availableBikes;
    }

    public void setAvailableBikes(int availableBikes) {
        this.availableBikes = availableBikes;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public Boolean getOperational() {
        return operational;
    }

    public void setOperational(Boolean operational) {
        this.operational = operational;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return id + name + availableBikes + availableSlots;
    }

    @Override
    public int compareTo(Stations stations) {
        return 1;
    }
}
