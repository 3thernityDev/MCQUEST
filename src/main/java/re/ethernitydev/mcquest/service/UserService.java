package re.ethernitydev.mcquest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import re.ethernitydev.mcquest.model.Title;
import re.ethernitydev.mcquest.model.User;
import re.ethernitydev.mcquest.repository.QuestCompletionRepository;
import re.ethernitydev.mcquest.repository.TitleRepository;
import re.ethernitydev.mcquest.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TitleRepository titleRepository;

    @Autowired
    private QuestCompletionRepository questCompletionRepository;

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void addXP(User user, int xp) {
        user.setXp(user.getXp() + xp);
        updateLevel(user);
        userRepository.save(user);
    }

    public void removeXP(User user, int xp) {
        int newXp = Math.max(0, user.getXp() - xp);
        user.setXp(newXp);
        updateLevel(user);
        userRepository.save(user);
    }

    private void updateLevel(User user) {
        int newLevel = calculateLevelFromXP(user.getXp());
        if (newLevel != user.getLevel()) {
            user.setLevel(newLevel);
            updateTitle(user);
        }
    }

    private int calculateLevelFromXP(int xp) {
        // Formule simple : niveau = sqrt(xp / 100) + 1
        return (int) Math.floor(Math.sqrt(xp / 100.0)) + 1;
    }

    private void updateTitle(User user) {
        List<Title> availableTitles = titleRepository.findByRequiredLevelLessThanEqual(user.getLevel());
        if (!availableTitles.isEmpty()) {
            Title bestTitle = availableTitles.stream()
                    .max((t1, t2) -> Integer.compare(t1.getRequiredLevel(), t2.getRequiredLevel()))
                    .orElse(null);
            user.setTitle(bestTitle);
        }
    }

    public List<User> getTopUsers(int limit) {
        return userRepository.findAll(
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "xp"))
        ).getContent();
    }

    public long getUserQuestCount(User user) {
        return questCompletionRepository.countByUser(user);
    }
}