package com.itt.designpatterns.vehicleservicemanagement;

public class VehicleServiceFactory {

    public static VehicleService getService(String vehicleType) {
        return switch (vehicleType.toLowerCase()) {
            case "car" -> new CarService();
            case "bike" -> new BikeService();
            case "truck" -> new TruckService();
            default -> throw new IllegalArgumentException("Invalid vehicle type: " + vehicleType);
        };
    }

}
