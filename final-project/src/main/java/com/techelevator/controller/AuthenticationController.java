package com.techelevator.controller;

import javax.validation.Valid;
import com.techelevator.dao.ProductDao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.techelevator.dao.UserDao;
import com.techelevator.security.jwt.TokenProvider;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * AuthenticationController is a class used for handling requests to authenticate Users.
 *
 * It depends on an instance of a UserDao for retrieving and storing user data. This is provided
 * through dependency injection.
 */
@RestController
@CrossOrigin
public class AuthenticationController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserDao userDao;

    public AuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public LoginResponseDto login(@Valid @RequestBody LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication, false);

            User user = userDao.getUserByUsername(loginDto.getUsername());
            return new LoginResponseDto(jwt, user);
        }
        catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public User register(@Valid @RequestBody RegisterUserDto newUser) {
        try {
            User user = userDao.createUser(
                    new User(newUser.getUsername(),newUser.getPassword(), newUser.getRole(), newUser.getName(), newUser.getAddress(), newUser.getCity(), newUser.getStateCode(), newUser.getZIP())
            );
            return user;
        }
        catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    } }
/* package com.techelevator.controller;

        import com.techelevator.dao.ProductDao;
        import com.techelevator.dao.ShoppingCartDao;
        import com.techelevator.dao.UserDao;
        import com.techelevator.exception.DaoException;
        import com.techelevator.model.*;
        import com.techelevator.security.jwt.TokenProvider;
        import com.techelevator.service.ExternalTaxApiService;
        import org.springframework.http.HttpStatus;
        import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
        import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
        import org.springframework.security.core.Authentication;
        import org.springframework.security.core.context.SecurityContextHolder;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.server.ResponseStatusException;

        import javax.validation.Valid;
        import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class AuthenticationController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserDao userDao;
    private final ProductDao productDao;
    private final ShoppingCartDao shoppingCartDao;
    private final ExternalTaxApiService externalTaxApiService;

    public AuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
                                    UserDao userDao, ProductDao productDao, ShoppingCartDao shoppingCartDao,
                                    ExternalTaxApiService externalTaxApiService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
        this.productDao = productDao;
        this.shoppingCartDao = shoppingCartDao;
        this.externalTaxApiService = externalTaxApiService;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody LoginDto loginDto) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication, false);

            User user = userDao.getUserByUsername(loginDto.getUsername());
            return new LoginResponseDto(jwt, user);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterUserDto newUser) {
        try {
            User user = userDao.createUser(
                    new User(newUser.getUsername(), newUser.getPassword(), newUser.getRole(), newUser.getName(),
                            newUser.getAddress(), newUser.getCity(), newUser.getStateCode(), newUser.getZIP())
            );
            return user;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        try {
            return productDao.getAllProducts();
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @GetMapping("/products/search")
    public List<Product> searchProducts(@RequestParam String sku, @RequestParam String name) {
        try {
            return productDao.searchProducts(sku, name);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @GetMapping("/products/{id}")
    public Product getProductById(@PathVariable Long id) {
        try {
            return productDao.getProductById(id);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @GetMapping("/cart")
    public ShoppingCartDto getCart(@RequestParam String username) {
        try {
            return shoppingCartDao.getShoppingCart(username);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @PostMapping("/cart/items")
    public void addToCart(@RequestParam String username, @RequestParam Long productId, @RequestParam int quantity) {
        try {
            shoppingCartDao.addToCart(username, productId, quantity);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @DeleteMapping("/cart/items/{itemId}")
    public void removeFromCart(@PathVariable Long itemId) {
        try {
            shoppingCartDao.removeFromCart(itemId);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @DeleteMapping("/cart")
    public void clearCart(@RequestParam String username) {
        try {
            shoppingCartDao.clearCart(username);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "DAO error - " + e.getMessage());
        }
    }

    @GetMapping("/calculate-tax")
    public double calculateTax(@RequestParam String stateCode) {
        try {
            return externalTaxApiService.getTaxRate(stateCode);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error calculating tax - " + e.getMessage());
        }
    }
}

/*
 */
