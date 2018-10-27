package cn.jerryshell.jwtrestful.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginForm {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
