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

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestService questService;

    @Autowired
    private ChallengeService challengeService;

    @GetMapping("/home")
    public String showHome(Model model, Authentication authentication) {
        User currentUser = userService.getUserByUsername(authentication.getName()).orElse(null);
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
            model.addAttribute("availableQuests", questService.getAvailableQuests(currentUser));
            model.addAttribute("activeChallenges", challengeService.getActiveChallengesByTarget(currentUser));
            model.addAttribute("questCount", userService.getUserQuestCount(currentUser));
        }
        return "home";
    }

    @GetMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }
}