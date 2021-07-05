package com.akalinkina.storage;

import com.akalinkina.model.Car;
import com.akalinkina.model.Owner;

import java.util.*;

public class CarStorage {
    private NavigableSet<Car> storage = new TreeSet<>(Comparator.comparing(Car::getVin));

    private static final CarStorage INSTANCE = new CarStorage();

    public static CarStorage getStorage() {
        return INSTANCE;
    }

    private CarStorage() {}

    public Car addCar(String vin) throws IllegalArgumentException {
        Car car = new Car(vin);
        if (storage.contains(car)) {
            throw new IllegalArgumentException("Машина c таким vin уже существует в хранилище данных");
        }
        storage.add(car);
        return car;
    }

    public Car delete(String vin) {
        Iterator<Car> carIterator = storage.iterator();
        while (carIterator.hasNext()) {
            Car car = carIterator.next();
            if (car.getVin().equals(vin)) {
                carIterator.remove();
                return car;
            }
        }
        return null;
    }

    public Car updateOwner(String vin, Owner owner) throws IllegalArgumentException {
        Car car = getByVin(vin);
        if (car != null) {
            car.setOwner(owner);
            return car;
        }
        throw new IllegalArgumentException("Машина c таким vin не существует в хранилище данных");
    }

    public Set<Car> getAll() {
        return Collections.unmodifiableNavigableSet(this.storage);
    }

    public Car getByVin(String vin) {
        for (Car car: storage) {
            if (car.getVin().equals(vin)) {
                return car;
            }
        }
        return null;
    }

    public Set<Car> getByFullName(String fullName) {
        Set<Car> carSet = new HashSet<>();
        for (Car car: storage) {
            Owner carOwner = car.getOwner();
            if (carOwner != null && carOwner.getFullName().equals(fullName)) {
                carSet.add(car);
            }
        }
        return carSet;
    }

    public Set<Car> getById(long id) {
        Set<Car> carSet = new HashSet<>();
        for (Car car: storage) {
            Owner carOwner = car.getOwner();
            if (carOwner != null && carOwner.getId() == id) {
                carSet.add(car);
            }
        }
        return carSet;
    }

    public Map<Owner, Set<String>> mapByOwner() {
        Map<Owner, Set<String>> map = new HashMap<>();
        for (Car car: storage) {
            Owner carOwner = car.getOwner();
            if (car.getOwner() != null) {
                map.compute(carOwner, (owner, vins) ->  {
                    if (vins == null) {
                        vins = new HashSet<>();
                    }
                    vins.add(car.getVin());
                    return vins;
                });
            }
        }
        return map;
    }

    public Set<Owner> getOwners() {
        Set<Owner> owners = new HashSet<>();
        for (Car car: storage) {
            Owner carOwner = car.getOwner();
            if (carOwner != null) {
                owners.add(carOwner);
            }
        }
        return owners;
    }
}
