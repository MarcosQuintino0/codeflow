package com.bugsolver.controller;

import com.bugsolver.entity.User;
import com.bugsolver.exception.user.AlreadyUsedEmailException;
import com.bugsolver.exception.user.AlreadyUsedUsernameException;
import com.bugsolver.exception.user.UserNotFoundException;
import com.bugsolver.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerTests extends BaseControllerTests{

    private static final String BASE_URL = "/user";

    @MockBean
    private UserService userService;

    @DisplayName("WHEN register a new user with valid data SHOULD create and return new user with status 201")
    @Test
    public void createNewUserWithValidData() throws Exception {
        User newUser = new User().builder()
                .username("Michael")
                .password("12345678")
                .email("mike@abc.com")
                .build();

        User createdUser = new User().builder()
                .id(1L)
                .username("Michael")
                .password("12345678")
                .email("mike@abc.com")
                .build();

        when(userService.save(any(User.class))).thenReturn(createdUser);

        mockMvc.perform(post(BASE_URL + "/register")
                        .content(convertToJson(newUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdUser.getId()))
                .andExpect(jsonPath("$.username").value(createdUser.getUsername()))
                .andExpect(jsonPath("$.email").value(createdUser.getEmail()));
    }

    @DisplayName("WHEN register a new user with already existent username SHOULD return BAD REQUEST 400")
    @Test
    public void createNewUserWithExistentUsername() throws Exception {

        User newUser = new User().builder()
                .username("Michael")
                .password("12345678")
                .email("mike@abc.com")
                .build();


        when(userService.save(any(User.class))).thenThrow(new AlreadyUsedUsernameException());

        mockMvc.perform(post(BASE_URL + "/register")
                        .content(convertToJson(newUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/register"))
                .andExpect(jsonPath("$.message").value(getMessage("user.username.already-exists")))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @DisplayName("WHEN register a new user with already existent email SHOULD return BAD REQUEST 400")
    @Test
    public void createNewUserWithExistentEmail() throws Exception {

        User newUser = new User().builder()
                .username("Michael")
                .password("12345678")
                .email("mike@abc.com")
                .build();


        when(userService.save(any(User.class))).thenThrow(new AlreadyUsedEmailException());

        mockMvc.perform(post(BASE_URL + "/register")
                        .content(convertToJson(newUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/register"))
                .andExpect(jsonPath("$.message").value(getMessage("user.email.already-exists")))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @DisplayName("WHEN register a new user with password less than 8 characters SHOULD return BAD REQUEST 400")
    @Test
    public void createNewUserWithInvalidPassword() throws Exception {

        User newUser = new User().builder()
                .username("Michael")
                .password("12345")
                .email("mike@abc.com")
                .build();

        mockMvc.perform(post(BASE_URL + "/register")
                        .content(convertToJson(newUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/register"))
                .andExpect(jsonPath("$.message").value("BAD REQUEST 400"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors[0]").value(getMessage("user.password.too-short")));
    }

    @DisplayName("WHEN register a new user with blank username SHOULD return BAD REQUEST 400")
    @Test
    public void createNewUserWithBlankUsername() throws Exception {

        User newUser = new User().builder()
                .password("12345678")
                .email("mike@abc.com")
                .build();

        mockMvc.perform(post(BASE_URL + "/register")
                        .content(convertToJson(newUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/register"))
                .andExpect(jsonPath("$.message").value("BAD REQUEST 400"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors[0]").value(getMessage("user.username.not-blank")));
    }

    @DisplayName("WHEN register a new user with blank email SHOULD return BAD REQUEST 400")
    @Test
    public void createNewUserWithBlankEmail() throws Exception {

        User newUser = new User().builder()
                .username("mike")
                .password("12345678")
                .build();

        mockMvc.perform(post(BASE_URL + "/register")
                        .content(convertToJson(newUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/register"))
                .andExpect(jsonPath("$.message").value("BAD REQUEST 400"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors[0]").value(getMessage("user.email.not-blank")));
    }

    @DisplayName("WHEN register a new user with blank password SHOULD return BAD REQUEST 400")
    @Test
    public void createNewUserWithBlankPassword() throws Exception {

        User newUser = new User().builder()
                .username("mike")
                .email("mike@abc.com")
                .build();

        mockMvc.perform(post(BASE_URL + "/register")
                        .content(convertToJson(newUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/register"))
                .andExpect(jsonPath("$.message").value("BAD REQUEST 400"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors[0]").value(getMessage("user.password.not-blank")));
    }

    @DisplayName("WHEN register a new user with invalid email SHOULD return BAD REQUEST 400")
    @Test
    public void createNewUserWithInvalidEmail() throws Exception {

        User newUser = new User().builder()
                .username("mike")
                .email("invalidEmail.com")
                .password("12345678")
                .build();

        mockMvc.perform(post(BASE_URL + "/register")
                        .content(convertToJson(newUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/register"))
                .andExpect(jsonPath("$.message").value("BAD REQUEST 400"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errors[0]").value(getMessage("user.email.invalid")));
    }

    @DisplayName("WHEN get a user with valid id SHOULD return user")
    @Test
    public void getUserWithValidId() throws Exception {

        User user = new User().builder()
                .id(1L)
                .username("mike")
                .email("mike@abc.com")
                .build();

        when(userService.findById(anyLong())).thenReturn(user);

        mockMvc.perform(get(BASE_URL + "/" + user.getId())
                        .header(AUTHORIZATION, getAuthToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @DisplayName("WHEN get a user with invalid id SHOULD return 404 NOT FOUND")
    @Test
    public void getUserWithInvalidId() throws Exception {

        when(userService.findById(anyLong())).thenThrow(new UserNotFoundException());
        long userId = anyLong();

        mockMvc.perform(get(BASE_URL + "/" + userId)
                        .header(AUTHORIZATION, getAuthToken()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path").value(BASE_URL + "/" + userId))
                .andExpect(jsonPath("$.message").value(getMessage("user.not-found")))
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    private String convertToJson(User user) throws JSONException {
        return new JSONObject()
                .put("email", user.getEmail())
                .put("username", user.getUsername())
                .put("password", user.getPassword())
                .toString();
    }
}
