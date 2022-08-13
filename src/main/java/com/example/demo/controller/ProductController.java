package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.IProductRepository;
import com.example.demo.repository.IUserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/product/")
public class ProductController {
    private IProductRepository productRepository;
    private IUserRepository userRepository;

    public ProductController(IProductRepository productRepository, IUserRepository userRepository){
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    /*
    linkTo create bounds with other methods for show it in response body in request
    get all products
     */
    @GetMapping("products")
    CollectionModel<EntityModel<Product>> getAllProducts(){
        List<EntityModel<Product>> products = productRepository.findAll().stream()
                .map(product -> EntityModel.of(
                        product,
                        linkTo(methodOn(ProductController.class).getOneProduct(product.getId())).withSelfRel(),
                        linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products")))
                .collect(Collectors.toList());

        return CollectionModel.of(products,
                linkTo(methodOn(ProductController.class).getAllProducts()).withSelfRel());
    }

    /*
    linkTo create bounds with other methods for show it in response body in request
    getting one product by id
     */
    @GetMapping("{id}")
    public EntityModel<Product> getOneProduct(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        return  EntityModel.of(product,
                linkTo(methodOn(ProductController.class).getOneProduct(id)).withSelfRel(),
                linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products"));

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
        List<Product> products = new ArrayList<>();

        Product product = productRepository.findById(ProductId).orElseThrow(ChangeSetPersister.NotFoundException::new);
        int productPrice = product.getPrice();

        products.add(product);

        if(amountOfMoney < productPrice){
            throw new ChangeSetPersister.NotFoundException();
        } else {
            amountOfMoney -= productPrice;
            user.setAmountOfMoney(amountOfMoney);
            user.setProducts(products);
            return ResponseEntity.ok(userRepository.save(user));
        }
    }

    /*
    delete product by his Id
     */
    @DeleteMapping("{ProductId}")
    public EntityModel<Product> deleteProductById (@PathVariable Long ProductId){
        Product product = productRepository.findById(ProductId).orElseThrow();
        if (product != null){
            productRepository.deleteById(ProductId);
            return EntityModel.of(product,
                    linkTo(methodOn(ProductController.class).getOneProduct(ProductId)).withSelfRel(),
                    linkTo(methodOn(ProductController.class).getAllProducts()).withRel("products"));
        }else{
            return null;
        }
    }

    // TODO: Create method delete Product by Id and delete this product from all users if they have it


}
