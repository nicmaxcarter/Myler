package myler.com.myler;


import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Vehicle {

    public int user, year;
    public String vin, make, model;
    public int original_miles;

    public Vehicle() {}

    public Vehicle(String make) {
        this.make = make;
    }

    public Vehicle(String vin, String make, String model){
        this.vin = vin;
        this.make = make;
        this.model = model;
    }

    public Vehicle(String vin, String make, String model, int year, int original_miles) {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.original_miles = original_miles;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("vin", vin);
        result.put("make", make);
        result.put("model", model);
        result.put("year", year);
        result.put("miles", original_miles);

        return result;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getOriginal_miles() {
        return original_miles;
    }

    public void setOriginal_miles(int original_miles) {
        this.original_miles = original_miles;
    }
}
