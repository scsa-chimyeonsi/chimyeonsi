package scsa.project.PlayState;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayStateRepository extends JpaRepository<PlayState, Long> {
    Optional<PlayState> findByUser_UserId(Long userId);
}
