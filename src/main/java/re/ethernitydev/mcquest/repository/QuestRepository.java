package re.ethernitydev.mcquest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import re.ethernitydev.mcquest.model.Quest;
import re.ethernitydev.mcquest.model.User;

import java.util.List;

public interface QuestRepository extends JpaRepository<Quest, Long> {
    List<Quest> findByAuthor(User author);

    @Query("SELECT q FROM Quest q WHERE q.id NOT IN " +
            "(SELECT qc.quest.id FROM QuestCompletion qc WHERE qc.user = :user)")
    List<Quest> findAvailableQuestsByUser(User user);
    List<Quest> findTop5ByOrderByCreatedAtDesc();

}