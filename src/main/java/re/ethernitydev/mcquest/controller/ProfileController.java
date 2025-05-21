package re.ethernitydev.mcquest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re.ethernitydev.mcquest.model.User;
import re.ethernitydev.mcquest.service.ChallengeService;
import re.ethernitydev.mcquest.service.QuestService;
import re.ethernitydev.mcquest.service.TitleService;
import re.ethernitydev.mcquest.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuestService questService;

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private TitleService titleService;

    @GetMapping
    public String getProfile(Model model, Authentication authentication) {
        User currentUser = userService.getUserByUsername(authentication.getName()).orElse(null);
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
            model.addAttribute("questCount", userService.getUserQuestCount(currentUser));
            model.addAttribute("completedQuests", questService.getUserCompletions(currentUser));
            model.addAttribute("receivedChallenges", challengeService.getChallengesByTarget(currentUser));
            model.addAttribute("sentChallenges", challengeService.getChallengesByChallenger(currentUser));
            model.addAttribute("availableTitles", titleService.getAvailableTitles(currentUser.getLevel()));
        }
        return "profile/profile";
    }

    @GetMapping("/{id}")
    public String getUserProfile(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id).orElse(null);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("questCount", userService.getUserQuestCount(user));
            model.addAttribute("completedQuests", questService.getUserCompletions(user));
        }
        return "profile/public";
    }
}