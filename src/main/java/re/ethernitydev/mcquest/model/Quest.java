package re.ethernitydev.mcquest.model;

import jakarta.persistence.*;

import java.util.List;

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

    @OneToMany(mappedBy = "quest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participations;

}
