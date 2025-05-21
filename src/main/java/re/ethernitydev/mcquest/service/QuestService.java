package re.ethernitydev.mcquest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.ethernitydev.mcquest.model.Quest;
import re.ethernitydev.mcquest.model.QuestCompletion;
import re.ethernitydev.mcquest.model.User;
import re.ethernitydev.mcquest.repository.QuestCompletionRepository;
import re.ethernitydev.mcquest.repository.QuestRepository;

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

    public QuestCompletion completeQuest(User user, Quest quest) {
        Optional<QuestCompletion> existingCompletion = questCompletionRepository.findByUserAndQuest(user, quest);
        if (existingCompletion.isPresent()) {
            throw new RuntimeException("Quête déjà complétée par cet utilisateur");
        }

        QuestCompletion completion = new QuestCompletion();
        completion.setUser(user);
        completion.setQuest(quest);

        userService.addXP(user, quest.getXpReward());

        return questCompletionRepository.save(completion);
    }

    public List<QuestCompletion> getUserCompletions(User user) {
        return questCompletionRepository.findByUser(user);
    }

    public boolean hasUserCompletedQuest(User user, Quest quest) {
        return questCompletionRepository.findByUserAndQuest(user, quest).isPresent();
    }
}