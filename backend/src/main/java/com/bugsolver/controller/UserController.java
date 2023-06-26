package com.bugsolver.controller;

import com.bugsolver.entity.Bug;
import com.bugsolver.entity.User;
import com.bugsolver.service.BugService;
import com.bugsolver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User newUser){
        User userCreated = userService.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("")
    public ResponseEntity<Map<String,Long>> getTotalUsersCount(){
        return ResponseEntity.ok(Map.of("totalUsers", userService.countAllUsers()));
    }

}
