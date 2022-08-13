package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "firstName")
    private String firstName;
    @Column(name = "lastName")
    private String lastName;
    @Column(name = "amountOfMoney")
    private int amountOfMoney;
    
    @OneToMany(
            mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL
    )
    @JsonManagedReference
    private List<Product> products = new ArrayList<>();

    public User(){}

    public User(String firstName, String lastName, int amountOfMoney){
        this.firstName = firstName;
        this.lastName = lastName;
        this.amountOfMoney = amountOfMoney;
    }

    public void setId(long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setAmountOfMoney(int amountOfMoney){
        this.amountOfMoney = amountOfMoney;
    }

    public int getAmountOfMoney(){
        return amountOfMoney;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return products;
    }
}
