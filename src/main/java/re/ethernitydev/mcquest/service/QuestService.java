package re.ethernitydev.mcquest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.ethernitydev.mcquest.model.Challenge;
import re.ethernitydev.mcquest.model.ChallengeStatus;
import re.ethernitydev.mcquest.model.Quest;
import re.ethernitydev.mcquest.model.QuestCompletion;
import re.ethernitydev.mcquest.model.User;
import re.ethernitydev.mcquest.repository.ChallengeRepository;
import re.ethernitydev.mcquest.repository.QuestCompletionRepository;
import re.ethernitydev.mcquest.repository.QuestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuestService {

    @Autowired
    private QuestRepository questRepository;

    @Autowired
    private QuestCompletionRepository questCompletionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private ChallengeRepository challengeRepository;

    public Quest createQuest(Quest quest, User author) {
        quest.setAuthor(author);
        return questRepository.save(quest);
    }

    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    public List<Quest> getQuestsByAuthor(User author) {
        return questRepository.findByAuthor(author);
    }

    public List<Quest> getAvailableQuests(User user) {
        return questRepository.findAvailableQuestsByUser(user);
    }

    public Optional<Quest> getQuestById(Long id) {
        return questRepository.findById(id);
    }

    @Transactional
    public QuestCompletion completeQuest(User user, Quest quest) {
        Optional<QuestCompletion> existing = questCompletionRepository.findByUserAndQuest(user, quest);
        if (existing.isPresent()) {
            throw new RuntimeException("Quête déjà complétée par cet utilisateur");
        }

        QuestCompletion completion = new QuestCompletion();
        completion.setUser(user);
        completion.setQuest(quest);
        userService.addXP(user, quest.getXpReward());
        questCompletionRepository.save(completion);

        Optional<Challenge> optChal = challengeRepository
                .findByTargetAndQuestAndStatus(user, quest, ChallengeStatus.IN_PROGRESS);

        if (optChal.isPresent()) {
            Challenge chal = optChal.get();
            boolean inTime = chal.getExpirationDate().isAfter(LocalDateTime.now());
            challengeService.completeChallenge(chal, inTime);
        }

        return completion;
    }

    public List<QuestCompletion> getUserCompletions(User user) {
        return questCompletionRepository.findByUser(user);
    }

    public boolean hasUserCompletedQuest(User user, Quest quest) {
        return questCompletionRepository.findByUserAndQuest(user, quest).isPresent();
    }

    public List<Quest> getRecentQuests() {
        return questRepository.findTop5ByOrderByCreatedAtDesc();
    }
}
