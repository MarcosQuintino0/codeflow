package com.bugsolver.service;

import com.bugsolver.entity.User;
import com.bugsolver.exception.user.AlreadyUsedEmailException;
import com.bugsolver.exception.user.AlreadyUsedUsernameException;
import com.bugsolver.exception.user.UserNotFoundException;
import com.bugsolver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User save(User user){
        if(userRepository.existsByEmail(user.getEmail())){
            throw new AlreadyUsedEmailException();
        }
        else if(userRepository.existsByUsername(user.getUsername())){
            throw new AlreadyUsedUsernameException();
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(Long id, User user){
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException();
        }
        else if(userRepository.existsByEmail(user.getEmail())){
            throw new AlreadyUsedEmailException();
        }
        else if(userRepository.existsByUsername(user.getUsername())){
            throw new AlreadyUsedUsernameException();
        }

        user.setId(id);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteById(Long id){
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException();
        }

        userRepository.deleteById(id);
    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException()
        );
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException()
        );
    }


    public User findBugAuthorByReplyId(Long replyId){
        return userRepository.findBugAuthorByReplyId(replyId).orElseThrow(
                () -> new UserNotFoundException()
        );
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public Long countAllUsers() {
        return userRepository.count();
    }

}
