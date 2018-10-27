package cn.jerryshell.jwtrestful.controller;

import cn.jerryshell.jwtrestful.annotation.RoleRequired;
import cn.jerryshell.jwtrestful.annotation.TokenRequired;
import cn.jerryshell.jwtrestful.dao.UserDAO;
import cn.jerryshell.jwtrestful.domain.Role;
import cn.jerryshell.jwtrestful.domain.User;
import cn.jerryshell.jwtrestful.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return JWTUtil.sign(userFromDB.getUsername(), null, userFromDB.getRole());
    }

    @TokenRequired
    @GetMapping("/verify")
    public User verify(@RequestAttribute String username) {
        return userDAO.findByUsername(username);
    }

    @TokenRequired
    @RoleRequired(roles = {Role.VIP, Role.ADMINISTRATOR})
    @GetMapping("/vip")
    public User vip(@RequestAttribute String username) {
        return userDAO.findByUsername(username);
    }

    @TokenRequired
    @RoleRequired(roles = Role.ADMINISTRATOR)
    @GetMapping("/admin")
    public User admin(@RequestAttribute String username) {
        return userDAO.findByUsername(username);
    }

    @TokenRequired
    @PutMapping
    public User updateUser(@RequestBody User user, @RequestAttribute String username) {
        user.setUsername(username);
        return userDAO.update(user);
    }

}
