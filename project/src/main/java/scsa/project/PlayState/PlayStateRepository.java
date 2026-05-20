package scsa.project.PlayState;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlayStateRepository extends JpaRepository<PlayState, Long> {

    @Query("SELECT ps FROM PlayState ps WHERE ps.user.userId = :userId")
    Optional<PlayState> findByUserId(@Param("userId") Long userId);

    Optional<PlayState> findByUser_UserId(Long userId);

}