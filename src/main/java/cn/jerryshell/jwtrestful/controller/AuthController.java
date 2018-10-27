package cn.jerryshell.jwtrestful.controller;

import cn.jerryshell.jwtrestful.annotation.TokenRequired;
import cn.jerryshell.jwtrestful.dao.UserDAO;
import cn.jerryshell.jwtrestful.exception.ResourceNotFoundException;
import cn.jerryshell.jwtrestful.model.User;
import cn.jerryshell.jwtrestful.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @PostMapping("register")
    public User register(@RequestBody User user) {
        return userDAO.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User userFromDB = userDAO.findByUsernameAndPassword(user.getUsername(), user.getPassword())
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
