package com.example.hw04s_att.models;

import com.example.hw04s_att.enumm.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Orders {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String number;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Product product;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Person person;
    
    private int count;
    private float price;
    private LocalDateTime dateTime;
    private Status status;
    
    @PrePersist
    private void init() {
        dateTime = LocalDateTime.now();
    }
    
    public Orders(String number, Product product, Person person, int count, float price, Status status) {
        this.number = number;
        this.product = product;
        this.person = person;
        this.count = count;
        this.price = price;
        this.status = status;
    }
    
    public Orders() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNumber() {
        return number;
    }
    
    public void setNumber(String number) {
        this.number = number;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public Person getPerson() {
        return person;
    }
    
    public void setPerson(Person person) {
        this.person = person;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public float getPrice() {
        return price;
    }
    
    public void setPrice(float price) {
        this.price = price;
    }
    
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }

}
