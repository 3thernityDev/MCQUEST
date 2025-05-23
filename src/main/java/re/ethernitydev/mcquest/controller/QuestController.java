package re.ethernitydev.mcquest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re.ethernitydev.mcquest.model.Quest;
import re.ethernitydev.mcquest.model.QuestCompletion;
import re.ethernitydev.mcquest.model.User;
import re.ethernitydev.mcquest.service.QuestService;
import re.ethernitydev.mcquest.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/quests")
public class QuestController {

    @Autowired
    private QuestService questService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getAllQuests(Model model, Authentication authentication) {
        User currentUser = userService
                .getUserByUsername(authentication.getName())
                .orElse(null);

        // Toujours la liste complète
        model.addAttribute("quests", questService.getAllQuests());

        // On récupère et mappe les quêtes complétées en entités Quest
        if (currentUser != null) {
            List<QuestCompletion> completions = questService.getUserCompletions(currentUser);
            List<Quest> completedQuests = completions.stream()
                    .map(QuestCompletion::getQuest)
                    .collect(Collectors.toList());
            model.addAttribute("completedQuests", completedQuests);
        } else {
            model.addAttribute("completedQuests", List.of());
        }

        model.addAttribute("currentUser", currentUser);
        return "quests/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("quest", new Quest());
        return "quests/create";
    }

    @PostMapping("/create")
    public String createQuest(@ModelAttribute Quest quest, Authentication authentication) {
        User author = userService.getUserByUsername(authentication.getName()).orElse(null);
        if (author != null) {
            questService.createQuest(quest, author);
        }
        return "redirect:/quests";
    }

    @GetMapping("/{id}")
    public String getQuestDetails(@PathVariable Long id,
                                  Model model,
                                  Authentication authentication) {
        Quest quest = questService.getQuestById(id).orElse(null);
        User currentUser = userService.getUserByUsername(authentication.getName()).orElse(null);

        if (quest != null && currentUser != null) {
            boolean completed = questService.hasUserCompletedQuest(currentUser, quest);
            model.addAttribute("quest", quest);
            model.addAttribute("completedByUser", completed);
            model.addAttribute("currentUser", currentUser);
        }
        return "quests/details";
    }

    @PostMapping("/{id}/complete")
    public String completeQuest(@PathVariable Long id, Authentication authentication) {
        Quest quest = questService.getQuestById(id).orElse(null);
        User user = userService.getUserByUsername(authentication.getName()).orElse(null);

        if (quest != null && user != null) {
            try {
                questService.completeQuest(user, quest);
            } catch (RuntimeException e) {
                // ignore
            }
        }
        return "redirect:/quests/" + id;
    }
}
