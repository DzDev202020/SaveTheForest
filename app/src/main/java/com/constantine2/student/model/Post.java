package com.constantine2.student.model;

import java.sql.Date;

public class Post {

    int id;
    int id_c;
    int id_f;
    String content;

    String photoFilePath;
    Date createdAt;

    Client rClient;
    Fire rFire;


    public Post() {

    }

    public Post(int id, int client_idc, int fire_idf, String content, String photoFilePath, Date createdAt) {
        super();
        this.id = id;
        this.id_c = client_idc;
        this.id_f = fire_idf;
        this.content = content;
        this.photoFilePath = photoFilePath;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_c() {
        return id_c;
    }

    public void setId_c(int id_c) {
        this.id_c = id_c;
    }

    public int getId_f() {
        return id_f;
    }

    public void setId_f(int id_f) {
        this.id_f = id_f;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhotoFilePath() {
        return photoFilePath;
    }

    public void setPhotoFilePath(String photoFilePath) {
        this.photoFilePath = photoFilePath;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Client getrClient() {
        return rClient;
    }

    public void setrClient(Client rClient) {
        this.rClient = rClient;
    }

    public Fire getrFire() {
        return rFire;
    }

    public void setrFire(Fire rFire) {
        this.rFire = rFire;
    }
}


