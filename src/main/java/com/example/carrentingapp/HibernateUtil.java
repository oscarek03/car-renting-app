package com.example.carrentingapp;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            configuration.addAnnotatedClass(com.example.carrentingapp.User.class);
            configuration.addAnnotatedClass(com.example.carrentingapp.Car.class);
            configuration.addAnnotatedClass(com.example.carrentingapp.Client.class);
            configuration.addAnnotatedClass(com.example.carrentingapp.Rental.class);

            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties());
            return configuration.buildSessionFactory(builder.build());
        } catch (Throwable ex) {
            System.err.println("Inicjalizacja SessionFactory nie powiodła się: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
