package com.example.hw04s_att.controllers;

import com.example.hw04s_att.enumm.Role;
import com.example.hw04s_att.enumm.Status;
import com.example.hw04s_att.models.Cart;
import com.example.hw04s_att.models.Orders;
import com.example.hw04s_att.models.Person;
import com.example.hw04s_att.models.Product;
import com.example.hw04s_att.repositories.CartRepository;
import com.example.hw04s_att.repositories.ProductRepository;
import com.example.hw04s_att.security.PersonDetails;
import com.example.hw04s_att.services.OrderService;
import com.example.hw04s_att.services.PersonService;
import com.example.hw04s_att.services.ProductService;
import com.example.hw04s_att.util.PersonValidator;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class MainController {
    
    private final PersonValidator personValidator;
    private final PersonService personService;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderService orderService;
    
    public MainController(PersonValidator personValidator, PersonService personService, ProductService productService, ProductRepository productRepository, CartRepository cartRepository, OrderService orderService) {
        this.personValidator = personValidator;
        this.personService = personService;
        this.productService = productService;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.orderService = orderService;
    }
    
    @GetMapping("/person_account")
    public String person_account(Model model) {
        //        получаем объект аутентификации из сессии текущего пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String role = personDetails.getPerson().getRole();
        if (Role.ADMIN.getTypeRole().equals(role)){
            return "redirect:/admin";
        }
        
        model.addAttribute("products", productService.getAllProduct());
        return "/user/person_account";
    }
    
    @GetMapping("/registration")
    public String registration(@ModelAttribute("person") Person person) {
        return "registration";
    }
    
    @PostMapping("/registration")
    public String resultRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "/registration";
        }
        personService.registerPerson(person);
        
        return "redirect:/person_account";
    }
    
    @GetMapping("/person_account/product/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", productService.getProductId(id));
        return "/user/infoProduct";
    }
    
    @PostMapping("/person_account/product/search")
    public String productSearch(@RequestParam("search") String search, @RequestParam("ot") String Ot, @RequestParam("do") String Do, @RequestParam(value = "price", required = false, defaultValue = "") String price, @RequestParam(value = "category", required = false, defaultValue = "") String category, Model model) {
        
        model.addAttribute("products", productService.getAllProduct());
        
        if (!Ot.isEmpty() & !Do.isEmpty()) {
            if (!price.isEmpty()) {
                if (price.equals("sorted_by_asc_price")) {
                    if (!category.isEmpty()) {
                        if (category.equals("furniture")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(Ot), Float.valueOf(Do), 1));
                        } else if (category.equals("appliance")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 3));
                        } else if (category.equals("clothes")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 2));
                        }
                    } else {
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceAsc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do)));
                    }
                } else if (price.equals("sorted_by_desc_price")) {
                    if (!category.isEmpty()) {
                        if (category.equals("furniture")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 1));
                        } else if (category.equals("appliance")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 3));
                        } else if (category.equals("clothes")) {
                            model.addAttribute("search_product", productRepository.findByTitleAndCategoryOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do), 2));
                        }
                    } else {
                        model.addAttribute("search_product", productRepository.findByTitleOrderByPriceDesc(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do)));
                    }
                }
            } else {
                model.addAttribute("search_product", productRepository.findByTitleAndPriceGreaterThanEqualAndPriceLessThanEqual(search.toLowerCase(), Float.parseFloat(Ot), Float.parseFloat(Do)));
            }
        } else {
            model.addAttribute("search_product", productRepository.findByTitleContainingIgnoreCase(search));
        }
        
        model.addAttribute("value_search", search);
        model.addAttribute("value_ot", Ot);
        model.addAttribute("value_do", Do);
        
        return "/product/product";
        
    }
    
    @GetMapping("/cart/add/{id}")
    public String addProductInCart(@PathVariable("id") int id, Model model){
        //продукт
        Product product = productRepository.getReferenceById(id);
        //сессия пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        
        int idPerson = personDetails.getPerson().getId();
        Cart cart = new Cart(idPerson, product.getId());
        
        cartRepository.save(cart);
    
        return "redirect:/cart";
    }
    
    @GetMapping("/cart")
    public String cart(Model model){
        //сессия пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        
        int idPerson = personDetails.getPerson().getId();
        
        List<Cart> cartList = cartRepository.findByPersonId(idPerson);
//        List<Cart> cartList = cartRepository.findDistinctProductByPersonId(idPerson);
        List<Product> productList = new ArrayList<>();
        
        float total = 0;
        Product product;
        for (Cart cart: cartList) {
            product = productService.getProductId(cart.getProductId());
            productList.add(product);
            total += product.getPrice();
        }
        
        model.addAttribute("cart_product", productList);
        model.addAttribute("total", total);
        
        return "/user/cart";
    }
    
    @GetMapping("/delete/info/{id}")
    public String deleteProductFromCart(Model model, @PathVariable("id") int id){
        
        //сессия пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        
        int idPerson = personDetails.getPerson().getId();
        
        List<Cart> cartList = cartRepository.findByPersonId(idPerson);
        List<Product> productList = new ArrayList<>();
        
        for (Cart cart: cartList) {
            productList.add(productService.getProductId(cart.getProductId()));
        }
        
        cartRepository.deleteCartByProductId(id);
        
        return "redirect:/cart";
    }
    
    @GetMapping("/orders/create")
    public String order(Model model){
        //сессия пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        
        int idPerson = personDetails.getPerson().getId();
        
        List<Cart> cartList = cartRepository.findByPersonId(idPerson);
        //        List<Cart> cartList = cartRepository.findDistinctProductByPersonId(idPerson);
        List<Product> productList = new ArrayList<>();
        
        float total = 0;
        Product product;
        for (Cart cart: cartList) {
            product = productService.getProductId(cart.getProductId());
            productList.add(product);
            total += product.getPrice();
        }
        
        String uuid = UUID.randomUUID().toString();
        for (Product prod : productList) {
            Orders newOrder = new Orders(uuid, prod, personDetails.getPerson(), 1, prod.getPrice(), Status.Оформлен);
            orderService.saveOrders(newOrder);
            cartRepository.deleteCartByProductId(prod.getId());
        }
        
        return "redirect:/orders";
    }
    
    @GetMapping("/orders")
    public String ordersUser(Model model){
        //сессия пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        
        int idPerson = personDetails.getPerson().getId();
        
        List<Orders> orderList = orderService.getOrdersByPersonId(idPerson);
        
        float total = 0;
        for (Orders orders: orderList) {
            total += orders.getPrice();
        }
        
        model.addAttribute("total", total);
        model.addAttribute("orders", orderList);
        return "/user/orders";
        
    }
    
    @GetMapping("/delete/orders/{order_id}/{product_id}")
    public String deleteProductFromOrder(Model model, @PathVariable("order_id") int order_id, @PathVariable("product_id") int product_id){
        
        //сессия пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        
        int idPerson = personDetails.getPerson().getId();
        
        List<Orders> orderList = orderService.getOrdersByPersonIdAndProductId(idPerson, product_id);
        List<Product> productList = new ArrayList<>();

        for (Orders ord: orderList) {
            productList.add(productService.getProductId(ord.getProduct().getId()));
        }

        orderService.deleteOrdersByIdAndProductId(order_id, product_id);
        
        return "redirect:/orders";
    }
    
}
