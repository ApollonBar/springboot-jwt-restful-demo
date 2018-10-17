package cn.jerryshell.jwttest.dao;

import cn.jerryshell.jwttest.domain.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDAO {
    private static Map<String, User> datasource = new HashMap<>();

    static {
        User user = new User();
        user.setUsername("jerry");
        user.setPassword("123");
        user.setEmail("email@123.com");
        datasource.put(user.getUsername(), user);
    }

    public User findByUsername(String username) {
        return datasource.getOrDefault(username, null);
    }

    public User findByUsernameAndPassword(String username, String password) {
        User user = findByUsername(username);
        if (user == null) {
            return null;
        } else if (user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }

    public User create(User user) {
        User userInDB = findByUsername(user.getUsername());
        if (userInDB != null) {
            return null;
        }

        datasource.put(user.getUsername(), user);
        return user;
    }

    public User update(User user) {
        datasource.put(user.getUsername(), user);
        return user;
    }

    public void delete(String username) {
        datasource.remove(username);
    }
}
