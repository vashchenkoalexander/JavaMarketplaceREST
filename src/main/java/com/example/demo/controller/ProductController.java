package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.IProductRepository;
import com.example.demo.repository.IUserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product/")
public class ProductController {
    private IProductRepository productRepository;
    private IUserRepository userRepository;

    public ProductController(IProductRepository productRepository, IUserRepository userRepository){
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    //
    @GetMapping("products")
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    //create a new product
    @PostMapping("new")
    public Product createProduct(@RequestBody Product product){
        return productRepository.save(product);
    }

    //buy a product by Id
    @PutMapping("buyProduct/{UserId}/{ProductId}")
    public ResponseEntity<?> buyProduct(@PathVariable Long UserId, @PathVariable Long ProductId) throws ChangeSetPersister.NotFoundException {
        User user = userRepository.findById(UserId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        int amountOfMoney = user.getAmountOfMoney();

        Product product = productRepository.findById(ProductId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        int productPrice = product.getPrice();

        if(amountOfMoney < productPrice){
            throw new ChangeSetPersister.NotFoundException();
        } else {
            amountOfMoney -= productPrice;
            user.setAmountOfMoney(amountOfMoney);
            user.setAllProducts(product.getId().toString());
            return ResponseEntity.ok(userRepository.save(user));
        }
    }
}
