package me.aboullaite.flighttracker.service;

import me.aboullaite.flighttracker.model.Aircraft;
import me.aboullaite.flighttracker.model.Flight;
import me.aboullaite.flighttracker.repository.AircraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collection;

@Service
public class FlightService {
    @Value("${opensky.base_url}")
    private String baseURL;
    @Value("${opensky.all_states}")
    private String allStates;

    @Autowired
    AircraftRepository repository;

    @Bean
    WebClient client(){
        return WebClient.create(baseURL);
    }

    public Mono<Flight> getAllFlights(){
        return client().get().uri(allStates).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(cr -> cr.bodyToMono(Flight.class));

    }

    public Mono<Aircraft> getFlightDetail(String icao24){
        return repository.findByIcao(icao24);
    }


}
