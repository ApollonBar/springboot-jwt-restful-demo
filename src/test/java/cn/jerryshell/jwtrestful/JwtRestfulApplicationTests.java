package cn.jerryshell.jwtrestful;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JwtRestfulApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void findUserByUsernameTest() throws Exception {
        mockMvc.perform(get("/users/username/admin"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/users/username/none"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void registerTest() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "mockUsername");
        jsonObject.put("password", "mockPassword");
        jsonObject.put("password2", "mockPassword");
        jsonObject.put("email", "mockEmail@email.com");

        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("username").value(jsonObject.get("username")))
                .andExpect(jsonPath("password").value(jsonObject.get("password")));

        mockMvc.perform(get("/users/username/mockUsername"))
                .andExpect(status().isOk());
    }

    @Test
    public void loginAndRoleRequireAndUpdateUserTest() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "vip");
        jsonObject.put("password", "456");

        // Login Test
        String token = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        // RoleRequire Test
        mockMvc.perform(get("/auth/verify").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(get("/users/vip").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(get("/users/admin").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        // UpdateUser Test
        jsonObject.put("email", "updateEmail@email.com");
        mockMvc.perform(put("/users").header("Authorization", token).contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("email").value("updateEmail@email.com"))
                .andDo(print());

        jsonObject.put("password", "updateUserPassword");
        mockMvc.perform(put("/users/password").header("Authorization", token).contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("password").value("updateUserPassword"))
                .andDo(print());
    }

}
