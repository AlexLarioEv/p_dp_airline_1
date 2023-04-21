package app.services.interfaces;

import app.dto.SeatDTO;
import app.entities.Seat;
import app.exceptions.ViolationOfForeignKeyConstraintException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SeatService {
    Seat save (Seat seat);
    Seat findById (long id);
    Seat editById(Long id, Seat Seat);
    void delete(Long id) throws ViolationOfForeignKeyConstraintException;
    Page<Seat> findByAircraftId(Long id, Pageable pageable);
    List<SeatDTO> saveManySeats(long aircraftId);
    Page<Seat> findAll(Pageable pageable);
}
