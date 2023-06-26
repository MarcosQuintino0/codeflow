package com.bugsolver.service;

import com.bugsolver.entity.Bug;
import com.bugsolver.entity.Reply;
import com.bugsolver.entity.User;
import com.bugsolver.exception.user.AlreadyUsedEmailException;
import com.bugsolver.exception.user.AlreadyUsedUsernameException;
import com.bugsolver.exception.user.UserNotFoundException;
import com.bugsolver.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("WHEN find user with valid id SHOULD return valid User")
    public void findUserByIdWithValidId(){
        User user = new User(
                1L,
                "mike.nss",
                "mike.nss@abc.com",
                "123456"
        );

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        User returnedUser = userService.findById(user.getId());
        assertThat(returnedUser).isEqualTo(user);
    }

    @Test
    @DisplayName("WHEN find user with invalid id SHOULD return UserNotFoundException")
    public void findUserByIdWithInvalidId(){

        when(userRepository.findById(anyLong())).thenThrow(new UserNotFoundException());

        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.findById(1L)
        );

    }



    @Test
    @DisplayName("WHEN save user with valid data SHOULD save a new user")
    public void saveUserWithValidData(){

        User newUser = new User().builder()
                        .username("mike.nss")
                        .email("mike.nss@abc.com")
                        .password("123456")
                        .build();

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        assertThat(userService.save(newUser)).isEqualTo(newUser);

    }

    @Test
    @DisplayName("WHEN save user with already existing email SHOULD throw a AlreadyUsedEmailException")
    public void saveUserWithInvalidEmail(){

        User newUser = new User().builder()
                        .username("mike.nss")
                        .email("mike.nss@abc.com")
                        .password("123456")
                        .build();

        when(userRepository.existsByEmail(anyString())).thenThrow(new AlreadyUsedEmailException());

        Assertions.assertThrows(AlreadyUsedEmailException.class,
            () -> userService.save(newUser)
        );

    }

    @Test
    @DisplayName("WHEN save user with already existing username SHOULD throw a AlreadyUsedUsernameException")
    public void saveUserWithInvalidUsername(){

        User newUser = new User().builder()
                        .username("mike.nss")
                        .email("mike.nss@abc.com")
                        .password("123456")
                        .build();

        when(userRepository.existsByUsername(anyString())).thenThrow(new AlreadyUsedUsernameException());

        Assertions.assertThrows(AlreadyUsedUsernameException.class,
            () -> userService.save(newUser)
        );

    }

    @Test
    @DisplayName("WHEN update user with valid id SHOULD update the user data")
    public void updateUserWithValidId(){

        User oldUser = new User().builder()
                        .username("mike.nss")
                        .email("mike.nss@abc.com")
                        .password("123456")
                        .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(oldUser);

        assertThat(userService.update(1L,oldUser)).isEqualTo(oldUser);

    }

    @Test
    @DisplayName("WHEN update user with invalid id SHOULD throw an UserNotFoundException")
    public void updateUserWithInvalidId(){

        User oldUser = new User().builder()
                        .username("mike.nss")
                        .email("mike.nss@abc.com")
                        .password("123456")
                        .build();

        when(userRepository.existsById(anyLong())).thenReturn(false);

        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.update(1L, oldUser)
        );

    }

    @Test
    @DisplayName("WHEN update user with already existing username SHOULD throw a AlreadyUsedUsernameException")
    public void updateUserWithInvalidUsername(){

        User oldUser = new User().builder()
                        .username("mike.nss")
                        .email("mike.nss@abc.com")
                        .password("123456")
                        .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByUsername(anyString())).thenThrow(new AlreadyUsedUsernameException());

        Assertions.assertThrows(AlreadyUsedUsernameException.class,
            () -> userService.update(1L,oldUser)
        );

    }

    @Test
    @DisplayName("WHEN update user with already existing email SHOULD throw a AlreadyUsedEmailException")
    public void updateUserWithInvalidEmail(){

        User oldUser = new User().builder()
                        .username("mike.nss")
                        .email("mike.nss@abc.com")
                        .password("123456")
                        .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(userRepository.existsByEmail(anyString())).thenThrow(new AlreadyUsedEmailException());

        Assertions.assertThrows(AlreadyUsedEmailException.class,
            () -> userService.update(1L,oldUser)
        );

    }

    @Test
    @DisplayName("WHEN find all user SHOULD return all users")
    public void getAllUsers(){

        when(userRepository.findAll()).thenReturn(new ArrayList<User>());
        assertThat(userService.findAll()).isEqualTo(new ArrayList<User>());
    }
    
    @Test
    @DisplayName("WHEN delete an user with valid id SHOULD delete a user")
    public void deleteUserById(){
        User userToDelete = new User().builder()
                            .id(1L)
                            .build();

        when(userRepository.existsById(anyLong())).thenReturn(true);
        userService.deleteById(userToDelete.getId());
        verify(userRepository, times(1)).deleteById(userToDelete.getId());
    }

    @Test
    @DisplayName("WHEN delete an user with invalid id SHOULD throw UserNotFoundException")
    public void deleteUserWithInvalidId(){
        User userToDelete = new User().builder()
                            .id(1L)
                            .build();

        when(userRepository.existsById(anyLong())).thenReturn(false);
        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.deleteById(userToDelete.getId())
        );
    }

    @Test
    @DisplayName("WHEN find an bug author with valid reply id SHOULD return bug author")
    public void findBugAuthorWithValidReplyId(){
        User bugAuthor = new User().builder()
                            .id(1L)
                            .build();

        Bug bug = new Bug();
        bug.setUser(bugAuthor);

        Reply reply = new Reply();
        reply.setId(1L);
        reply.setBug(bug);

        when(userRepository.findBugAuthorByReplyId(anyLong())).thenReturn(Optional.ofNullable(bugAuthor));
        assertThat(userService.findBugAuthorByReplyId(reply.getId())).isEqualTo(bugAuthor);
    }

    @Test
    @DisplayName("WHEN find an bug author with valid reply id SHOULD throw UserNotFoundException")
    public void findBugAuthorWithInvalidReplyId(){
        Reply reply = new Reply();
        reply.setId(1L);

        when(userRepository.findBugAuthorByReplyId(anyLong())).thenThrow(new UserNotFoundException());
        Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.findBugAuthorByReplyId(reply.getId())
        );
    }

}
