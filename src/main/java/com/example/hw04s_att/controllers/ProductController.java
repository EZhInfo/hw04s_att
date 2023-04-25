package com.example.hw04s_att.controllers;

import com.example.hw04s_att.repositories.ProductRepository;
import com.example.hw04s_att.services.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductController {
    
    private final ProductRepository productRepository;
    private final ProductService productService;
    
    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }
    
    @GetMapping("")
    public String getAllProduct(Model model) {
        model.addAttribute("products", productService.getAllProduct());
        return "/product/product";
    }
    
    @GetMapping("/info/{id}")
    public String infoProduct(@PathVariable("id") int id, Model model) {
        model.addAttribute("product", productService.getProductId(id));
        return "/product/infoProduct";
    }
    
    @PostMapping("/search")
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
}
