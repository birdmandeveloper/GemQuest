package com.techelevator.dao;

import com.techelevator.model.Wishlist;
import com.techelevator.model.WishlistDto;

import java.util.List;

public interface WishlistDao {
    List<Wishlist> getWishlistsByUsername(String username);

    Wishlist getWishlistById(Long id);

    void createWishlist(String username, WishlistDto wishlistDto);

    void addProductToWishlist(Long wishlistId, Long productId);

    void removeProductFromWishlist(Long wishlistId, Long productId);

    void deleteWishlist(Long id);

}
