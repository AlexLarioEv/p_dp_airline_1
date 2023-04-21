package app.services;

import app.entities.Aircraft;
import app.entities.Flight;
import app.entities.FlightSeat;
import app.entities.Seat;
import app.repositories.FlightRepository;
import app.repositories.FlightSeatRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class FlightSeatServiceImplTest {

    @Mock
    FlightSeatRepository flightSeatRepository;

    @Mock
    FlightRepository flightRepository;

    @InjectMocks
    FlightSeatServiceImpl flightSeatService;

    @Test
    void addFlightSeatsByFlightNumber() {
        String flightNumber = "Code:Fl-1";

        Aircraft aircraft = new Aircraft();
        aircraft.setId(Long.valueOf(1));
        aircraft.setAircraftNumber("Number:A-1");
        aircraft.setModel("ModelAir");
        aircraft.setFlightRange(500);
        aircraft.setModelYear(2008);

        //Список мест самолёта с номерами от 8 до 17
        Set<Seat> seatSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            Seat seat = new Seat();
            seat.setSeatNumber(Integer.valueOf(i + 8).toString());
            seat.setAircraft(aircraft);
            seatSet.add(seat);
        }

        aircraft.setSeatSet(seatSet);

        Flight flight = new Flight();
        flight.setCode(flightNumber);
        flight.setAircraft(aircraft);

        //Список уже  существующих мест для продажи с номерами от 1 до 10
        //№8 - такой же номер как в самолёте, но не относится к нему
        //№9 и №10 - уже добавленные места нашего самолёта для этого Flight-а
        Set<FlightSeat> flightSeatSet = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            FlightSeat flightSeat = new FlightSeat();
            Seat seat = new Seat();
            seat.setSeatNumber(Integer.valueOf(i + 1).toString());

            if ((i + 1) >= 9) {
                seat.setAircraft(aircraft);
                flightSeat.setFlight(flight);
            }
            flightSeat.setSeat(seat);
            flightSeatSet.add(flightSeat);
        }

        Mockito.doReturn(flight)
                .when(flightRepository)
                .getByCode(flightNumber);

        Mockito.doReturn(flightSeatSet)
                .when(flightSeatRepository)
                .findAll();

        Set<FlightSeat> result = flightSeatService.addFlightSeatsByFlightNumber(flightNumber);

        Mockito.verify(flightSeatRepository, Mockito.times(8)).save(Mockito.any(FlightSeat.class));

        Assert.assertEquals(8, result.size());

        result.forEach(flightSeat -> Assert.assertEquals(flight, flightSeat.getFlight()));

        Set<Seat> seatsInResult = new HashSet<>();
        result.forEach(flightSeat -> seatsInResult.add(flightSeat.getSeat()));
        seatSet.removeIf(seat -> seat.getSeatNumber().equals(Integer.valueOf(9).toString())
                || seat.getSeatNumber().equals(Integer.valueOf(10).toString()));
        seatSet.addAll(seatsInResult);
        Assert.assertEquals(8, seatSet.size());

    }
}