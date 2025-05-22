package re.ethernitydev.mcquest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import re.ethernitydev.mcquest.model.Challenge;
import re.ethernitydev.mcquest.model.ChallengeStatus;
import re.ethernitydev.mcquest.model.Quest;
import re.ethernitydev.mcquest.model.User;
import re.ethernitydev.mcquest.service.ChallengeService;
import re.ethernitydev.mcquest.service.QuestService;
import re.ethernitydev.mcquest.service.UserService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/challenges")
public class ChallengeController {

    @Autowired
    private QuestService questService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChallengeService challengeService;

    @GetMapping
    public String listChallenges(Model model, Authentication auth) {
        User user = userService.getUserByUsername(auth.getName()).orElse(null);

        model.addAttribute("challenges",
                challengeService.getActiveChallengesByTarget(user));
        model.addAttribute("successfulChallenges",
                challengeService.getChallengesByTargetAndStatus(user, ChallengeStatus.SUCCESS));
        model.addAttribute("failedChallenges",
                challengeService.getChallengesByTargetAndStatus(user, ChallengeStatus.FAILED));

        return "challenges/list";
    }
    @GetMapping("/create")
    public String showCreateForm(Model model, Authentication auth) {
        model.addAttribute("challengeForm", new Challenge());
        User currentUser = userService.getUserByUsername(auth.getName()).orElse(null);
        model.addAttribute("quests", questService.getAvailableQuests(currentUser));
        model.addAttribute("players", userService.getAllUsers());
        return "challenges/create";
    }

    @PostMapping("/create")
    public String createChallenge(@RequestParam("questId") Long questId,
                                  @RequestParam("targetId") Long targetId,
                                  @RequestParam("durationValue") int durationValue,
                                  @RequestParam("durationUnit") String durationUnit,
                                  Authentication auth) {
        User challenger = userService.getUserByUsername(auth.getName()).orElse(null);
        Quest quest = questService.getQuestById(questId).orElse(null);
        User target = userService.getUserById(targetId).orElse(null);
        if (challenger != null && quest != null && target != null) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime expiration;
            switch (durationUnit) {
                case "SECONDS": expiration = now.plusSeconds(durationValue); break;
                case "HOURS":   expiration = now.plusHours(durationValue);   break;
                case "DAYS":    expiration = now.plusDays(durationValue);    break;
                default:         expiration = now.plusHours(durationValue);   break;
            }
            challengeService.createChallenge(challenger, target, quest, expiration);
        }
        return "redirect:/challenges";
    }

    @GetMapping("/{id}")
    public String viewChallengeDetails(@PathVariable Long id,
                                       Model model,
                                       Authentication auth) {
        Challenge challenge = challengeService.getChallengeById(id).orElse(null);
        model.addAttribute("challenge", challenge);
        return "challenges/details";
    }
}
