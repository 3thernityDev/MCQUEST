package re.ethernitydev.mcquest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import re.ethernitydev.mcquest.model.Challenge;
import re.ethernitydev.mcquest.model.Quest;
import re.ethernitydev.mcquest.model.User;
import re.ethernitydev.mcquest.service.ChallengeService;
import re.ethernitydev.mcquest.service.QuestService;
import re.ethernitydev.mcquest.service.UserService;

@Controller
@RequestMapping("/challenges")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestService questService;

    @GetMapping
    public String getChallenges(Model model, Authentication authentication) {
        User currentUser = userService.getUserByUsername(authentication.getName()).orElse(null);
        if (currentUser != null) {
            model.addAttribute("receivedChallenges", challengeService.getChallengesByTarget(currentUser));
            model.addAttribute("sentChallenges", challengeService.getChallengesByChallenger(currentUser));
            model.addAttribute("activeChallenges", challengeService.getActiveChallengesByTarget(currentUser));
        }
        return "challenges/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, @RequestParam(required = false) Long questId) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("quests", questService.getAllQuests());
        model.addAttribute("selectedQuestId", questId);
        return "challenges/create";
    }

    @PostMapping("/create")
    public String createChallenge(@RequestParam Long targetUserId,
                                  @RequestParam Long questId,
                                  @RequestParam int durationHours,
                                  Authentication authentication) {
        User challenger = userService.getUserByUsername(authentication.getName()).orElse(null);
        User target = userService.getUserById(targetUserId).orElse(null);
        Quest quest = questService.getQuestById(questId).orElse(null);

        if (challenger != null && target != null && quest != null && !challenger.equals(target)) {
            challengeService.createChallenge(challenger, target, quest, durationHours);
        }

        return "redirect:/challenges";
    }

    @GetMapping("/{id}")
    public String getChallengeDetails(@PathVariable Long id, Model model, Authentication authentication) {
        Challenge challenge = challengeService.getChallengeById(id).orElse(null);
        User currentUser = userService.getUserByUsername(authentication.getName()).orElse(null);

        if (challenge != null) {
            model.addAttribute("challenge", challenge);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("canComplete", currentUser != null &&
                    (currentUser.equals(challenge.getTarget()) || currentUser.equals(challenge.getChallenger())));
        }

        return "challenges/details";
    }

    @PostMapping("/{id}/complete")
    public String completeChallenge(@PathVariable Long id,
                                    @RequestParam boolean success,
                                    Authentication authentication) {
        Challenge challenge = challengeService.getChallengeById(id).orElse(null);
        User currentUser = userService.getUserByUsername(authentication.getName()).orElse(null);

        if (challenge != null && currentUser != null &&
                (currentUser.equals(challenge.getTarget()) || currentUser.equals(challenge.getChallenger()))) {
            challengeService.completeChallenge(challenge, success);
        }

        return "redirect:/challenges/" + id;
    }
}