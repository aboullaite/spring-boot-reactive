package me.aboullaite.flighttracker.controller;

import me.aboullaite.flighttracker.model.Aircraft;
import me.aboullaite.flighttracker.model.Flight;
import me.aboullaite.flighttracker.model.StateVector;
import me.aboullaite.flighttracker.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class FlightController {
    @Autowired
    FlightService service;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "/flights")
    @ResponseBody
    Flux<Flight> flights(){
        return Flux.from(service.getAllFlights());

    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "/aircraft/{icao}")
    @ResponseBody
    Mono<Aircraft> aircraft(@PathVariable String icao){
        return service.getFlightDetail(icao);

    }

    @GetMapping("/")
    Mono<String> home() {
        return Mono.just("flights");
    }
}
