package com.example.hw04s_att.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @NotEmpty(message = "Наименование должно быть заполнено")
    @Column(name = "title", nullable = false, columnDefinition = "text", unique = true)
    private String title;
    
    @Column(name = "description", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Необходимо указать описание товара")
    private String description;
    
    @Column(name = "price", nullable = false)
    @Min(value = 1, message = "Цена дожна быть не менее 1 руб")
    private float price;
    
    @Column(name = "warehouse", nullable = false)
    @NotEmpty(message = "Необходимо указать склад")
    private String warehouse;
    
    @Column(name = "seller")
    @NotEmpty(message = "Необходимо указать продавца")
    private  String seller;
    
    @ManyToOne(optional = false)
    private Category category;
    
    private LocalDateTime dateTime;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<Image> imagesList = new ArrayList<>();
    
    @ManyToMany()
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Person> personList;
    
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Orders> orderList;
    
    public void addImageToProduct(Image image){
        image.setProduct(this);
        imagesList.add(image);
    }
    
    //инициализация при создании объекта
    @PrePersist
    private void init(){
       dateTime = LocalDateTime.now();
    }
    
    public Product(String title, String description, float price, String warehouse, String seller, Category category, LocalDateTime dateTime, List<Image> imagesList) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.warehouse = warehouse;
        this.seller = seller;
        this.category = category;
        this.dateTime = dateTime;
        this.imagesList = imagesList;
    }
    
    public Product() {
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public float getPrice() {
        return price;
    }
    
    public void setPrice(float price) {
        this.price = price;
    }
    
    public String getWarehouse() {
        return warehouse;
    }
    
    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }
    
    public String getSeller() {
        return seller;
    }
    
    public void setSeller(String seller) {
        this.seller = seller;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    
    public List<Image> getImagesList() {
        return imagesList;
    }
    
    public void setImagesList(List<Image> imagesList) {
        this.imagesList = imagesList;
    }
    
    @Override
    public String toString() {
        return
                "\nТовар №" + id +
                "\n\tНаименование: " + title +
                "\n\tОписание: " + description +
                "\n\tКатегория: " + category.getName() +
                "\n\tЦена: " + price +
                "\n\tСклад: " + warehouse +
                "\n\tПоставщик: " + seller;
    }
}
