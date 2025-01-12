package pt.ipleiria.estg.dei.ei.dae.monitoring.dtos;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class PasswordDTO implements Serializable {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String confirmPassword;

    public PasswordDTO(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public PasswordDTO() {
    }

    public @NotBlank String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(@NotBlank String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public @NotBlank String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(@NotBlank String newPassword) {
        this.newPassword = newPassword;
    }

    public @NotBlank String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotBlank String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
