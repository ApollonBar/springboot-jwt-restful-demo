package cn.jerryshell.jwttest;

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
public class JwtTestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void findUserByUsernameTest() throws Exception {
        mockMvc.perform(get("/user/has/jerry"))
                .andExpect(status().isOk())
                .andExpect(content().string("yes"));

        mockMvc.perform(get("/user/has/none"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void registerTest() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "mockUsername");
        jsonObject.put("password", "mockPassword");

        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("username").value(jsonObject.get("username")))
                .andExpect(jsonPath("password").value(jsonObject.get("password")));

        mockMvc.perform(get("/user/has/mockUsername"))
                .andExpect(status().isOk())
                .andExpect(content().string("yes"));
    }

    @Test
    public void loginAndRoleRequireAndUpdateUserTest() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "vip");
        jsonObject.put("password", "456");

        // Login Test
        String token = mockMvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();

        // RoleRequire Test
        mockMvc.perform(get("/user/verify").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(get("/user/vip").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        mockMvc.perform(get("/user/admin").header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        // UpdateUser Test
        jsonObject.put("password", "updateUserPassword");
        mockMvc.perform(put("/user").header("Authorization", token).contentType(MediaType.APPLICATION_JSON_UTF8).content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("password").value("updateUserPassword"))
                .andDo(print());
    }

}
