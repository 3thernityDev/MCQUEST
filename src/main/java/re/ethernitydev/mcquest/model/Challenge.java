package re.ethernitydev.mcquest.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "challenger_id")
    private User challenger;

    @ManyToOne(optional = false)
    @JoinColumn(name = "target_id")
    private User target;

    @ManyToOne(optional = false)
    @JoinColumn(name = "quest_id")
    private Quest quest;

    private LocalDateTime startDate = LocalDateTime.now();
    private LocalDateTime expirationDate;

    @Enumerated(EnumType.STRING)
    private ChallengeStatus status = ChallengeStatus.IN_PROGRESS;

}
