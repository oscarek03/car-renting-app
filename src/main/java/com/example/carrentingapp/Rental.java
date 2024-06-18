package com.example.carrentingapp;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "rental")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private int rental_id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "start_date")
    private String start_date;

    @Column(name = "end_date")
    private String end_date;

    @Column(name = "price")
    private Double price; // Dodane pole price

    public int getRental_id() {
        return rental_id;
    }

    public void setRental_id(int rental_id) {
        this.rental_id = rental_id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long calculateDaysBetween() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(start_date, formatter);
        LocalDate end = LocalDate.parse(end_date, formatter);
        return ChronoUnit.DAYS.between(start, end);
    }

    public double calculateCost() {
        return calculateDaysBetween() * car.getDaily_rate();
    }

    @Override
    public String toString() {
        return "Rental{" +
                client.getName() + " " + client.getSurname() + " " + car.getBrand() + " " + car.getModel() + " " + car.getRegistration_number() + " " + calculateDaysBetween() +
                '}';
    }

    // Metody dostępowe dla TableView
    public StringProperty nameProperty() {
        return new SimpleStringProperty(client.getName());
    }

    public StringProperty surnameProperty() {
        return new SimpleStringProperty(client.getSurname());
    }

    public StringProperty brandProperty() {
        return new SimpleStringProperty(car.getBrand());
    }

    public StringProperty modelProperty() {
        return new SimpleStringProperty(car.getModel());
    }

    public StringProperty registration_numberProperty() {
        return new SimpleStringProperty(car.getRegistration_number());
    }

    public StringProperty startDateProperty() {
        return new SimpleStringProperty(start_date);
    }

    public StringProperty endDateProperty() {
        return new SimpleStringProperty(end_date);
    }

    public StringProperty costsProperty() {
        return new SimpleStringProperty(String.valueOf(calculateCost()));
    }
}
