package com.example.carrentingapp;

import javafx.beans.property.*;

import javax.persistence.*;

@Entity
@Table(name = "client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private int client_id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "surname", nullable = false, length = 50)
    private String surname;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phone_number;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "pesel", nullable = false, length = 11)
    private String pesel;


    public Client() {
    }

    // Getters & setters

    public int getClient_id(){ return client_id; };

    public void setClient_id(int client_id){
        this.client_id = client_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String adress) {
        this.address = adress;
    }


    // Properties used in TableView
    public StringProperty nameProperty() {
        return new SimpleStringProperty(this.name);
    }

    public StringProperty surnameProperty() {
        return new SimpleStringProperty(this.surname);
    }

    public StringProperty emailProperty() {
        return new SimpleStringProperty(this.email);
    }

    public StringProperty phone_numberProperty() {
        return new SimpleStringProperty(this.phone_number);
    }

    public StringProperty peselProperty() {
        return new SimpleStringProperty(this.pesel);
    }

    public StringProperty addressProperty() {
        return new SimpleStringProperty(this.address);
    }

    //Provides a string representation of the client
    @Override
    public String toString() {
        return String.format("%s %s, Phone: %s, Email: %s, PESEL: %s", name, surname, phone_number, email, pesel);
    }

}
