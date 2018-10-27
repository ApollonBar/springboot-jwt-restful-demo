package cn.jerryshell.jwtrestful.controller;

import cn.jerryshell.jwtrestful.annotation.TokenRequired;
import cn.jerryshell.jwtrestful.dao.UserDAO;
import cn.jerryshell.jwtrestful.exception.ResourceNotFoundException;
import cn.jerryshell.jwtrestful.model.Role;
import cn.jerryshell.jwtrestful.model.User;
import cn.jerryshell.jwtrestful.payload.LoginForm;
import cn.jerryshell.jwtrestful.payload.RegisterForm;
import cn.jerryshell.jwtrestful.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterForm registerForm) {
        @NotBlank String password = registerForm.getPassword();
        @NotBlank String password2 = registerForm.getPassword2();
        if (!password.equals(password2)) {
            throw new RuntimeException("注册失败，密码和确认密码不符");
        }
        @NotBlank String username = registerForm.getUsername();
        if (userDAO.existsByUsername(username)) {
            throw new RuntimeException("注册失败，用户名已经存在");
        }
        @NotBlank @Email String email = registerForm.getEmail();
        if (userDAO.existsByEmail(email)) {
            throw new RuntimeException("注册失败，邮箱已经被使用");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(Role.ROLE_USER);
        return userDAO.save(user);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginForm loginForm) {
        User userFromDB = userDAO.findByUsernameAndPassword(loginForm.getUsername(), loginForm.getPassword())
                .orElseThrow(() -> new RuntimeException("登录失败，用户名或密码错误"));
        return JWTUtil.sign(userFromDB.getUsername(), null, userFromDB.getRole());
    }

    @TokenRequired
    @GetMapping("/verify")
    public User verify(@RequestAttribute String username) {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
    }
}
