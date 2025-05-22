package re.ethernitydev.mcquest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.ethernitydev.mcquest.model.Challenge;
import re.ethernitydev.mcquest.model.ChallengeStatus;
import re.ethernitydev.mcquest.model.Quest;
import re.ethernitydev.mcquest.model.User;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByChallenger(User challenger);
    List<Challenge> findByTarget(User target);
    List<Challenge> findByStatus(ChallengeStatus status);
    List<Challenge> findByTargetAndStatus(User target, ChallengeStatus status);
    List<Challenge> findByChallengerAndStatus(User challenger, ChallengeStatus status);

    Optional<Challenge> findByTargetAndQuestAndStatus(User user, Quest quest, ChallengeStatus challengeStatus);
}