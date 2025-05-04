package com.hoenscanner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hoenscanner.core.SearchResult;
import com.hoenscanner.resources.SearchResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    public static void main(String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<HoenScannerConfiguration> bootstrap) {}

    @Override
    public void run(HoenScannerConfiguration config, Environment env) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<SearchResult> searchResults = new ArrayList<>();

        File rentalFile = new File(getClass().getClassLoader().getResource("rental_cars.json").getFile());
        List<SearchResult> cars = Arrays.asList(mapper.readValue(rentalFile, SearchResult[].class));
        cars.forEach(c -> c.setKind("car"));

        File hotelFile = new File(getClass().getClassLoader().getResource("hotels.json").getFile());
        List<SearchResult> hotels = Arrays.asList(mapper.readValue(hotelFile, SearchResult[].class));
        hotels.forEach(h -> h.setKind("hotel"));

        searchResults.addAll(cars);
        searchResults.addAll(hotels);

        env.jersey().register(new SearchResource(searchResults));
    }
}
