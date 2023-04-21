package app.repositories;

import app.entities.Flight;
import java.util.List;

public interface SearchResultProjection {
    Long getId();
    List<Flight> getDepartFlight();
    List<Flight> getReturnFlight();

}
