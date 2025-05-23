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
import java.util.List;
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
        User currentUser = userService.getUserByUsername(authentication.getName()).orElse(null);
        if (currentUser != null) {
            // tes stats
            Map<String, Object> userStats = new HashMap<>();
            userStats.put("xp", currentUser.getXp());
            userStats.put("level", currentUser.getLevel());
            userStats.put("completedQuests", questService.getUserCompletions(currentUser).size());
            userStats.put("activeChallenges", challengeService.getActiveChallengesByTarget(currentUser).size());
            model.addAttribute("userStats", userStats);

            model.addAttribute("recentQuests", questService.getRecentQuests());

            model.addAttribute("activeChallenges", challengeService.getActiveChallengesByTarget(currentUser));

            model.addAttribute("topUsers", userService.getTopUsers(5));
        }
        return "home";
    }
}
