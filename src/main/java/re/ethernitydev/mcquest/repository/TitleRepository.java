package re.ethernitydev.mcquest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.ethernitydev.mcquest.model.Title;

import java.util.List;
import java.util.Optional;

public interface TitleRepository extends JpaRepository<Title, Long> {
    Optional<Title> findByName(String name);
    List<Title> findByRequiredLevelLessThanEqual(int level);
}