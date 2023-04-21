package app.entities;

import app.enums.FlightStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "flights",
        indexes = {
                @Index(columnList = "departure_date", name = "departure_index")
        })
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"code"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_flights")
    @SequenceGenerator(name = "seq_flights", initialValue = 1000, allocationSize = 1)
    private Long id;

    @Column(name = "code")
    private String code;

    @ManyToOne
    private Destination from;

    @ManyToOne
    private Destination to;

    @Column(name = "departure_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime departureDateTime;

    @Column(name = "arrival_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime arrivalDateTime;

    @ManyToOne
    @JoinColumn(name = "aircraft_id")
    private Aircraft aircraft;

    @Enumerated(EnumType.STRING)
    @Column(name = "flight_status")
    private FlightStatus flightStatus;
}
