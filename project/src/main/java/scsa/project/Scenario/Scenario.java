package scsa.project.Scenario;

import jakarta.persistence.*;
import lombok.*;
import scsa.project.PlayLog.PlayLog;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "scenarios")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Scenario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scenarios_seq_generator")
    @SequenceGenerator(
            name = "scenarios_seq_generator",
            sequenceName = "scenarios_seq",
            allocationSize = 1
    )
    private Long scenarioId;

    @Column(nullable = false)
    private Integer stepOrder;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name = "opt_A_text", nullable = false)
    private String optAText;

    @Column(name = "opt_A_score", nullable = false)
    private Integer optAScore;

    @Column(name = "next_A_id")
    private Long nextAId;

    @Column(name = "opt_B_text", nullable = false)
    private String optBText;

    @Column(name = "opt_B_score", nullable = false)
    private Integer optBScore;

    @Column(name = "next_B_id")
    private Long nextBId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScenarioType type;

    @OneToMany(mappedBy = "scenario", fetch = FetchType.LAZY)
    @Builder.Default
    private List<PlayLog> playLogs = new ArrayList<>();
}
