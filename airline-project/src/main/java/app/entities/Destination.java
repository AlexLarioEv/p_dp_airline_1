package app.entities;

import app.enums.Airport;
import lombok.AllArgsConstructor;
import lombok.Data;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "destination")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Destination {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_destination")
    @SequenceGenerator(name = "seq_destination", initialValue = 1000, allocationSize = 1)
    private Long id;

    @Column(name = "airport_code")
    @Enumerated(EnumType.STRING)
    private Airport airportCode;

    @Column(name = "airport_name")
    private String airportName;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "country_name")
    private String countryName;
}
