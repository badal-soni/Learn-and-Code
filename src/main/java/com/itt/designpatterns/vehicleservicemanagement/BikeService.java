package com.itt.designpatterns.vehicleservicemanagement;

public class BikeService implements VehicleService {

    @Override
    public void doService() {
        System.out.println("Bike service started");
    }

}
