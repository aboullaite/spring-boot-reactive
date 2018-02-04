package me.aboullaite.flighttracker.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import me.aboullaite.flighttracker.config.PositionSource;
import java.util.Set;

@Data
@JsonFormat(shape=JsonFormat.Shape.ARRAY)
public class StateVector {
    private String icao24;
    private String callsign;
    private String originCountry;
    private Double lastPositionUpdate;
    private Double lastContact;
    private Double longitude;
    private Double latitude;
    private Double geoAltitude;
    private boolean onGround;
    private Double velocity;
    private Double heading;
    private Double verticalRate;
    private Set<Integer> serials;
    private Double baroAltitude;
    private String squawk;
    private boolean spi;
    private PositionSource positionSource;


}
