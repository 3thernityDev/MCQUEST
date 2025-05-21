package re.ethernitydev.mcquest.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    private int requiredLevel;

    @OneToMany(mappedBy = "title")
    private List<User> users;

}
