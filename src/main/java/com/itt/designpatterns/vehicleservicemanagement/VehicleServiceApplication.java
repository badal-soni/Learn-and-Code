package com.itt.designpatterns.vehicleservicemanagement;

public class VehicleServiceApplication {

    public static void main(String[] args) {
        VehicleService carService = VehicleServiceFactory.getService("car");
        carService.doService();

        VehicleService bikeService = VehicleServiceFactory.getService("bike");
        bikeService.doService();

        VehicleService truckService = VehicleServiceFactory.getService("truck");
        truckService.doService();
    }

}
