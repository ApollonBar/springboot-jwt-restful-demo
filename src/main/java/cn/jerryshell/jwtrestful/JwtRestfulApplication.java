package cn.jerryshell.jwtrestful;

import cn.jerryshell.jwtrestful.dao.UserDAO;
import cn.jerryshell.jwtrestful.model.Role;
import cn.jerryshell.jwtrestful.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwtRestfulApplication implements CommandLineRunner {

    private UserDAO userDAO;

    public static void main(String[] args) {
        SpringApplication.run(JwtRestfulApplication.class, args);
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void run(String... args) {
        // 初始化用户数据
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("123");
        admin.setEmail("admin@email.com");
        admin.setRole(Role.ADMINISTRATOR);

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

        userDAO.save(admin);
        userDAO.save(vip);
        userDAO.save(user);
    }
}
