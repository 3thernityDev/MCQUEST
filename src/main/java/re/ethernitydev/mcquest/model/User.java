package re.ethernitydev.mcquest.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(nullable=false, unique=true)
    private String email;

    private int xp = 0;
    private int level = 1;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quest> createdQuests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participation> participations;

    @OneToMany(mappedBy = "challenger")
    private List<Challenge> challengesSent;

    @OneToMany(mappedBy = "target")
    private List<Challenge> challengesReceived;

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public List<Challenge> getChallengesReceived() {
        return challengesReceived;
    }

    public void setChallengesReceived(List<Challenge> challengesReceived) {
        this.challengesReceived = challengesReceived;
    }

    public List<Challenge> getChallengesSent() {
        return challengesSent;
    }

    public void setChallengesSent(List<Challenge> challengesSent) {
        this.challengesSent = challengesSent;
    }

    public List<Participation> getParticipations() {
        return participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }

    public List<Quest> getCreatedQuests() {
        return createdQuests;
    }

    public void setCreatedQuests(List<Quest> createdQuests) {
        this.createdQuests = createdQuests;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "title_id")
    private Title title;


}
