package cn.jerryshell.jwtrestful.controller;

import cn.jerryshell.jwtrestful.annotation.RoleRequired;
import cn.jerryshell.jwtrestful.annotation.TokenRequired;
import cn.jerryshell.jwtrestful.dao.UserDAO;
import cn.jerryshell.jwtrestful.domain.Role;
import cn.jerryshell.jwtrestful.domain.User;
import cn.jerryshell.jwtrestful.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserDAO userDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GetMapping("/username/{username}")
    public User findUserByUsername(@PathVariable String username) {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
    }

    @TokenRequired
    @RoleRequired(roles = {Role.VIP, Role.ADMINISTRATOR})
    @GetMapping("/vip")
    public User vip(@RequestAttribute String username) {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
    }

    @TokenRequired
    @RoleRequired(roles = Role.ADMINISTRATOR)
    @GetMapping("/admin")
    public User admin(@RequestAttribute String username) {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
    }

    @TokenRequired
    @PutMapping
    public User updateUser(@RequestBody User user, @RequestAttribute String username) {
        user.setUsername(username);
        return userDAO.save(user);
    }

}
