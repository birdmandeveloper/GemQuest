package com.techelevator.controller;

import com.techelevator.dao.WishlistDao;
import com.techelevator.model.Wishlist;
import com.techelevator.model.WishlistDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
@CrossOrigin
public class WishlistController {

    private final WishlistDao wishlistDao;

    public WishlistController(WishlistDao wishlistDao) {
        this.wishlistDao = wishlistDao;
    }

    @GetMapping
    public List<Wishlist> getWishlists(@RequestParam String username) {
        return wishlistDao.getWishlistsByUsername(username);
    }

    @GetMapping("/{id}")
    public Wishlist getWishlistById(@PathVariable Long id) {
        return wishlistDao.getWishlistById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createWishlist(@RequestParam String username, @RequestBody WishlistDto wishlistDto) {
        wishlistDao.createWishlist(username, wishlistDto);
    }

    @PostMapping("/{wishlistId}/products/{productId}")
    public void addProductToWishlist(@PathVariable Long wishlistId, @PathVariable Long productId) {
        wishlistDao.addProductToWishlist(wishlistId, productId);
    }

    @DeleteMapping("/{wishlistId}/products/{productId}")
    public void removeProductFromWishlist(@PathVariable Long wishlistId, @PathVariable Long productId) {
        wishlistDao.removeProductFromWishlist(wishlistId, productId);
    }

    @DeleteMapping("/{id}")
    public void deleteWishlist(@PathVariable Long id) {
        wishlistDao.deleteWishlist(id);
    }
}
