package re.ethernitydev.mcquest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import re.ethernitydev.mcquest.model.Quest;
import re.ethernitydev.mcquest.model.QuestCompletion;
import re.ethernitydev.mcquest.model.User;

import java.util.List;
import java.util.Optional;

public interface QuestCompletionRepository extends JpaRepository<QuestCompletion, Long> {
    List<QuestCompletion> findByUser(User user);
    List<QuestCompletion> findByQuest(Quest quest);
    Optional<QuestCompletion> findByUserAndQuest(User user, Quest quest);

    @Query("SELECT COUNT(qc) FROM QuestCompletion qc WHERE qc.user = :user")
    long countByUser(User user);
}