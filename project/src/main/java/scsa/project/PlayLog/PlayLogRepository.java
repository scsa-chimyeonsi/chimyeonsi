package scsa.project.PlayLog;


import org.springframework.data.jpa.repository.JpaRepository;
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
}