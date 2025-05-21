package re.ethernitydev.mcquest.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getChallenger() {
        return challenger;
    }

    public void setChallenger(User challenger) {
        this.challenger = challenger;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public ChallengeStatus getStatus() {
        return status;
    }

    public void setStatus(ChallengeStatus status) {
        this.status = status;
    }

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
