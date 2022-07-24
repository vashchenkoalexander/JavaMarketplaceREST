package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.IUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/")
public class UserController {

    private IUserRepository userRepository;

    public UserController(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    //get all users
    @GetMapping("users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

//    //get all products
//    @GetMapping("products")
//    public List<Product> getAllProducts(){
//        return productRepository.findAll();
//    }

    //create a new user
    @PostMapping("new")
    public User createUser(@RequestBody User user){
        return userRepository.save(user);
    }

    //delete user by Id
    @DeleteMapping("delete/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userRepository.deleteById(userId);
    }

}
