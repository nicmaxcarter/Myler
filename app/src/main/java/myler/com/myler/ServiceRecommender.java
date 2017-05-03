package myler.com.myler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Max on 5/2/2017.
 */

public class ServiceRecommender {

    private List<String> longTermService, midTermService, shortTermService;

    public ServiceRecommender() {

        shortTermService = new ArrayList<>();
        shortTermService.add("Automatic Transmission Fluid Check");
        shortTermService.add("Battery and Cable Condition");
        shortTermService.add("Engine Air Filter");
        shortTermService.add("Engine Oil and Filter Replacement");
        shortTermService.add("Exhaust System Check");
        shortTermService.add("Engine Hoses Check");
        shortTermService.add("Lug Nuts/Bolts");
        shortTermService.add("Power Steering Fluid Check");
        shortTermService.add("Serpentine/Accessory Belts");

        midTermService = new ArrayList<>();
        midTermService.add("Cabin Air Filter");
        midTermService.add("Chassis Lubrication");
        midTermService.add("Engine Coolant Level, Condition");
        midTermService.add("Spark Plug Wire Inspection/Replacement");
        midTermService.add("Tire Rotation");
        midTermService.add("Wiper Blades");

        longTermService = new ArrayList<>();
        longTermService.add("Air Conditioning System Check");
        longTermService.add("Brake System Check");
        longTermService.add("Recall Check");
        longTermService.add("Spark Plug Inspection/Replacement");
        longTermService.add("Steering and Suspension Check");
        longTermService.add("Timing Belt Check");
        longTermService.add("Wheel Alignment Check");

    }

    public List<String> getPreviousServices(int currentMiles){
        List<String> list = new ArrayList<String>();
        int temp = currentMiles/3000;
        int shortMiles = temp*3000;

        for(String str : shortTermService) {
            list.add("At " + shortMiles + " miles: " + str);
        }

        temp = currentMiles/6000;
        int midMiles = temp*6000;

        for(String str : midTermService) {
            list.add("At " + midMiles + " miles: " + str);
        }

        temp = currentMiles/12000;
        int longMiles = temp*12000;

        for(String str : midTermService) {
            list.add("At " + longMiles + " miles: " + str);
        }

        return list;
    }

    public List<String> getFutureServices(int currentMiles){
        List<String> list = new ArrayList<String>();
        int temp = currentMiles/3000;
        int shortMiles = (temp+1)*3000;

        for(String str : shortTermService) {
            list.add("At " + shortMiles + " miles: " + str);
        }

        temp = currentMiles/6000;
        int midMiles = (temp+1)*6000;

        for(String str : midTermService) {
            list.add("At " + midMiles + " miles: " + str);
        }

        temp = currentMiles/12000;
        int longMiles = (temp+1)*12000;

        for(String str : midTermService) {
            list.add("At " + longMiles + " miles: " + str);
        }

        return list;
    }


}
