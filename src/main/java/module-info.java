module com.example.carrentingapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.persistence;
    requires org.hibernate.orm.core;

    requires java.naming;
    requires java.sql;
    requires jbcrypt;
    requires javafx.base;

    opens com.example.carrentingapp to javafx.fxml, org.hibernate.orm.core; // dodane org.hibernate.orm.core
    exports com.example.carrentingapp;
}
