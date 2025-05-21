package re.ethernitydev.mcquest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.ethernitydev.mcquest.model.Challenge;
import re.ethernitydev.mcquest.model.ChallengeStatus;
import re.ethernitydev.mcquest.model.Quest;
import re.ethernitydev.mcquest.model.User;
import re.ethernitydev.mcquest.repository.ChallengeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChallengeService {

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestService questService;

    /**
     * Crée un défi avec une date d'expiration précise.
     * @param challenger l'utilisateur qui lance le défi
     * @param target l'utilisateur défié
     * @param quest la quête associée au défi
     * @param expirationDate date et heure d'expiration du défi
     * @return le défi sauvegardé
     */
    public Challenge createChallenge(User challenger, User target, Quest quest, LocalDateTime expirationDate) {
        Challenge challenge = new Challenge();
        challenge.setChallenger(challenger);
        challenge.setTarget(target);
        challenge.setQuest(quest);
        challenge.setStartDate(LocalDateTime.now());
        challenge.setExpirationDate(expirationDate);
        challenge.setStatus(ChallengeStatus.IN_PROGRESS);

        return challengeRepository.save(challenge);
    }

    public List<Challenge> getChallengesByTarget(User target) {
        return challengeRepository.findByTarget(target);
    }

    public List<Challenge> getChallengesByChallenger(User challenger) {
        return challengeRepository.findByChallenger(challenger);
    }

    public List<Challenge> getActiveChallengesByTarget(User target) {
        return challengeRepository.findByTargetAndStatus(target, ChallengeStatus.IN_PROGRESS);
    }

    public Optional<Challenge> getChallengeById(Long id) {
        return challengeRepository.findById(id);
    }

    public Challenge completeChallenge(Challenge challenge, boolean success) {
        if (challenge.getStatus() != ChallengeStatus.IN_PROGRESS) {
            throw new RuntimeException("Ce défi n'est plus en cours");
        }

        int xpAmount = challenge.getQuest().getXpReward();

        if (success) {
            challenge.setStatus(ChallengeStatus.SUCCESS);

            userService.addXP(challenge.getTarget(), xpAmount);
            userService.removeXP(challenge.getChallenger(), xpAmount / 2);
        } else {
            challenge.setStatus(ChallengeStatus.FAILED);

            userService.addXP(challenge.getChallenger(), xpAmount);
            userService.removeXP(challenge.getTarget(), xpAmount / 2);
        }

        return challengeRepository.save(challenge);
    }

    public void checkExpiredChallenges() {
        List<Challenge> activeChallenges = challengeRepository.findByStatus(ChallengeStatus.IN_PROGRESS);
        LocalDateTime now = LocalDateTime.now();

        for (Challenge challenge : activeChallenges) {
            if (challenge.getExpirationDate().isBefore(now)) {
                completeChallenge(challenge, false);
            }
        }
    }
}