package com.gepardec.mega.domain.model.monthlyreport;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Vehicle {
    CAR_ACTIVE("Auto", false),
    CAR_INACTIVE("Auto (PKW passiv)", false),
    OTHER_INACTIVE("", false);

    private static final Map<String, Vehicle> idToVehicleMap = Stream.of(Vehicle.values()).collect(Collectors.toMap(Vehicle::getId, Function.identity()));

    public final String id;

    public final boolean activeTraveler;

    Vehicle(String id, boolean activeTraveler) {
        this.id = id;
        this.activeTraveler = activeTraveler;
    }

    public static Optional<Vehicle> forId(String id) {
        return Optional.ofNullable(idToVehicleMap.get(Optional.ofNullable(id).orElse("")));
    }

    public String getId() {
        return id;
    }
}
