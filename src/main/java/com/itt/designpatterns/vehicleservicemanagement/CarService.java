package com.itt.designpatterns.vehicleservicemanagement;

public class CarService implements VehicleService {

    @Override
    public void doService() {
        System.out.println("Car service started");
    }

}
