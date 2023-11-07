package com.techelevator.dao;

import com.techelevator.model.ShoppingCartDto;
import com.techelevator.exception.DaoException;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartDao {
    ShoppingCartDto getShoppingCart(String username) throws DaoException;
    void addToCart(String username, Long productId, int quantity) throws DaoException;
    void removeFromCart(Long itemId) throws DaoException;
    void clearCart(String username) throws DaoException;
}