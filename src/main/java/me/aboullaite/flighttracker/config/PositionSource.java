package me.aboullaite.flighttracker.config;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PositionSource {
    ADS_B(0),
    ASTERIX(1),
    MLAT(2);

    private final int number;

    PositionSource(final int number) {
        this.number = number;
    }

    @JsonValue
    int getNumber() {
        return this.number;
    }
}
