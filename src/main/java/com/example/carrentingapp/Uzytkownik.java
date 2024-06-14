package com.example.carrentingapp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;

@Entity
@Table(name = "uzytkownicy")
public class Uzytkownik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uzytkownik_id")
    private int id;

    @Column(name = "login", nullable = false, length = 50)
    private String login;

    @Column(name = "haslo", nullable = false, length = 255)
    private String haslo;

    @Column(name = "rola", nullable = false, length = 20)
    private String rola;

    public Uzytkownik() {
        // Bezargumentowy konstruktor
    }

    // Gettery i settery
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getHaslo() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo = haslo;
    }

    public String getRola() {
        return rola;
    }

    public void setRola(String rola) {
        this.rola = rola;
    }
}
