package com.techelevator.controller;

import com.techelevator.dao.ShoppingCartDao;
import com.techelevator.model.ShoppingCartDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin
public class CartController {

    private final ShoppingCartDao shoppingCartDao;

    public CartController(ShoppingCartDao shoppingCartDao) {
        this.shoppingCartDao = shoppingCartDao;
    }

    @GetMapping
    public ShoppingCartDto getCart(@RequestParam String username) {
        return shoppingCartDao.getShoppingCart(username);
    }

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public void addToCart(@RequestParam String username, @RequestParam Long productId, @RequestParam int quantity) {
        shoppingCartDao.addToCart(username, productId, quantity);
    }

    @DeleteMapping("/items/{itemId}")
    public void removeFromCart(@PathVariable Long itemId) {
        shoppingCartDao.removeFromCart(itemId);
    }

    @DeleteMapping
    public void clearCart(@RequestParam String username) {
        shoppingCartDao.clearCart(username);
    }
}
