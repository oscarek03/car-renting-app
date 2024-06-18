package com.example.carrentingapp;

import javafx.beans.property.*;

import javax.persistence.*;

@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private int car_id;

    @Column(name = "brand", nullable = false, length = 50)
    private String brand;

    @Column(name = "model", nullable = false, length = 50)
    private String model;

    @Column(name = "year_of_production", nullable = false)
    private int year_of_production;

    @Column(name = "registration_number", nullable = false, length = 15)
    private String registration_number;

    @Column(name = "condition", nullable = false, length = 20)
    private String condition;

    @Column(name = "status", nullable = false, length = 50)
    private String status = "Not rented";

    @Column(name = "daily_rate", nullable = false)
    private double daily_rate;




    public Car() {
    }

    // Getters & setters

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getYear_of_production() {
        return year_of_production;
    }

    public void setYear_of_production(int yearOfProduction) {
        this.year_of_production = yearOfProduction;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getDaily_rate() {
        return daily_rate;
    }

    public void setDaily_rate(double daily_rate) {
        this.daily_rate = daily_rate;
    }

    // Properties to be used in TableView

    public StringProperty brandProperty() {
        return new SimpleStringProperty(this.brand);
    }

    public StringProperty modelProperty() {
        return new SimpleStringProperty(this.model);
    }

    public StringProperty statusProperty() {
        return new SimpleStringProperty(this.status);
    }

    public IntegerProperty year_of_productionProperty() {
        return new SimpleIntegerProperty(this.year_of_production);
    }

    public StringProperty registration_numberProperty() {
        return new SimpleStringProperty(this.registration_number);
    }

    public StringProperty conditionProperty() {
        return new SimpleStringProperty(this.condition);
    }

    public DoubleProperty daily_rateProperty() {
        return new SimpleDoubleProperty(this.daily_rate);
    }

    //Provides a string representation of the car
    @Override
    public String toString() {
        return String.format("%s %s, Registration: %s", brand, model, registration_number);
    }
}
