package pt.ipleiria.estg.dei.ei.dae.monitoring.dtos;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class AuthDTO implements Serializable {
    @NotBlank
    private String code;
    @NotBlank
    private String password;

    public AuthDTO(String code, String password) {
        this.code = code;
        this.password = password;
    }

    public AuthDTO() {
    }

    public @NotBlank String getCode() {
        return code;
    }

    public void setCode(@NotBlank String code) {
        this.code = code;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }
}
