package app.services.interfaces;

import app.entities.Aircraft;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AircraftService {

    Aircraft save(Aircraft aircraft);

    Page<Aircraft> findAll(Pageable pageable);

    Aircraft findById(Long id);

    Aircraft findByAircraftNumber(String aircraftNumber);

    void delete(Long id);
}
