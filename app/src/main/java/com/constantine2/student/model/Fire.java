package com.constantine2.student.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

public class Fire implements Serializable {
    int id;

    Date createdDate;
    Date controlledAt;

    Double latitude;
    Double longitude;

    String street;
    String city;
    String country;
    String zipcode;


    int id_c;

    Client rClient;

    Set<Post> rPosts;

    Set<Scale> rScales;


    public Fire() {

    }

    public Fire(int id, Date createdDate, Date controlledAt, Double latitude, Double longitude, String street, String city, String country, String zipcode, int id_c) {
        this.id = id;
        this.createdDate = createdDate;
        this.controlledAt = controlledAt;
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
        this.city = city;
        this.country = country;
        this.zipcode = zipcode;
        this.id_c = id_c;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getControlledAt() {
        return controlledAt;
    }

    public void setControlledAt(Date controlledAt) {
        this.controlledAt = controlledAt;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getId_c() {
        return id_c;
    }

    public void setId_c(int id_c) {
        this.id_c = id_c;
    }

    public Client getrClient() {
        return rClient;
    }

    public void setrClient(Client rClient) {
        this.rClient = rClient;
    }

    public Set<Post> getrPosts() {
        return rPosts;
    }

    public void setrPosts(Set<Post> rPosts) {
        this.rPosts = rPosts;
    }

    public Set<Scale> getrScales() {
        return rScales;
    }

    public void setrScales(Set<Scale> rScales) {
        this.rScales = rScales;
    }
}
