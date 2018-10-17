package cn.jerryshell.jwttest.controller;

import cn.jerryshell.jwttest.annotation.TokenRequired;
import cn.jerryshell.jwttest.dao.UserDAO;
import cn.jerryshell.jwttest.domain.User;
import cn.jerryshell.jwttest.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/has/{username}")
    public String findUserByUsername(@PathVariable String username) {
        User user = userDAO.findByUsername(username);
        if (user == null) {
            return null;
        }
        return "yes";
    }

    @PostMapping
    public User register(@RequestBody User user) {
        return userDAO.create(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        User userFromDB = userDAO.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (userFromDB == null) {
            return null;
        }
        return JWTUtil.signWithExpires(userFromDB.getUsername(), new Date(System.currentTimeMillis() + 10 * 1000));
    }

    @TokenRequired
    @GetMapping("/verify")
    public String verify(@RequestAttribute String username) {
        return "Hello " + username;
    }

    @TokenRequired
    @PutMapping
    public User updateUser(@RequestBody User user, @RequestAttribute String username) {
        user.setUsername(username);
        return userDAO.update(user);
    }

}
