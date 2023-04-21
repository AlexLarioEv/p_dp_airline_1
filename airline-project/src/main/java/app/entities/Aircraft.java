package app.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "aircrafts")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"aircraftNumber", "model", "modelYear", "flightRange"})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_aircraft")
    @SequenceGenerator(name = "seq_aircraft", initialValue = 1000, allocationSize = 1)
    private Long id;

    @Column(name = "aircraft_number")
    private String aircraftNumber;

    private String model;

    @Column(name = "model_year")
    private int modelYear;

    @Column(name = "flight_range")
    private int flightRange;

    @OneToMany(mappedBy = "aircraft", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Seat> seatSet = new HashSet<>();

    @PreRemove
    public void removeAircraft() {
        seatSet.forEach(seat -> seat.setAircraft(null));
    }
}