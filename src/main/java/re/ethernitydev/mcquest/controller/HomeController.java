package re.ethernitydev.mcquest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import re.ethernitydev.mcquest.model.User;
import re.ethernitydev.mcquest.service.ChallengeService;
import re.ethernitydev.mcquest.service.QuestService;
import re.ethernitydev.mcquest.service.UserService;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestService questService;

    @Autowired
    private ChallengeService challengeService;

    @GetMapping({"/", "/home"})
    public String showHome(Model model, Authentication authentication) {
        // Récupération de l'utilisateur connecté
        User currentUser = userService
                .getUserByUsername(authentication.getName())
                .orElse(null);

        if (currentUser != null) {
            int xp = currentUser.getXp();
            int level = currentUser.getLevel();
            long completedQuests = questService.getUserCompletions(currentUser).size();
            long activeChallenges = challengeService.getActiveChallengesByTarget(currentUser).size();

            // Assemblage dans un DTO (Map)
            Map<String, Object> userStats = new HashMap<>();
            userStats.put("xp", xp);
            userStats.put("level", level);
            userStats.put("completedQuests", completedQuests);
            userStats.put("activeChallenges", activeChallenges);

            model.addAttribute("userStats", userStats);
            model.addAttribute("recentQuests", questService.getRecentQuests());
            model.addAttribute("activeChallengesList", challengeService.getActiveChallengesByTarget(currentUser));
        }
        return "home";
    }
}
