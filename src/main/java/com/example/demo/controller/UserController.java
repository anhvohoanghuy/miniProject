package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.model.UserClaim;
import com.example.demo.repository.IUserClaimRepository;
import com.example.demo.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IUserClaimRepository userClaim;
    @GetMapping
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getUserByUserName(@PathVariable String id){
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

}
