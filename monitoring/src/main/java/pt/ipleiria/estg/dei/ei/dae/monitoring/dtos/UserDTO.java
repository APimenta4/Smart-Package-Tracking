package pt.ipleiria.estg.dei.ei.dae.monitoring.dtos;

import org.hibernate.Hibernate;
import pt.ipleiria.estg.dei.ei.dae.monitoring.entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {
    private String code;
    private String name;
    private String email;
    private String role;

    public UserDTO() {
    }
    public UserDTO(String code, String name, String email, String role) {
        this.code = code;
        this.name = name;
        this.email = email;
        this.role = role;
    }
    public static UserDTO from(User user) {
        return new UserDTO(
                user.getCode(),
                user.getName(),
                user.getEmail(),
                Hibernate.getClass(user).getSimpleName()
        );
    }
    public static List<UserDTO> from(List<User> users) {
        return users.stream().map(UserDTO::from).collect(Collectors.toList());
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
