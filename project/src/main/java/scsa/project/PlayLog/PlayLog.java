package scsa.project.PlayLog;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import scsa.project.PlayState.PlayState;
import scsa.project.Scenario.Scenario;

import java.time.LocalDateTime;

@Entity
@Table(name = "play_logs")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PlayLog {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "play_logs_seq_generator")
    @SequenceGenerator(
            name = "play_logs_seq_generator",
            sequenceName = "play_logs_seq",
            allocationSize = 1
    )
    private Long logId;

    @Enumerated(EnumType.STRING)
    @Column(length = 1, nullable = false)
    private SelectedOption selectedOpt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id", nullable = false)
    private PlayState playState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scenario_id", nullable = false)
    private Scenario scenario;


}
