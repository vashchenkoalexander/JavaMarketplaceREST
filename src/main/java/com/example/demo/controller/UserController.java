package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.IProductRepository;
import com.example.demo.repository.IUserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private IUserRepository userRepository;
    private IProductRepository productRepository;

    //get all users
    @GetMapping("users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("products")
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
}
