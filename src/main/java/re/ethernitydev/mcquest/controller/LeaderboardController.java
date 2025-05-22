package re.ethernitydev.mcquest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import re.ethernitydev.mcquest.service.UserService;

@Controller
@RequestMapping("/leaderboard")
public class LeaderboardController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String getLeaderboard(Model model) {
        model.addAttribute("topPlayers", userService.getTopUsers(20));
        return "leaderboard/leaderboard";
    }
}