package com.example.carrentingapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HomeController {

    @FXML
    private TableView<Car> carsTable;

    @FXML
    private TableColumn<Car, String> brandColumn;

    @FXML
    private TableColumn<Car, String> modelColumn;

    @FXML
    private TableColumn<Car, String> statusColumn;

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
    private TextField brand, model, year_of_production, registration_number, daily_rate;

    @FXML
    private TableView<Rental> rentalsTable;

    private ObservableList<Rental> rentalList = FXCollections.observableArrayList();

    @FXML
    private TableColumn<Rental, String> name_rentalColumn;

    @FXML
    private TableColumn<Rental, String> surname_rentalColumn;

    @FXML
    private TableColumn<Rental, String> brand_rentalColumn;

    @FXML
    private TableColumn<Rental, String> model_rentalColumn;

    @FXML
    private TableColumn<Rental, String> registration_number_rentalColumn;

    @FXML
    private TableColumn<Rental, String> costs_rentalColumn;

    @FXML
    private TableColumn<Rental, String> end_date_rentalColumn;

    @FXML
    private TableColumn<Rental, String> start_date_rentalColumn;

    @FXML
    private ComboBox<Client> selectClient;

    @FXML
    private ComboBox<Car> selectCar;


    @FXML
    protected void initialize() {
        // Inicjalizacja tabel
        showCars();
        showClients();
        showRentals();

        // Ładowanie danych do ComboBoxów
        loadClientsToComboBox();
        loadCarsToComboBox();

        // Inicjalizacja TableView i TableColumn dla samochodów
        if (carsTable != null) {
            brandColumn.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
            modelColumn.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
            year_of_productionColumn.setCellValueFactory(cellData -> cellData.getValue().year_of_productionProperty().asObject());
            registration_numberColumn.setCellValueFactory(cellData -> cellData.getValue().registration_numberProperty());
            conditionColumn.setCellValueFactory(cellData -> cellData.getValue().conditionProperty());
            daily_rateColumn.setCellValueFactory(cellData -> cellData.getValue().daily_rateProperty().asObject());
            statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
            // Dodanie obsługi zmiany wartości w tableview
            carsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                // Aktualizacja statusu samochodu na podstawie wypożyczeń
                if (newValue != null) {
                    Session session = HibernateUtil.getSessionFactory().getCurrentSession();
                    Transaction transaction = null;

                    try {
                        transaction = session.beginTransaction();

                        // Sprawdzenie czy samochód jest wypożyczony
                        String hql = "SELECT r FROM Rental r WHERE r.car = :car";
                        Query<Rental> query = session.createQuery(hql, Rental.class);
                        query.setParameter("car", newValue);
                        List<Rental> rentals = query.list();

                        // Jeśli są wypożyczenia, ustaw status na "Rented", w przeciwnym razie na "Not rented"
                        if (!rentals.isEmpty()) {
                            newValue.setStatus("Rented");
                        } else {
                            newValue.setStatus("Not rented");
                        }

                        // Zapisz zmiany w bazie danych
                        session.update(newValue);
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
            });
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

        // Inicjalizacja TableView i TableColumn dla Rental
        if(rentalsTable != null){
            name_rentalColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
            surname_rentalColumn.setCellValueFactory(cellData -> cellData.getValue().surnameProperty());
            brand_rentalColumn.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
            model_rentalColumn.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
            registration_number_rentalColumn.setCellValueFactory(cellData -> cellData.getValue().registration_numberProperty());
            start_date_rentalColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
            end_date_rentalColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());
            costs_rentalColumn.setCellValueFactory(cellData -> cellData.getValue().costsProperty());
        } else {
            System.err.println("rentalsTable is null. Check FXML file or initialization.");
        }
    }


    private List<Client> getClientsFromDatabase() {
        List<Client> clients = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            String hql = "FROM Client";
            Query<Client> query = session.createQuery(hql, Client.class);
            clients = query.list();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return clients;
    }

    private List<Car> getCarsFromDatabase() {
        List<Car> cars = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            String hql = "FROM Car";
            Query<Car> query = session.createQuery(hql, Car.class);
            cars = query.list();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return cars;
    }

    private List<Car> getCarsFromDatabaseToComboBox() {
        List<Car> cars = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            String hql = "FROM Car WHERE status = :status";
            Query<Car> query = session.createQuery(hql, Car.class);
            query.setParameter("status", "Not rented");
            cars = query.list();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return cars;
    }

    private void loadClientsToComboBox() {
        List<Client> clientsList = getClientsFromDatabase(); // Use the new method
        ObservableList<Client> clients = FXCollections.observableArrayList(clientsList);
        selectClient.setItems(clients);
    }

    private void loadCarsToComboBox() {
        List<Car> carsList = getCarsFromDatabaseToComboBox(); // Use the new method
        ObservableList<Car> cars = FXCollections.observableArrayList(carsList);
        selectCar.setItems(cars);
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
            ObservableList<Car> carList = FXCollections.observableArrayList(getCarsFromDatabase());
            carsTable.setItems(carList);
        } else {
            System.err.println("carsTable is null. Check FXML file or initialization.");
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
    protected void showClients() {
        if (clientsTable != null) {
            ObservableList<Client> clientList = FXCollections.observableArrayList(getClientsFromDatabase());
            clientsTable.setItems(clientList);
        } else {
            System.err.println("clientsTable is null. Check FXML file or initialization.");
        }
    }

    @FXML
    public void deleteSelectedCar() {
        Car selectedCar = carsTable.getSelectionModel().getSelectedItem();
        if (selectedCar == null) {
            showAlert("Error!", "No car selected.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation of Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected car?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();
                session.delete(selectedCar);
                transaction.commit();
                System.out.println("Car has been deleted from the database.");

                // Refresh the cars table after deletion
                showCars();

                // Refresh ComboBox for cars after deletion
                loadCarsToComboBox();

            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace(); // Additional error logging
            } finally {
                session.close();
            }
        }
    }

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
    protected void deleteSelectedClient() {
        Client selectedClient = clientsTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert("Error", "No client selected!");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation of Deletion");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete the selected client?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();
                session.delete(selectedClient);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                session.close();
            }

            // Odświeżenie tabeli po usunięciu klienta
            showClients();

            // Odświeżenie comboBoxa po usunięciu klienta
            loadClientsToComboBox();
        }
    }

    @FXML
    protected void editCar() {
        Car selectedCar = carsTable.getSelectionModel().getSelectedItem();
        if (selectedCar == null) {
            showAlert("Error", "No car selected!");
            return;
        }

        // Tworzenie okna dialogowego do edycji
        Dialog<Car> dialog = new Dialog<>();
        dialog.setTitle("Edit Car");
        dialog.setHeaderText("Edit the selected car details");

        // Dodawanie przycisków do dialogu
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Tworzenie pól tekstowych i ich wypełnianie danymi wybranego samochodu
        TextField brandField = new TextField(selectedCar.getBrand());
        TextField modelField = new TextField(selectedCar.getModel());
        TextField yearField = new TextField(String.valueOf(selectedCar.getYear_of_production()));
        TextField registrationNumberField = new TextField(selectedCar.getRegistration_number());
        TextField conditionField = new TextField(selectedCar.getCondition());
        TextField statusField = new TextField(selectedCar.getStatus());
        TextField dailyRateField = new TextField(String.valueOf(selectedCar.getDaily_rate()));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Brand:"), 0, 0);
        grid.add(brandField, 1, 0);
        grid.add(new Label("Model:"), 0, 1);
        grid.add(modelField, 1, 1);
        grid.add(new Label("Year:"), 0, 2);
        grid.add(yearField, 1, 2);
        grid.add(new Label("Registration Number:"), 0, 3);
        grid.add(registrationNumberField, 1, 3);
        grid.add(new Label("Condition:"), 0, 4);
        grid.add(conditionField, 1, 4);
        grid.add(new Label("Status:"), 0, 5);
        grid.add(statusField, 1, 5);
        grid.add(new Label("Daily Rate:"), 0, 6);
        grid.add(dailyRateField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        // Konwersja wyniku, aby po naciśnięciu przycisku "Save" zwrócić nowy samochód
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                selectedCar.setBrand(brandField.getText());
                selectedCar.setModel(modelField.getText());
                selectedCar.setYear_of_production(Integer.parseInt(yearField.getText()));
                selectedCar.setRegistration_number(registrationNumberField.getText());
                selectedCar.setCondition(conditionField.getText());
                selectedCar.setStatus(statusField.getText());
                selectedCar.setDaily_rate(Double.parseDouble(dailyRateField.getText()));
                return selectedCar;
            }
            return null;
        });

        Optional<Car> result = dialog.showAndWait();

        result.ifPresent(editedCar -> {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();
                session.update(editedCar);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                session.close();
            }

            // Odświeżenie tabeli po edycji samochodu
            showCars();
        });
    }

    @FXML
    protected void editClient() {
        Client selectedClient = clientsTable.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            showAlert("Error", "No client selected!");
            return;
        }

        // Tworzenie okna dialogowego do edycji
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle("Edit Client");
        dialog.setHeaderText("Edit the selected client details");

        // Dodawanie przycisków do dialogu
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Tworzenie pól tekstowych i ich wypełnianie danymi wybranego klienta
        TextField nameField = new TextField(selectedClient.getName());
        TextField surnameField = new TextField(selectedClient.getSurname());
        TextField emailField = new TextField(selectedClient.getEmail());
        TextField phoneNumberField = new TextField(selectedClient.getPhone_number());
        TextField addressField = new TextField(selectedClient.getAddress());
        TextField peselField = new TextField(selectedClient.getPesel());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Surname:"), 0, 1);
        grid.add(surnameField, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(new Label("Phone Number:"), 0, 3);
        grid.add(phoneNumberField, 1, 3);
        grid.add(new Label("Address:"), 0, 4);
        grid.add(addressField, 1, 4);
        grid.add(new Label("PESEL:"), 0, 5);
        grid.add(peselField, 1, 5);

        dialog.getDialogPane().setContent(grid);

        // Konwersja wyniku, aby po naciśnięciu przycisku "Save" zwrócić zaktualizowanego klienta
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                selectedClient.setName(nameField.getText());
                selectedClient.setSurname(surnameField.getText());
                selectedClient.setEmail(emailField.getText());
                selectedClient.setPhone_number(phoneNumberField.getText());
                selectedClient.setAddress(addressField.getText());
                selectedClient.setPesel(peselField.getText());
                return selectedClient;
            }
            return null;
        });

        Optional<Client> result = dialog.showAndWait();

        result.ifPresent(editedClient -> {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();
                session.update(editedClient);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            } finally {
                session.close();
            }

            // Odświeżenie tabeli po edycji klienta
            showClients();
        });
    }


    @FXML
    protected void addRental() {
        Client client = selectClient.getSelectionModel().getSelectedItem();
        Car car = selectCar.getSelectionModel().getSelectedItem();
        LocalDate startDate;
        LocalDate endDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (client == null) {
            showAlert("Error", "Please select a client!");
            return;
        }

        if (car == null) {
            showAlert("Error", "Please select a car!");
            return;
        }

        TextInputDialog startDateDialog = new TextInputDialog();
        startDateDialog.setTitle("Enter Date");
        startDateDialog.setHeaderText("Enter start date (yyyy-MM-dd):");
        startDateDialog.setContentText("Start date:");

        Optional<String> startDateResult = startDateDialog.showAndWait();
        if (startDateResult.isPresent()) {
            try {
                startDate = LocalDate.parse(startDateResult.get(), formatter);
            } catch (DateTimeParseException e) {
                showAlert("Error", "Invalid start date format!");
                return;
            }
        } else {
            showAlert("Error", "Start date not provided!");
            return;
        }

        TextInputDialog endDateDialog = new TextInputDialog();
        endDateDialog.setTitle("Enter Date");
        endDateDialog.setHeaderText("Enter end date (yyyy-MM-dd):");
        endDateDialog.setContentText("End date:");

        Optional<String> endDateResult = endDateDialog.showAndWait();
        if (endDateResult.isPresent()) {
            try {
                endDate = LocalDate.parse(endDateResult.get(), formatter);
            } catch (DateTimeParseException e) {
                showAlert("Error", "Invalid end date format!");
                return;
            }
        } else {
            showAlert("Error", "End date not provided!");
            return;
        }

        if (startDate == null || endDate == null) {
            showAlert("Error", "Please provide both start and end dates!");
            return;
        }

        if (endDate.isBefore(startDate)) {
            showAlert("Error", "End date cannot be before start date!");
            return;
        }

        double costs = car.getDaily_rate() * (endDate.toEpochDay() - startDate.toEpochDay());

        Rental rental = new Rental();
        rental.setClient(client);
        rental.setCar(car);
        rental.setStart_date(String.valueOf(startDate));
        rental.setEnd_date(String.valueOf(endDate));
        rental.setPrice(costs);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(rental);

            car.setStatus("Rented");
            session.update(car);

            transaction.commit();

            // Usunięcie samochodu z ComboBoxa
            selectCar.getItems().remove(car);
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        showRentals();
    }



    @FXML
    protected void deleteRental() {
        Rental selectedRental = rentalsTable.getSelectionModel().getSelectedItem();

        if (selectedRental == null) {
            showAlert("Error", "Please select a rental to delete!");
            return;
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Usuń wypożyczenie
            session.delete(selectedRental);

            // Zaktualizuj status samochodu na 'Not rented'
            Car rentedCar = selectedRental.getCar();
            rentedCar.setStatus("Not rented");
            session.update(rentedCar);

            transaction.commit();
            showRentals();
            loadCarsToComboBox();
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
    protected void showRentals() {
        List<Rental> rentals = getRentalsFromDatabase();
        rentalList.setAll(rentals);
        rentalsTable.setItems(rentalList);
    }
    @FXML
    private List<Rental> getRentalsFromDatabase() {
        List<Rental> rentals = new ArrayList<>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            String hql = "FROM Rental";
            Query<Rental> query = session.createQuery(hql, Rental.class);
            rentals = query.list();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rentals;
    }

    @FXML
    protected void updateCarStatus() {
        if (rentalsTable != null) {
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();

                String hql = "FROM Rental";
                Query<Rental> query = session.createQuery(hql, Rental.class);
                List<Rental> rentals = query.list();

                rentalsTable.getItems().clear();
                rentalsTable.getItems().addAll(rentals);

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
            System.err.println("rentalsTable is null. Cannot show rentals.");
        }
    }

}
