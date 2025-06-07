package com.example.demo.info.domain;

import org.springframework.stereotype.Component;

@Component
public class MapValidator {

    public String[] splitCoordinates(String coordinates) {
        return coordinates.split(",");
    }

    public double parseDouble(String coords) {
        return Double.parseDouble(coords);
    }

}
