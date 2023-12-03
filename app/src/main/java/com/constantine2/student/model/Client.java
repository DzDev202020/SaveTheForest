package com.constantine2.student.model;

import java.io.Serializable;
import java.sql.Date;

public class Client implements Serializable {

    int id;

    String firstName;
    String lastName;
    String email;
    Date birthDay;
    String password;
    String phoneNumber;

    int reputation;


    public Client() {
    }

    public Client(int id, String firstName, String lastName, String email, Date birthDay, String password, String phoneNumber, int reputation) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDay = birthDay;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.reputation = reputation;
    }

    public Client(int id, String firstName, String lastName, String email, Date birthDay, String password, String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDay = birthDay;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public Client(String firstName, String lastName, String email, Date birthDay, String password, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthDay = birthDay;
        this.password = password;
        this.phoneNumber = phoneNumber;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }
}
