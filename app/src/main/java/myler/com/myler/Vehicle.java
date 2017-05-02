package myler.com.myler;


import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Vehicle {

    public int user, year;
    public String vin, make, model;
    public int original_miles;
    public int last_oil_change;

    public Vehicle() {}

    public Vehicle(String make) {
        this.make = make;
        year = 0;
    }

    public Vehicle(String vin, String make, String model){
        this.vin = vin;
        this.make = make;
        this.model = model;
        year = 0;
    }

    public Vehicle(String vin, String make, String model, int year, int original_miles, int last_oil_change) {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.original_miles = original_miles;
        this.last_oil_change = last_oil_change;
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

    public int getLast_oil_change() {
        return last_oil_change;
    }

    public void setLast_oil_change(int last_oil_change) {
        this.last_oil_change = last_oil_change;
    }
}
