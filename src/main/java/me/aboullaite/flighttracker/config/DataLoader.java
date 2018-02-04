package me.aboullaite.flighttracker.config;

import me.aboullaite.flighttracker.model.Aircraft;
import me.aboullaite.flighttracker.repository.AircraftRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private static final Logger LOGGER= LoggerFactory.getLogger(DataLoader.class);
    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    AircraftRepository repository;
    List<Aircraft> aircrafts = new ArrayList();
    @Override
    public void run(String... args) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:aircraftDatabase.csv");
        LOGGER.info("Start loading data ...");
        InputStream is = resource.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        CSVParser parser = new CSVParser(br, CSVFormat.EXCEL.withHeader().withTrim());

        Iterable<CSVRecord> records = parser.getRecords();
        records.forEach(r ->
        aircrafts.add(
                new Aircraft(r.get("icao24"),
                r.get("registration"),
                r.get("manufacturericao"),
                r.get("manufacturername"),
                r.get("model"),
                r.get("owner"),
                r.get("operator"),
                r.get("reguntil"),
                r.get("engines"),
                r.get("built")
                ))
                );
        repository.saveAll(aircrafts).subscribe();
        LOGGER.info("Done loading data ...!");
    }
}
