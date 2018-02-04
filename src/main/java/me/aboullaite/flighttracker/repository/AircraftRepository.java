package me.aboullaite.flighttracker.repository;

import me.aboullaite.flighttracker.model.Aircraft;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface AircraftRepository extends ReactiveCrudRepository<Aircraft, Long> {

    Mono<Aircraft> findByIcao(String icao);
}
