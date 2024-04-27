package com.example.demo1;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
enum ItemCondition {
    NEW, USED, DAMAGED
}

class Vehicle implements Comparable<Vehicle>,Serializable {
    private transient CarShowroom showroom;
    private transient BooleanProperty isReserved;
    private boolean isReservedSerialize;
    private transient StringProperty brand;
    private String brandSerialize;
    private transient StringProperty model;
    private String modelSerialize;
    private transient ItemCondition condition;
    private transient DoubleProperty price;
    private double priceSerialize;
    private transient IntegerProperty year;
    private int yearSerialize;
    private transient DoubleProperty mileage;
    private double mileageSerialize;
    private transient DoubleProperty engineCapacity;
    private double engineCapacitySerialize;
    private transient IntegerProperty range;
    private int rangeSerialize;
    public boolean isIsReserved() {
        return isReserved.get();
    }

    public BooleanProperty isReservedProperty() {
        return isReserved;
    }

    public void setIsReserved(boolean isReserved) {
        this.isReserved.set(isReserved);
    }

    public CarShowroom getShowroom() {
        return showroom;
    }

    public void setShowroom(CarShowroom showroom) {
        this.showroom = showroom;
    }

    public String getBrand() {
        return brand.get();
    }

    public StringProperty brandProperty() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand.set(brand);
    }

    public String getModel() {
        return model.get();
    }

    public StringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public ItemCondition getCondition() {
        return condition;
    }

    public void setCondition(ItemCondition condition) {
        this.condition = condition;
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public int getYear() {
        return year.get();
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public double getMileage() {
        return mileage.get();
    }

    public DoubleProperty mileageProperty() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage.set(mileage);
    }

    public double getEngineCapacity() {
        return engineCapacity.get();
    }

    public DoubleProperty engineCapacityProperty() {
        return engineCapacity;
    }

    public void setEngineCapacity(double engineCapacity) {
        this.engineCapacity.set(engineCapacity);
    }

    public int getRange() {
        return range.get();
    }

    public IntegerProperty rangeProperty() {
        return range;
    }

    public void setRange(int range) {
        this.range.set(range);
    }

    public Vehicle(){

    }
    public Vehicle(String brand, String model, ItemCondition condition, double price, int year, double mileage, double engineCapacity, int range) {
        this.brand = new SimpleStringProperty(brand);
        this.brandSerialize = brand;
        this.model = new SimpleStringProperty(model);
        this.modelSerialize = model;
        this.condition = condition;
        this.price = new SimpleDoubleProperty(price);
        this.priceSerialize = price;
        this.year = new SimpleIntegerProperty(year);
        this.yearSerialize = year;
        this.mileage = new SimpleDoubleProperty(mileage);
        this.mileageSerialize = mileage;
        this.engineCapacity = new SimpleDoubleProperty( engineCapacity);
        this.engineCapacitySerialize = engineCapacity;
        this.isReserved = new SimpleBooleanProperty(false);
        this.isReservedSerialize = false;
        this.range = new SimpleIntegerProperty(range);
        this.rangeSerialize = range;
        this.showroom = null;
    }
    public Vehicle(String brand, String model, ItemCondition condition, double price, int year, double mileage, double engineCapacity, int range,CarShowroom showroom) {
        this.brand = new SimpleStringProperty(brand);
        this.brandSerialize = brand;
        this.model = new SimpleStringProperty(model);
        this.modelSerialize = model;
        this.condition = condition;
        this.price = new SimpleDoubleProperty(price);
        this.priceSerialize = price;
        this.year = new SimpleIntegerProperty(year);
        this.yearSerialize = year;
        this.mileage = new SimpleDoubleProperty(mileage);
        this.mileageSerialize = mileage;
        this.engineCapacity = new SimpleDoubleProperty( engineCapacity);
        this.engineCapacitySerialize = engineCapacity;
        this.isReserved = new SimpleBooleanProperty(false);
        this.isReservedSerialize = false;
        this.range = new SimpleIntegerProperty(range);
        this.rangeSerialize = range;
        this.showroom = showroom;
    }

    public void print() {
        System.out.println("com.example.carshowroom.Vehicle: " + brand + " " + model +
                "\nCondition: " + condition +
                "\nPrice: " + price +
                "\nYear: " + year +
                "\nMileage: " + mileage +
                "\nEngine Capacity: " + engineCapacity);
    }
    public boolean isElectric(){
        return false;
    }


    @Override
    public int compareTo(Vehicle other) {
        return this.brand.toString().compareTo(other.brand.toString());
    }


}

