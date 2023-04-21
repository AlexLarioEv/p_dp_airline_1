package app.entities.account;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "airline_manager")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AirlineManager extends Account {
}
