package re.ethernitydev.mcquest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.ethernitydev.mcquest.model.Title;
import re.ethernitydev.mcquest.repository.TitleRepository;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class TitleService {

    @Autowired
    private TitleRepository titleRepository;

    public List<Title> getAllTitles() {
        return titleRepository.findAll();
    }

    public Optional<Title> getTitleById(Long id) {
        return titleRepository.findById(id);
    }

    public Title createTitle(Title title) {
        return titleRepository.save(title);
    }

    public List<Title> getAvailableTitles(int userLevel) {
        return titleRepository.findByRequiredLevelLessThanEqual(userLevel);
    }

    @PostConstruct
    public void initializeDefaultTitles() {
        if (titleRepository.count() == 0) {
            createDefaultTitles();
        }
    }

    private void createDefaultTitles() {
        Title novice = new Title();
        novice.setName("Novice");
        novice.setDescription("Premier pas dans l'aventure");
        novice.setRequiredLevel(1);
        titleRepository.save(novice);

        Title aventurier = new Title();
        aventurier.setName("Aventurier");
        aventurier.setDescription("Explorateur confirmé");
        aventurier.setRequiredLevel(5);
        titleRepository.save(aventurier);

        Title heros = new Title();
        heros.setName("Héros");
        heros.setDescription("Légende vivante");
        heros.setRequiredLevel(10);
        titleRepository.save(heros);

        Title maitre = new Title();
        maitre.setName("Maître des Quêtes");
        maitre.setDescription("Maître absolu de toutes les quêtes");
        maitre.setRequiredLevel(20);
        titleRepository.save(maitre);
    }
}