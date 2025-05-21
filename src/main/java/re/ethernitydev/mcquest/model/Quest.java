package re.ethernitydev.mcquest.model;

import jakarta.persistence.*;

@Entity
public class Quest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private int xpReward;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id")
    private User author;

}
