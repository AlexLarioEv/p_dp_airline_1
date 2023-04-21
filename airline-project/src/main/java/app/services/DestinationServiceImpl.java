package app.services;

import app.entities.Destination;
import app.enums.Airport;
import app.repositories.DestinationRepository;
import app.services.interfaces.DestinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {
    private final DestinationRepository destinationRepository;

    @Override
    public Page<Destination> findAll(Pageable pageable) {
        return destinationRepository.findAll(pageable);
    }

    @Override
    public List<Destination> findDestinationByName(String cityName, String countryName) {
        if (cityName != null) {
            return destinationRepository.findByCityNameContainingIgnoreCase(cityName);
        } else {
            return destinationRepository.findByCountryNameContainingIgnoreCase(countryName);
        }
    }

    @Override
    @Transactional
    public void saveDestination(Destination destination) {
        destinationRepository.save(destination);
    }

    @Override
    @Transactional
    public void updateDestination(Long id, Destination destination) {
        destination.setId(id);
        destinationRepository.save(destination);
    }

    @Override
    @Transactional
    public void deleteDestinationById(Long id) {
        destinationRepository.deleteById(id);
    }

    @Override
    public Destination getDestinationById(Long id) {
        return destinationRepository.findById(id).orElse(null);
    }

    @Override
    public Destination findDestinationByAirportCode(Airport airportCode) {
        return destinationRepository.findDestinationByAirportCode(airportCode).orElse(null);
    }
}
