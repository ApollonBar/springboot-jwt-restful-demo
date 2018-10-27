package cn.jerryshell.jwtrestful.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterForm extends LoginForm {
    @NotBlank
    private String password2;

    @NotBlank
    @Email
    private String email;
}
