package com.example.carrentingapp;

import org.mindrot.jbcrypt.BCrypt;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;


public class HelloController {

    @FXML
    private Label loginLabel;
    @FXML
    private Button loginButton;
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

            String hql = "FROM Uzytkownik WHERE login = :login";
            Query<Uzytkownik> query = session.createQuery(hql);
            query.setParameter("login", login);
            Uzytkownik user = query.uniqueResult();

            if (user == null) {
                showAlert("Error!", "No such login! The login you entered does not exist. Please try again.");
            } else if (checkPassword(password, user.getHaslo())) {
                System.out.println("Logowanie pomyslnie");
                // tutaj można dodać dodatkową logikę po pomyślnym logowaniu
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
            Uzytkownik user = new Uzytkownik();
            String login = "ob131409"; // Przykładowy login
            String rawPassword = "123321@"; // Przykładowe hasło
            String hashedPassword = PasswordUtil.hashPassword(rawPassword); // Haszowanie hasła

            user.setLogin(login);
            user.setHaslo(hashedPassword);
            user.setRola("ADMIN");

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