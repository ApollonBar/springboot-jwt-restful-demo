package cn.jerryshell.jwtrestful.controller;

import cn.jerryshell.jwtrestful.annotation.RoleRequired;
import cn.jerryshell.jwtrestful.annotation.TokenRequired;
import cn.jerryshell.jwtrestful.dao.UserDAO;
import cn.jerryshell.jwtrestful.exception.ResourceNotFoundException;
import cn.jerryshell.jwtrestful.model.Role;
import cn.jerryshell.jwtrestful.model.User;
import cn.jerryshell.jwtrestful.payload.UserInfoUpdateForm;
import cn.jerryshell.jwtrestful.payload.UserPasswordUpdateForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    @RoleRequired(roles = {Role.ROLE_VIP, Role.ROLE_ADMIN})
    @GetMapping("/vip")
    public User vip(@RequestAttribute String username) {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
    }

    @TokenRequired
    @RoleRequired(roles = Role.ROLE_ADMIN)
    @GetMapping("/admin")
    public User admin(@RequestAttribute String username) {
        return userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
    }

    @TokenRequired
    @PutMapping
    public User updateUserInfo(@Valid @RequestBody UserInfoUpdateForm userInfoUpdateForm, @RequestAttribute String username) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
        user.setEmail(userInfoUpdateForm.getEmail());
        return userDAO.save(user);
    }

    @TokenRequired
    @PutMapping("/password")
    public User updateUserPassword(@Valid @RequestBody UserPasswordUpdateForm userPasswordUpdateForm, @RequestAttribute String username) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> ResourceNotFoundException.build("User", "Username", username));
        user.setPassword(userPasswordUpdateForm.getPassword());
        return userDAO.save(user);
    }

}
