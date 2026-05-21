package scsa.project.PlayLog;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import scsa.project.PlayState.PlayState;

import java.util.List;

@Repository
public interface PlayLogRepository extends JpaRepository<PlayLog, Long> {

    // 1. 특정 게임 진행 상태(PlayState)에 연결된 모든 선택 로그 조회 (이력 확인용)
    List<PlayLog> findByPlayState(PlayState playState);

    // 2. 특정 게임 상태의 로그를 최신순(내림차순)으로 조회하고 싶을 때
    List<PlayLog> findByPlayStateOrderByCreatedAtDesc(PlayState playState);

    // 3. 특정 유저의 특정 시나리오 선택 기록이 있는지 확인할 때
    boolean existsByPlayStateAndScenario_ScenarioId(PlayState playState, Long scenarioId);

    // 4. PlayLog와 Scenario를 JOIN FETCH로 한 번에 조회하여 N+1 문제 회피
    @Query("SELECT pl FROM PlayLog pl JOIN FETCH pl.scenario s WHERE pl.playState.stateId = :stateId ORDER BY s.stepOrder ASC")
    List<PlayLog> findByPlayStateIdWithScenario(@Param("stateId") Long stateId);
}