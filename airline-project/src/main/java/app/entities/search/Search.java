package app.entities.search;

import app.entities.Destination;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Table(name = "searches")
@Setter
@Getter
@NoArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Search {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @ManyToOne
    private Destination from;

    @ManyToOne
    private Destination to;

    @NotNull
    @Column(name = "departure_date", columnDefinition = "DATE", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate departureDate;
    @Column(name = "return_date", columnDefinition = "DATE")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate returnDate;

    @Positive
    @Column(name = "number_of_passengers", nullable = false)
    private Integer numberOfPassengers;

    public Search(Destination from, Destination to, LocalDate departureDate, LocalDate returnDate, Integer numberOfPassengers) {
        this.from = from;
        this.to = to;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.numberOfPassengers = numberOfPassengers;
    }
}
