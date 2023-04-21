package app.entities;

import app.entities.account.Passenger;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tickets")
@EqualsAndHashCode(of = {"ticketNumber"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tickets")
    @SequenceGenerator(name = "seq_tickets", initialValue = 1000, allocationSize = 1)
    private Long id;

    @Column(name = "ticket_number")
    private String ticketNumber;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    @JsonView
    private Passenger passenger;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "flight_id")
    @JsonView
    private Flight flight;

    @OneToOne
    @JoinColumn(name = "flight_seat_id")
    @JsonView
    private FlightSeat flightSeat;
}
