package re.ethernitydev.mcquest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import re.ethernitydev.mcquest.dto.ProfileForm;
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

    @Autowired
    private UserDetailsService userDetailsService;


    @GetMapping
    public String getProfile(Model model, Authentication authentication) {
        User currentUser = userService
                .getUserByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur introuvable"));

        model.addAttribute("user", currentUser);
        model.addAttribute("questCount", userService.getUserQuestCount(currentUser));
        model.addAttribute("completedQuests", questService.getUserCompletions(currentUser));
        model.addAttribute("receivedChallenges", challengeService.getChallengesByTarget(currentUser));
        model.addAttribute("sentChallenges", challengeService.getChallengesByChallenger(currentUser));
        model.addAttribute("availableTitles", titleService.getAvailableTitles(currentUser.getLevel()));
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

    @GetMapping("/edit")
    public String getEditProfile(Model model, Authentication auth) {
        User current = userService.getUserByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ProfileForm form = new ProfileForm();
        form.setUsername(current.getUsername());
        form.setEmail(current.getEmail());
        model.addAttribute("userForm", form);
        return "profile/edit";
    }


    @PostMapping("/edit")
    public String postEditProfile(
            @ModelAttribute("userForm") ProfileForm form,
            BindingResult br,
            Authentication auth,
            Model model) {

        User current = userService.getUserByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (form.getNewPassword() != null && !form.getNewPassword().isBlank()) {
            if (form.getOldPassword() == null
                    || form.getOldPassword().isBlank()
                    || !userService.checkPassword(current, form.getOldPassword())) {
                br.rejectValue("oldPassword", "wrong.password", "Ancien mot de passe incorrect");
            }
        }

        if (br.hasErrors()) {
            return "profile/edit";
        }

        userService.updateProfile(
                current,
                form.getUsername(),
                form.getEmail(),
                form.getNewPassword()
        );

        UserDetails updatedUser =
                userDetailsService.loadUserByUsername(form.getUsername());

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedUser,
                auth.getCredentials(),
                updatedUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/profile?success";
    }
}