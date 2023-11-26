package com.letscook.letscook;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.letscook.LetscookApplication;
import com.letscook.userdetails.model.JwtResponse;
import com.letscook.userdetails.model.UserInfo;
import com.letscook.userdetails.model.UserInput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = LetscookApplication.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
public class UserDetailsControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegister() throws Exception {
        // Create test data
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("testuser@gmail.com");
        userInfo.setName("test");
        userInfo.setRole("cook");
        userInfo.setPassword("testpassword");
        System.out.println(objectMapper.writeValueAsString(userInfo));
        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInfo)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("testuser"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("testpassword"));
    }

    @Test
    public void testLogin() throws Exception {
        // Create test data
        UserInput userInput = new UserInput();
        userInput.setEmail("testuser@gmail.com");
        userInput.setPassword("testpassword");

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userInput)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("your_mocked_jwt_token"));
    }
}
