package com.example.carrentingapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class HomeController {

    @FXML
    private TableView<Car> carsTable;

    @FXML
    private TableColumn<Car, String> brandColumn;

    @FXML
    private TableColumn<Car, String> modelColumn;

    @FXML
    private TableColumn<Car, Integer> year_of_productionColumn;

    @FXML
    private TableColumn<Car, String> registration_numberColumn;

    @FXML
    private TableColumn<Car, String> conditionColumn;

    @FXML
    private TableColumn<Car, Double> daily_rateColumn;

    @FXML
    private ComboBox<String> condition;

    @FXML
    private TableView<Client> clientsTable;


    @FXML
    private TableColumn<Client, String> nameColumn;

    @FXML
    private TableColumn<Client, String> surnameColumn;

    @FXML
    private TableColumn<Client, String> emailColumn;

    @FXML
    private TableColumn<Client, String> phone_numberColumn;

    @FXML
    private TableColumn<Client, String> addressColumn;

    @FXML
    private TableColumn<Client, String> peselColumn;

    @FXML
    private TextField name, surname, email, phone_number, address, pesel;

    @FXML
    protected void initialize() {
        // Inicjalizacja TableView i TableColumn dla samochodów
        if (carsTable != null) {
            brandColumn.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
            modelColumn.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
            year_of_productionColumn.setCellValueFactory(cellData -> cellData.getValue().year_of_productionProperty().asObject());
            registration_numberColumn.setCellValueFactory(cellData -> cellData.getValue().registration_numberProperty());
            conditionColumn.setCellValueFactory(cellData -> cellData.getValue().conditionProperty());
            daily_rateColumn.setCellValueFactory(cellData -> cellData.getValue().daily_rateProperty().asObject());
        } else {
            System.err.println("carsTable is null. Check FXML file or initialization.");
        }

        // Inicjalizacja ComboBox dla stanu samochodu
        if (condition != null) {
            condition.getItems().addAll("Excellent", "Average", "Poor");
        } else {
            System.err.println("stan ComboBox is null. Check FXML file or initialization.");
        }

        // Inicjalizacja TableView i TableColumn dla klientów
        if (clientsTable != null) {
            nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
            surnameColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
            emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
            phone_numberColumn.setCellValueFactory(cellData -> cellData.getValue().phone_numberProperty());
            addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
            peselColumn.setCellValueFactory(cellData -> cellData.getValue().peselProperty());
        } else {
            System.err.println("clientsTable is null. Check FXML file or initialization.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Metoda do wczytania danych do tabeli
    @FXML
    protected void showCars() {
        if (carsTable != null) {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();

                String hql = "FROM Car";
                Query<Car> query = session.createQuery(hql, Car.class);
                List<Car> cars = query.list();

                carsTable.getItems().clear(); // Wyczyść tabelę przed dodaniem nowych danych
                carsTable.getItems().addAll(cars); // Dodaj dane samochodów do tabeli

                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                session.close();
            }
        } else {
            System.err.println("carsTable is null. Cannot show cars.");
        }
    }

    @FXML
    private TextField brand, model, year_of_production, registration_number, daily_rate;

    @FXML
    public void addNewCar() {
        if (brand.getText().isEmpty() ||
                model.getText().isEmpty() ||
                year_of_production.getText().isEmpty() ||
                registration_number.getText().isEmpty() ||
                condition.getValue() == null || // Pobieranie wartości z ComboBox
                daily_rate.getText().isEmpty()) {
            // Jedna z wartości jest pusta, więc wyświetlamy alert i przerywamy dalsze działania
            showAlert("Error!", "All fields must be filled.");
            return; // Przerwanie działania metody
        }

        // Regex'y
        String yearPattern = "^(19|20)\\d{2}$";
        String dailyRatePattern = "^[0-9]*\\.?[0-9]+$";

        if (!year_of_production.getText().matches(yearPattern)) {
            showAlert("Error!", "Year of production must be a valid year (e.g., 1999, 2020).");
            return;
        }

        if (!daily_rate.getText().matches(dailyRatePattern)) {
            showAlert("Error!", "Daily rate must be a valid number.");
            return;
        }

        // Tworzenie sesji
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Tworzenie nowego samochodu z danymi z pól tekstowych
            Car car = new Car();
            car.setBrand(brand.getText());
            car.setModel(model.getText());
            car.setYear_of_production(Integer.parseInt(year_of_production.getText()));
            car.setRegistration_number(registration_number.getText());
            car.setCondition(condition.getValue()); // Ustawienie wartości z ComboBox
            car.setDaily_rate(Double.parseDouble(daily_rate.getText()));

            // Zapisanie samochodu do bazy danych
            session.save(car);

            transaction.commit();
            System.out.println("Samochód został dodany do bazy danych.");

            // Odświeżenie tabeli samochodów
            showCars();

            // Czyszczeni pól po poprawnym dodaniu
            brand.clear();
            model.clear();
            year_of_production.clear();
            registration_number.clear();
            condition.setValue(null); // Ustawienie ComboBox na wartość domyślną
            daily_rate.clear();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace(); // Dodatkowe logowanie błędu
        } finally {
            session.close();
        }
    }

    @FXML
    protected void showClients() {
        if (clientsTable != null) {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();

                String hql = "FROM Client";
                Query<Client> query = session.createQuery(hql, Client.class);
                List<Client> clients = query.list();

                clientsTable.getItems().clear(); // Wyczyść tabelę przed dodaniem nowych danych
                clientsTable.getItems().addAll(clients); // Dodaj dane klientów do tabeli

                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                session.close();
            }
        } else {
            System.err.println("clientsTable is null. Cannot show clients.");
        }
    }

    @FXML
    protected void deleteSelectedClient() {
        Client selectedClient = clientsTable.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.delete(selectedClient);
                transaction.commit();
                clientsTable.getItems().remove(selectedClient);
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }

    // Metoda do dodawania nowego klienta
    @FXML
    public void addNewClient() {
        if (name.getText().isEmpty() ||
                surname.getText().isEmpty() ||
                email.getText().isEmpty() ||
                phone_number.getText().isEmpty() ||
                address.getText().isEmpty() ||
                pesel.getText().isEmpty()){
            showAlert("Error!", "All fields must be filled.");
            return;
        }

        // Regex'y
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        String phonePattern = "^\\d{9}$";
        String peselPattern = "^\\d{11}$";

        // Walidacje
        if (!email.getText().matches(emailPattern)) {
            showAlert("Error!", "Invalid email format.");
            return;
        }

        if (!phone_number.getText().matches(phonePattern)) {
            showAlert("Error!", "Phone number must be exactly 9 digits.");
            return;
        }

        if (!pesel.getText().matches(peselPattern)) {
            showAlert("Error!", "PESEL number must be exactly 11 digits.");
            return;
        }


        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            Client client = new Client();
            client.setName(name.getText());
            client.setSurname(surname.getText());
            client.setEmail(email.getText());
            client.setPhone_number(phone_number.getText());
            client.setAddress(address.getText());
            client.setPesel(pesel.getText());

            session.save(client);

            transaction.commit();
            System.out.println("Klient został dodany do bazy danych.");

            showClients(); // Odświeżenie tabeli klientów

            // Czyszczenie pól po poprawnym dodaniu
            name.clear();
            surname.clear();
            email.clear();
            phone_number.clear();
            address.clear();
            pesel.clear();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    @FXML
    public void deleteSelectedCar() {
        Car selectedCar = carsTable.getSelectionModel().getSelectedItem();
        if (selectedCar == null) {
            showAlert("Error!", "No car selected.");
            return;
        }

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            session.delete(selectedCar);

            transaction.commit();
            System.out.println("Samochód został usunięty z bazy danych.");

            // Odświeżenie tabeli samochodów
            showCars();
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
