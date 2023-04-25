package com.example.hw04s_att.models;

import com.example.hw04s_att.enumm.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty(message = "Логин не заполнен")
    @Size(min = 3, max = 100, message = "Логин должен быть от 3х и до 100 символов")
    @Column(name = "login")
    private String login;
    @NotEmpty(message = "Пароль не заполнен")
    //    @Size(min = 3, max = 100, message = "Пароль должен быть от 3х и до 100 символов")
    @Column(name = "password")
    private String password;
    
    @Column(name = "role")
    private String role;
    
    @ManyToMany()
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "person_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> productList;
    
    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private List<Orders> orderList;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getLogin() {
        return login;
    }
    
    public void setLogin(String login) {
        this.login = login;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        
        Person person = (Person) o;
        
        if (id != person.id)
            return false;
        if (!Objects.equals(login, person.login))
            return false;
        return Objects.equals(password, person.password);
    }
    
    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
    
}
