package com.itt.designpatterns.vehicleservicemanagement;

public class TruckService implements VehicleService {

    @Override
    public void doService() {
        System.out.println("Truck service started");
    }

}
