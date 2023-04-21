package app.repositories;

import app.entities.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Deprecated
public interface RouteRepository extends JpaRepository<Route, Long> {
}
