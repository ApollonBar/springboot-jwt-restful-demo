package cn.jerryshell.jwttest.dao;

import cn.jerryshell.jwttest.domain.Role;
import cn.jerryshell.jwttest.domain.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDAO {
    private static Map<String, User> datasource = new HashMap<>();

    // 初始化用户数据
    static {
        User jerry = new User();
        jerry.setUsername("jerry");
        jerry.setPassword("123");
        jerry.setEmail("jerry@email.com");
        jerry.setRole(Role.ADMINISTRATOR);

        User vip = new User();
        vip.setUsername("vip");
        vip.setPassword("456");
        vip.setEmail("vip@email.com");
        vip.setRole(Role.VIP);

        User user = new User();
        user.setUsername("user");
        user.setPassword("789");
        user.setEmail("user@email.com");
        user.setRole(Role.USER);

        datasource.put(jerry.getUsername(), jerry);
        datasource.put(vip.getUsername(), vip);
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
