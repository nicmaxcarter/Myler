package myler.com.myler;


public abstract class Vehicle {

    public int vin, user, year;
    public String make, model;
    public int original_miles;

    public Vehicle(int vin, String make, String model){
        this.vin = vin;
        this.make = make;
        this.model = model;
    }

    public int getVin() {
        return vin;
    }

    public void setVin(int vin) {
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
