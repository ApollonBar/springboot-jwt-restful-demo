package cn.jerryshell.jwtrestful.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserInfoUpdateForm {
    @NotBlank
    @Size(max = 100)
    @Email
    private String email;
}
