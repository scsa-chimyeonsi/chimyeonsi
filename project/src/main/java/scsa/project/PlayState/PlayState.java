package scsa.project.PlayState;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import scsa.project.PlayLog.PlayLog;
import scsa.project.User.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "play_states")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PlayState {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "play_states_seq_generator")
    @SequenceGenerator(
            name = "play_states_seq_generator",
            sequenceName = "play_states_seq",
            allocationSize = 1
    )
    @Column(name = "state_id")
    private Long stateId;


    @Builder.Default
    @Column(nullable = false)
    private Integer currentStep = 1;

    @Builder.Default
    @Column(nullable = false)
    private Integer totalScore = 0;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "playState", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PlayLog> playLogs = new ArrayList<>();

}