class CarShowroom implements  Serializable{
    private static final long serialVersionUID = 1L;
    private String showroomName;
    private transient StringProperty showroomNameP;
    private ArrayList<Vehicle> vehicles;
    private int maxCapacity;
    public void initializeFromCSV(String csvFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // Assuming CSV format: brand,model,condition,price,year,mileage,engineCapacity,range
                String brand = data[0];
                String model = data[1];
                ItemCondition condition = ItemCondition.valueOf(data[2]);
                double price = Double.parseDouble(data[3]);
                int year = Integer.parseInt(data[4]);
                double mileage = Double.parseDouble(data[5]);
                double engineCapacity = Double.parseDouble(data[6]);
                int range = Integer.parseInt(data[7]); // Assuming range is the 8th column in CSV
                Vehicle vehicle = new Vehicle(brand, model, condition, price, year, mileage, engineCapacity, range,this);
                addProduct(vehicle);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setVehicles(ArrayList<Vehicle> list){
        vehicles=list;
    }
    public void exportToFile() {
        File myFile = new File(showroomName+".src");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(myFile))) {
            outputStream.writeObject(showroomName);
            outputStream.writeObject(vehicles);
            outputStream.writeInt(maxCapacity);
            System.out.println(myFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CarShowroom importFromFile(String filename) {
        CarShowroom importedshowroom = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            importedshowroom = (CarShowroom)  inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        // Setting condition to NEW for each imported vehicle
        if (importedshowroom != null) {
            for (Vehicle vehicle : importedshowroom.vehicles) {
                vehicle.setCondition(ItemCondition.NEW);
            }
        }
        return importedshowroom;
    }

    public String getShowroomNameP() {
        return showroomNameP.get();
    }

    public StringProperty showroomNamePProperty() {
        return showroomNameP;
    }

    public void setShowroomNameP(String showroomNameP) {
        this.showroomNameP.set(showroomNameP);
    }

    public CarShowroom(String showroomName, int maxCapacity) {
        this.showroomName = showroomName;
        this.showroomNameP = new SimpleStringProperty(showroomName);
        this.maxCapacity = maxCapacity;
        this.vehicles = new ArrayList<>();
    }

    public String getName(){ return showroomName; }

    public void addProduct(Vehicle vehicle) {
        if (vehicles.size() >= maxCapacity) {
            System.err.println("Showroom capacity exceeded. Cannot add more vehicles.");
            return;
        }
        vehicle.setShowroom(this);
        vehicles.add(vehicle);
        System.out.println(vehicle.getModel());
    }

    public void getProduct(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }

    public void removeProduct(Vehicle vehicle) {
        vehicles.removeIf(v -> vehicle.getModel().equals(v.getModel()));
    }

    public List<Vehicle> getVehicles() {
//        vehicles.sort((vehicle1,vehicle2) ->{
//            int priceComparison = Double.compare(vehicle1.getPrice(), vehicle2.getPrice());
//            if (priceComparison != 0) {
//                return priceComparison;
//            }
//            int nameComparison = vehicle1.getModel().compareTo(vehicle2.getModel());
//            if (nameComparison != 0) {
//                return nameComparison;
//            }
//            return Integer.compare(vehicle1.getYear(), vehicle2.getYear());
//        });
    return vehicles;
    }

    public Vehicle search(String name) {
        for (Vehicle vehicle : vehicles) {
            if (name.compareTo(vehicle.getModel())==0) {
                return vehicle;
            }
        }
        return null;
    }

    public java.util.List<Vehicle> searchPartial(String partialName) {
        java.util.List<Vehicle> matchingVehicles = new ArrayList<>();
        for (Vehicle vehicle : vehicles) {
            if ((vehicle.getModel().toLowerCase()).contains(partialName)) {
                System.out.println(vehicle.getModel().toLowerCase());
                matchingVehicles.add(vehicle);
            }
        }
        return matchingVehicles;
    }

    public int countByCondition(ItemCondition condition) {
        int count = 0;
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getCondition() == condition) {
                count++;
            }
        }
        return count;
    }

    public void summary() {
        for (Vehicle vehicle : vehicles) {
            vehicle.print();
            System.out.println("------------");
        }
    }

    public java.util.List<Vehicle> sortByName() {
        java.util.List<Vehicle> sortedList = new ArrayList<>(vehicles);
        Collections.sort(sortedList);
        return sortedList;
    }

    public java.util.List<Vehicle> sortByAmount() {
        java.util.List<Vehicle> sortedList = new ArrayList<>(vehicles);
        sortedList.sort(Comparator.comparingInt(o -> Collections.frequency(sortedList, o)));
        Collections.reverse(sortedList);
        return sortedList;
    }

    public Vehicle max() {
        return Collections.max(vehicles, Comparator.comparingInt(o -> Collections.frequency(vehicles, o)));
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }


}

class CarShowroomContainer {
    private Map<String, CarShowroom> showrooms;

    public CarShowroomContainer() {
        this.showrooms = new HashMap<>();
    }

    public void addCenter(String name, int maxCapacity) {
        showrooms.put(name, new CarShowroom(name, maxCapacity));
    }
    public void addExisting(CarShowroom showroom) {
        showrooms.put(showroom.getName(), showroom);
    }

    public void removeCenter(String name) {
        showrooms.remove(name);
    }

    public java.util.List<String> findEmpty() {
        java.util.List<String> emptyShowrooms = new ArrayList<>();
        for (Map.Entry<String, CarShowroom> entry : showrooms.entrySet()) {
            if (entry.getValue().getVehicles().isEmpty()) {
                emptyShowrooms.add(entry.getKey());
            }
        }
        return emptyShowrooms;
    }

    public void summary() {
        for (Map.Entry<String, CarShowroom> entry : showrooms.entrySet()) {
            System.out.println("Showroom: " + entry.getKey() +
                    "\nPercentage filled: " + ((double) entry.getValue().getVehicles().size() / entry.getValue().getMaxCapacity()) * 100 + "%");
        }
    }

    public Map<String, CarShowroom> getShowrooms() {
        return showrooms;
    }
}
