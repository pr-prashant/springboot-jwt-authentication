package com.example.springboot.jwt.application.controller;

import com.example.springboot.jwt.application.beans.LoginBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
@EnableAutoConfiguration
public class AuthUserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void shouldReturnAuthTokenOnSuccessfulLogin() throws Exception {
        // add principal object to SecurityContextHolder
        LoginBean user = new LoginBean();
        user.setUsername("prashant");
        user.setPassword("Welcome1234");
        mockMvc.perform(MockMvcRequestBuilders.post(ControllerConstants.SERVICE_ROOT + ControllerConstants.LOGIN).content(mapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    public void shouldNotReturnAuthTokenOnUnsuccessfulLogin() throws Exception {
        // add principal object to SecurityContextHolder
        LoginBean user = new LoginBean();
        user.setUsername("test");
        user.setPassword("test");
        mockMvc.perform(MockMvcRequestBuilders.post(ControllerConstants.SERVICE_ROOT + ControllerConstants.LOGIN).content(mapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void shouldAllowUsersToLogout() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ControllerConstants.SERVICE_ROOT + ControllerConstants.LOGOUT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    @WithMockUser
    public void shouldAllowUsersToRefreshToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ControllerConstants.SERVICE_ROOT + ControllerConstants.REFRESH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.error").doesNotExist());
    }

}
