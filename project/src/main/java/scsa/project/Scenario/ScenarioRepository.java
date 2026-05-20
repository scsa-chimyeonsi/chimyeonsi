package scsa.project.Scenario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

    @Query("SELECT s FROM Scenario s WHERE s.stepOrder = :stepOrder")
    Optional<Scenario> findByStepOrder(@Param("stepOrder") Integer stepOrder);
}
