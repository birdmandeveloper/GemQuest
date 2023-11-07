

package com.techelevator.dao;

import com.techelevator.model.Product;
import com.techelevator.exception.DaoException;
import java.util.List;

public interface ProductDao {
    List<Product> getAllProducts() throws DaoException;
    List<Product> searchProducts(String sku, String name) throws DaoException;
    Product getProductById(Long id) throws DaoException;

    List<Product> searchProductsBySku(String sku);

    List<Product> searchProductsByName(String name);
}