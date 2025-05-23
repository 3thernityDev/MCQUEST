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

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired private UserService userService;
    @Autowired private QuestService questService;
    @Autowired private ChallengeService challengeService;

    @GetMapping({"/", "/home"})
    public String showHome(Model model, Authentication authentication) {
        User currentUser = null;
        if (authentication != null && authentication.isAuthenticated()) {
            currentUser = userService
                    .getUserByUsername(authentication.getName())
                    .orElse(null);
        }
        model.addAttribute("currentUser", currentUser);

        String greeting;
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.NOON)) {
            greeting = "Bonjour";
        } else if (now.isBefore(LocalTime.of(18, 0))) {
            greeting = "Bon après-midi";
        } else {
            greeting = "Bonsoir";
        }
        model.addAttribute("greeting", greeting);

        model.addAttribute("recentQuests", questService.getRecentQuests());
        model.addAttribute("topUsers", userService.getTopUsers(5));

        if (currentUser != null) {
            Map<String, Object> userStats = new HashMap<>();
            userStats.put("level", currentUser.getLevel());
            userStats.put("xp", currentUser.getXp());
            userStats.put("completedQuests", questService.getUserCompletions(currentUser).size());
            userStats.put("activeChallenges", challengeService.getActiveChallengesByTarget(currentUser).size());
            model.addAttribute("userStats", userStats);
            model.addAttribute("activeChallengesList", challengeService.getActiveChallengesByTarget(currentUser));
        }

        return "home";
    }
}
