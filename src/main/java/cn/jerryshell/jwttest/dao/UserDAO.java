package cn.jerryshell.jwttest.dao;

import cn.jerryshell.jwttest.domain.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDAO {
    private static List<User> database = new ArrayList<>();

    static {
        User user = new User();
        user.setUsername("jerry");
        user.setPassword("123");
        user.setEmail("email@123.com");
        database.add(user);
    }

    public User findUserByUsername(String username) {
        for (User user : database) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public User findUserByUsernameAndPassword(String username, String password) {
        User user = findUserByUsername(username);
        if (user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public User create(User user) {
        for (User e : database) {
            if (e.getUsername().equals(user.getUsername())) {
                return null;
            }
        }
        database.add(user);
        return user;
    }

    public User updateUserByUsername(User user) {
        for (int i = 0; i < database.size(); i++) {
            if (database.get(i).getUsername().equals(user.getUsername())) {
                database.set(i, user);
                return user;
            }
        }
        return null;
    }

    public void deleteByUsername(String username) {
        for (int i = 0; i < database.size(); i++) {
            if (database.get(i).getUsername().equals(username)) {
                database.remove(i);
                return;
            }
        }
    }
}
