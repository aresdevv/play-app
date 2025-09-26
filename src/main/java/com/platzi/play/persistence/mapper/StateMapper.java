package com.platzi.play.persistence.mapper;

import org.mapstruct.Named;

public class StateMapper {

    @Named("stateToBoolean")
    public static boolean stateToBoolean(String estado) {
        if (estado == null) return false;
        return "D".equalsIgnoreCase(estado);
    }
}


