package com.techelevator.controller;

import com.techelevator.dao.ProductDao;
import com.techelevator.model.Product;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin
public class ProductController {

    private final ProductDao productDao;

    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @GetMapping(params = {"sku"})
    public List<Product> findProductsBySku(@RequestParam String sku) {
        return productDao.searchProductsBySku(sku);
    }

    @GetMapping(params = {"name"})
    public List<Product> findProductsByName(@RequestParam String name) {
        return productDao.searchProductsByName(name);
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productDao.getProductById(id);
    }
}
