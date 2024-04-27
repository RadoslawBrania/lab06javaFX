package com.example.demo1;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CarShowroomController implements Initializable {

    ObservableList <Vehicle> lista=FXCollections.observableArrayList();
    @FXML
    private TableColumn<Vehicle,String> showroomColumn;
    @FXML
    private TableView<Vehicle> tableView;
    @FXML
    private TableColumn<Vehicle, String> brandColumn;
    @FXML
    private TableColumn<Vehicle, String> modelColumn;
    @FXML
    private TableColumn<Vehicle, ItemCondition> conditionColumn;
    @FXML
    private TableColumn<Vehicle, Double> priceColumn;
    @FXML
    private TableColumn<Vehicle, Integer> yearColumn;
    @FXML
    private TableColumn<Vehicle, Integer> rangeColumn;
    @FXML
    private TableColumn<Vehicle, Double> mileageColumn;
    @FXML
    private TableColumn<Vehicle, Double> engineCapacityColumn;
    @FXML
    private TableColumn<Vehicle,Boolean> isReserved;
    @FXML
    private Button addShowroomButton;
    @FXML
    private Button reserveButton;
    @FXML
    private void reserveVehicle(){
        Vehicle selectedVehicle = tableView.getSelectionModel().getSelectedItem();
        selectedVehicle.setIsReserved(true);
    }
    @FXML
    private ChoiceBox<String> showroomChoiceBox;
    @FXML
    private Button removeVehicleButton;
    @FXML
    private void removeVehicle() {
        Vehicle selectedVehicle = tableView.getSelectionModel().getSelectedItem();
        if (selectedVehicle != null && !selectedVehicle.isIsReserved()) {
            String selectedShowroomName = selectedVehicle.getShowroom().getName();
            //!"Wszystkie".equals(selectedShowroomName) &&
            if (selectedShowroomName != null) {
                CarShowroom selectedShowroom = showroomContainer.getShowrooms().get(selectedShowroomName);
                if (selectedShowroom != null) {
                    selectedShowroom.removeProduct(selectedVehicle);
                    loadShowroom();
                }
            }
        }
    }
    @FXML
    private Button importButton;
    @FXML
    private void importShowroom(){
        Dialog<Pair<String, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Add New Showroom");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create labels and text fields for showroom name and max capacity
        TextField nameField = new TextField();
        nameField.setPromptText("Showroom Name");
        TextField capacityField = new TextField();
        capacityField.setPromptText("Max Capacity");

        // Add labels and text fields to the dialog pane
        dialog.getDialogPane().setContent(new VBox(10, new Label("Name:"), nameField, new Label("Max Capacity:"), capacityField));

        // Convert the result to a pair when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return new Pair<>(nameField.getText(), Integer.parseInt(capacityField.getText()));
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            String name = result.getKey();
            int maxCapacity = result.getValue();
            CarShowroom newshowroom = CarShowroom.importFromFile(name);
            showroomContainer.addExisting(newshowroom);
            updateShowroomChoiceBox();
        });
    }
    @FXML
    private Button exportButton;
    @FXML
    private void exportShowroom(){
        String selectedShowroomName = showroomChoiceBox.getValue();
        CarShowroom selectedShowroom = showroomContainer.getShowrooms().get(selectedShowroomName);
        selectedShowroom.exportToFile();
    }
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private void search() {
        String searchQuery = searchField.getText().toLowerCase();
        String selectedShowroomName = showroomChoiceBox.getValue();
        if ("Wszystkie".equals(selectedShowroomName)) {
            List<Vehicle> matchingVehicles = showroomContainer.getShowrooms().values().stream()
                    .flatMap(showroom -> showroom.getVehicles().stream())
                    .filter(vehicle -> vehicle.getModel().toLowerCase().contains(searchQuery))
                    .collect(Collectors.toList());
            tableView.getItems().setAll(matchingVehicles);
        } else if (selectedShowroomName != null) {
            CarShowroom selectedShowroom = showroomContainer.getShowrooms().get(selectedShowroomName);
            if (selectedShowroom != null) {
                List<Vehicle> matchingVehicles = selectedShowroom.searchPartial(searchQuery);
                tableView.getItems().setAll(matchingVehicles);
            }
        }
    }
    @FXML
    private  Button addVehicleButton;
    private CarShowroomContainer showroomContainer;

    public void initData() {
        lista.addAll(new Vehicle("Toyota", "Camry", ItemCondition.NEW, 30000, 2022, 0, 2.5,0));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showroomContainer = new CarShowroomContainer();
        tableView.setRowFactory(tv -> {
            TableRow<Vehicle> row = new TableRow<>();
            Tooltip tooltip = new Tooltip();
            row.hoverProperty().addListener((observable, oldValue, newValue) -> {
                if (row.getItem() != null) {
                    tooltip.setText("Marka: " + row.getItem().getShowroom().getName() +
                            "\nModel: " + row.getItem().getModel() +
                            "\nCena: " + row.getItem().getPrice());
                    Tooltip.install(row, tooltip);
                }
            });
            return row;
        });
        // Add initial showroom with name "Main Showroom" and max capacity 20
        showroomContainer.addCenter("Main Showroom", 20);

        // Add sample vehicles to the initial showroom
        CarShowroom mainShowroom = showroomContainer.getShowrooms().get("Main Showroom");
        mainShowroom.addProduct(new Vehicle("Toyota", "Camry", ItemCondition.NEW, 30000, 2022, 0, 2.5, 0));
        mainShowroom.addProduct(new Vehicle("Ford", "Fusion", ItemCondition.USED, 20000, 2019, 25000, 2.0, 0));
        mainShowroom.addProduct(new Vehicle("Ford", "Fusion", ItemCondition.USED, 20000, 2019, 25000, 2.0, 0));
        mainShowroom.addProduct(new Vehicle("Honda", "Accord", ItemCondition.NEW, 32000, 2023, 0, 2.0, 0));


        // Add initial showroom with name "Main Showroom" and max capacity 20
        showroomContainer.addCenter("Showroom2", 20);

        showroomContainer.addCenter("Wszystkie", 41828383);
        // Add sample vehicles to the initial showroom
        CarShowroom Showroom2 = showroomContainer.getShowrooms().get("Showroom2");
        Showroom2.addProduct(new Vehicle("Toyota", "Camry", ItemCondition.NEW, 30000, 2022, 0, 2.5, 0));

        // Initialize choice box with showrooms
        updateShowroomChoiceBox();
        // Initialize table columns
        showroomColumn.setCellValueFactory(cellData -> cellData.getValue().getShowroom().showroomNamePProperty());
        brandColumn.setCellValueFactory(cellData -> cellData.getValue().brandProperty());
        modelColumn.setCellValueFactory(cellData -> cellData.getValue().modelProperty());
        conditionColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCondition()));
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asObject());
        rangeColumn.setCellValueFactory(cellData -> cellData.getValue().rangeProperty().asObject());
        mileageColumn.setCellValueFactory(cellData -> cellData.getValue().mileageProperty().asObject());
        engineCapacityColumn.setCellValueFactory(cellData -> cellData.getValue().engineCapacityProperty().asObject());
        isReserved.setCellValueFactory(cellData -> cellData.getValue().isReservedProperty().asObject());

        // Load initial showroom

        loadShowroom();
        showroomChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadShowroom();
            }
        });
    }


    @FXML
    private void addShowroom() {
        Dialog<Pair<String, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Add New Showroom");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create labels and text fields for showroom name and max capacity
        TextField nameField = new TextField();
        nameField.setPromptText("Showroom Name");
        TextField capacityField = new TextField();
        capacityField.setPromptText("Max Capacity");

        // Add labels and text fields to the dialog pane
        dialog.getDialogPane().setContent(new VBox(10, new Label("Name:"), nameField, new Label("Max Capacity:"), capacityField));

        // Convert the result to a pair when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return new Pair<>(nameField.getText(), Integer.parseInt(capacityField.getText()));
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            String name = result.getKey();
            int maxCapacity = result.getValue();
            // Add the new showroom to the container
            showroomContainer.addCenter(name, maxCapacity);
            updateShowroomChoiceBox();
        });
    }
    @FXML
    private void addVehicle() {
        // Create a dialog for adding a new vehicle
        Dialog<Vehicle> dialog = new Dialog<>();
        dialog.setTitle("Dodaj Samochód");

        // Set the button types
        ButtonType addButton = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create labels and text fields for vehicle details
        TextField brandField = new TextField();
        brandField.setPromptText("Marka");
        TextField modelField = new TextField();
        modelField.setPromptText("Model");
        ComboBox<ItemCondition> conditionComboBox = new ComboBox<>();
        conditionComboBox.getItems().addAll(ItemCondition.values());
        conditionComboBox.setPromptText("Stan");
        TextField priceField = new TextField();
        priceField.setPromptText("Cena");
        TextField yearField = new TextField();
        yearField.setPromptText("Rok");
        TextField mileageField = new TextField();
        mileageField.setPromptText("Przebieg");
        TextField engineCapacityField = new TextField();
        engineCapacityField.setPromptText("Pojemność silnika");
        TextField rangeField = new TextField();
        rangeField.setPromptText("Zasięg (jeśli elektryczny)");

        // Add labels and input fields to the dialog pane
        dialog.getDialogPane().setContent(new VBox(10,
                new Label("Marka:"), brandField,
                new Label("Model:"), modelField,
                new Label("Stan:"), conditionComboBox,
                new Label("Cena:"), priceField,
                new Label("Rok:"), yearField,
                new Label("Przebieg:"), mileageField,
                new Label("Pojemność silnika:"), engineCapacityField,
                new Label("Zasięg (jeśli elektryczny):"), rangeField));

        // Convert the result to a Vehicle object when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                try {
                    String brand = brandField.getText();
                    String model = modelField.getText();
                    ItemCondition condition = conditionComboBox.getValue();
                    double price = Double.parseDouble(priceField.getText());
                    int year = Integer.parseInt(yearField.getText());
                    double mileage = Double.parseDouble(mileageField.getText());
                    double engineCapacity = Double.parseDouble(engineCapacityField.getText());
                    int range = rangeField.getText().isEmpty() ? 0 : Integer.parseInt(rangeField.getText());
                    String selectedShowroomName = showroomChoiceBox.getValue();
                    if(selectedShowroomName.equals("Wszystkie")){
                        return new Vehicle(brand, model, condition, price, year, mileage, engineCapacity, range);
                    }
                    return new Vehicle(brand, model, condition, price, year, mileage, engineCapacity, range,showroomContainer.getShowrooms().get(selectedShowroomName));
                } catch (NumberFormatException e) {
                    // If there's an error in parsing input, return null
                    return null;
                }
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        dialog.showAndWait().ifPresent(result -> {
            String selectedShowroomName = showroomChoiceBox.getValue();
            if (selectedShowroomName != null) {
                CarShowroom selectedShowroom = showroomContainer.getShowrooms().get(selectedShowroomName);
                if (selectedShowroom != null) {
                    selectedShowroom.addProduct(result);
                    loadShowroom();
                }
            }
        });
    }
    @FXML
    private void loadShowroom() {
        String selectedShowroomName = showroomChoiceBox.getValue();
        if ("Wszystkie".equals(selectedShowroomName)) {
            List<Vehicle> allVehicles = showroomContainer.getShowrooms().values().stream()
                    .flatMap(showroom -> showroom.getVehicles().stream())
                    .collect(Collectors.toList());
            tableView.getItems().setAll(allVehicles);
        } else if (selectedShowroomName != null) {
            CarShowroom selectedShowroom = showroomContainer.getShowrooms().get(selectedShowroomName);
            if (selectedShowroom != null) {
                List<Vehicle> vehicles = selectedShowroom.getVehicles();
                tableView.getItems().setAll(vehicles);
            }
        }
    }

    private void updateShowroomChoiceBox() {
        List<String> showroomNames = new ArrayList<>(showroomContainer.getShowrooms().keySet());
        showroomChoiceBox.getItems().setAll(showroomNames);
        showroomChoiceBox.setValue("Wszystkie"); // Set "Wszystkie" as default selected option
    }
}