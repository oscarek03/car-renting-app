package com.example.carrentingapp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.IOException;


public class HelloController {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;

    @FXML
    protected void loginUser() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = null;

        try {
            String login = loginField.getText();
            String password = passwordField.getText();

            if (login.isEmpty() || password.isEmpty()) {
                showAlert("Error!", "Login and Password cannot be empty.");
                return;
            }

            transaction = session.beginTransaction();

            String hql = "FROM User WHERE login = :login";
            Query<User> query = session.createQuery(hql);
            query.setParameter("login", login);
            User user = query.uniqueResult();

            if (user == null) {
                showAlert("Error!", "No such login! The login you entered does not exist. Please try again.");
            } else if (checkPassword(password, user.getPassword())) {
                System.out.println("Logowanie pomyslnie");
                goToHomeScreen();
            } else {
                showAlert("Error!", "Incorrect Password! The password you entered is incorrect. Please try again.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void goToHomeScreen() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/carrentingapp/HomeController.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 1080, 720);

            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.setTitle("Car renting app");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean checkPassword(String inputPassword, String storedHashedPassword) {
        // Implementacja sprawdzania hasła (porównanie hashy)
        return BCrypt.checkpw(inputPassword, storedHashedPassword);
    }

    public class PasswordUtil {
        // Metoda do haszowania hasła
        public static String hashPassword(String password) {
            return BCrypt.hashpw(password, BCrypt.gensalt());
        }
    }
    @FXML
    public void createAccount() {

       // Tworzenie sesji
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Tworzenie nowego użytkownika z danymi z pól tekstowych (lub gotowymi danymi)
            User user = new User();
            String login = "ob131409"; // Przykładowy login
            String rawPassword = "123321@"; // Przykładowe hasło
            String hashedPassword = PasswordUtil.hashPassword(rawPassword); // Haszowanie hasła

            user.setLogin(login);
            user.setPassword(hashedPassword);
            user.setRole("ADMIN");

            session.save(user);

            transaction.commit();
            System.out.println("Użytkownik został dodany do bazy danych.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Dodatkowe logowanie błędu
        } finally {
            session.close();
        }
    }

}