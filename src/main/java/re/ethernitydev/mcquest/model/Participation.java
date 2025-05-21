package re.ethernitydev.mcquest.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "quest_id"})
)
public class Participation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "quest_id")
    private Quest quest;

    private LocalDateTime completionDate = LocalDateTime.now();
}

