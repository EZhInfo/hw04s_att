package com.example.hw04s_att.controllers;

import com.example.hw04s_att.enumm.Status;
import com.example.hw04s_att.models.Category;
import com.example.hw04s_att.models.Image;
import com.example.hw04s_att.models.Orders;
import com.example.hw04s_att.models.Product;
import com.example.hw04s_att.repositories.CategoryRepository;
import com.example.hw04s_att.security.PersonDetails;
import com.example.hw04s_att.services.OrderService;
import com.example.hw04s_att.services.PersonService;
import com.example.hw04s_att.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
public class AdminController {
    
    private final CategoryRepository categoryRepository;
    private final OrderService orderService;
    
    private final PersonService personService;
    
    @Value("${loadImg.path}")
    private String loadImgPath;
    
    private final ProductService productService;
    
    public AdminController(CategoryRepository categoryRepository, OrderService orderService, PersonService personService, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.orderService = orderService;
        this.personService = personService;
        this.productService = productService;
    }
    
    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "/admin/admin";
    }
    
    @GetMapping("/admin/product/add")
    public String addProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("category", categoryRepository.findAll());
        return "product/addProduct";
    }
    
    @PostMapping("/admin/product/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @RequestParam("file01") MultipartFile file01, @RequestParam("file02") MultipartFile file02, @RequestParam("file03") MultipartFile file03, @RequestParam("file04") MultipartFile file04, @RequestParam("file05") MultipartFile file05, @RequestParam("category") int category, Model model) throws IOException {
        
        Category category_db = (Category) categoryRepository.findById(category).orElseThrow();
        
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryRepository.findAll());
            return "product/addProduct";
        }
        
        if (file01 != null) {
            File uploadDir = new File(loadImgPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file01.getOriginalFilename();
            file01.transferTo(new File(loadImgPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        
        if (file02 != null) {
            File uploadDir = new File(loadImgPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file02.getOriginalFilename();
            file02.transferTo(new File(loadImgPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        if (file03 != null) {
            File uploadDir = new File(loadImgPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file03.getOriginalFilename();
            file03.transferTo(new File(loadImgPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        if (file04 != null) {
            File uploadDir = new File(loadImgPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file04.getOriginalFilename();
            file04.transferTo(new File(loadImgPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        if (file05 != null) {
            File uploadDir = new File(loadImgPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file05.getOriginalFilename();
            file05.transferTo(new File(loadImgPath + "/" + resultFileName));
            Image image = new Image();
            image.setProduct(product);
            image.setFileName(resultFileName);
            product.addImageToProduct(image);
        }
        
        productService.saveProduct(product, category_db);
        
        return "redirect:/admin/admin";
    }
    
    @GetMapping("/admin/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
        return "redirect:/admin/admin";
    }
    
    @GetMapping("/admin/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") int id) {
        model.addAttribute("product", productService.getProductId(id));
        model.addAttribute("category", categoryRepository.findAll());
        return "product/editProduct";
    }
    @PostMapping("/admin/product/edit/{id}")
    public String editProduct(@ModelAttribute("product") @Valid Product product, BindingResult bindingResult, @PathVariable("id") int id, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("category", categoryRepository.findAll());
            return "product/editProduct";
        }
        
        productService.updateProduct(id, product);
        return "redirect:/admin/admin";
    }
    
    @GetMapping("/admin/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }
    
    @PostMapping("/admin/orders/search")
    public String productSearch(@RequestParam("search") String search, Model model) {
        
        model.addAttribute("orders", orderService.getOrdersByNumber(search.toLowerCase()));
        model.addAttribute("value_search", search);
        
        return "admin/orders";
        
    }
    
    @GetMapping("/admin/delete/orders/{id}")
    public String deleteOrder(@PathVariable("id") int id) {
        orderService.deleteOrders(id);
        return "redirect:/admin/orders";
    }
    
    @GetMapping("/admin/orders/edit/{id}")
    public String editOrder(Model model, @PathVariable("id") int id) {
        List<Status> enums = Arrays.asList(Status.values());
        model.addAttribute("status", enums);
        model.addAttribute("orders", orderService.getOrdersId(id));
        return "admin/editOrders";
    }
    
    @PostMapping("/admin/orders/edit/{id}")
    public String editOrder(@ModelAttribute("orders") @Valid Orders orders, @ModelAttribute("status") @Valid Status status, BindingResult bindingResult, @PathVariable("id") int id, Model model) {
        if (bindingResult.hasErrors()) {
            List<Status> enums = Arrays.asList(Status.values());
            model.addAttribute("status", enums);
            return "admin/editOrders";
        }
        
        //сессия пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        
        // меняем только статус, для редактирования полей - переделать форму
        Orders orders_db = orderService.getOrdersId(id);
//        orders_db.setNumber(orders.getNumber());
//        orders_db.setProduct(orders.getProduct());
//        orders_db.setPerson(personDetails.getPerson());
//        orders_db.setCount(orders.getCount());
//        orders_db.setPrice(orders.getPrice());
        orders_db.setStatus(status);
        orders_db.setDateTime(LocalDateTime.now());
        
        orderService.updateOrders(id, orders_db);
        
        return "redirect:/admin/orders";
    }
    
}
