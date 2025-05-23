package re.ethernitydev.mcquest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProfileForm {

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Email
    private String email;

    private String oldPassword;

    @Size(min = 6, message = "Le nouveau mot de passe doit faire au moins 6 caractères")
    private String newPassword;

    // getters / setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
