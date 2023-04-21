package app.services.interfaces;

import app.entities.Destination;
import app.enums.Airport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface DestinationService {

    Page<Destination> findAll(Pageable pageable);

    List<Destination> findDestinationByName(String cityName, String countryName);

    void saveDestination(Destination destination);

    void updateDestination(Long id, Destination destination);

    void deleteDestinationById(Long id);

    Destination getDestinationById(Long id);

    Destination findDestinationByAirportCode(Airport airportCode);
}
