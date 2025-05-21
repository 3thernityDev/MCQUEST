package re.ethernitydev.mcquest.model;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique=true, nullable=false)
    private String username;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false, unique=true)
    private String email;

    private int xp = 0 ;
    private int level = 1;
}
