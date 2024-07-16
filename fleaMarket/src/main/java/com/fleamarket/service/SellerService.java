package com.fleamarket.service;

import com.fleamarket.dao.ProductRepository;
import com.fleamarket.dao.SellerRepository;
import com.fleamarket.exception.ProductNotFoundException;
import com.fleamarket.exception.SellerNotFoundException;
import com.fleamarket.model.entity.Product;
import com.fleamarket.model.entity.Seller;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class SellerService {
  private static final String SELLER_NOT_FOUND_MESSAGE = "Seller \"%s\" doesn't exist";
  private SellerRepository sellerRepository;
  private ProductRepository productRepository;

  @Autowired
  public void setProductRepository(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @Autowired
  public void setSellerRepository(SellerRepository sellerRepository) {
    this.sellerRepository = sellerRepository;
  }

  public List<Seller> getAllSellers() {
    return sellerRepository.findAll();
  }

  public void deleteSeller(String username) {
    Seller sellerOptional =
        sellerRepository
            .findSellerBySellerName(username)
            .orElseThrow(
                () ->
                    new SellerNotFoundException(String.format(SELLER_NOT_FOUND_MESSAGE, username)));
    sellerRepository.delete(sellerOptional);
  }

  public void createSeller(String username) {
    if (username.trim().isEmpty()) {
      throw new IllegalArgumentException("Seller parameter cannot be empty");
    }
    if (sellerRepository.existsSellerBySellerName(username).booleanValue()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT,String.format("Name \" %s \" already taken (((", username));
    }
    Seller seller = new Seller();
    seller.setSellerName(username);
    sellerRepository.save(seller);
  }

  public void changeSellerName(String oldUsername, String newUsername) {
    Seller sellerOptional =
        sellerRepository
            .findSellerBySellerName(oldUsername)
            .orElseThrow(
                () ->
                    new SellerNotFoundException(
                        String.format(SELLER_NOT_FOUND_MESSAGE, oldUsername)));

    if (sellerRepository.existsSellerBySellerName(newUsername).booleanValue()) {
      throw new IllegalArgumentException(
          String.format("Name \"%s\" is already taken (((", newUsername));
    }
    sellerOptional.setSellerName(newUsername);
  }

  public List<Product> getAllProducts(String seller) {
    Seller sellerOptional =
        sellerRepository
            .findSellerBySellerName(seller)
            .orElseThrow(
                () -> new SellerNotFoundException(String.format(SELLER_NOT_FOUND_MESSAGE, seller)));
    return sellerOptional.getProducts();
  }

  public void addProductToSeller(String product, String seller) {
    Seller sellerOptional =
        sellerRepository
            .findSellerBySellerName(seller)
            .orElseThrow(
                () -> new SellerNotFoundException(String.format(SELLER_NOT_FOUND_MESSAGE, seller)));
    if (product.trim().isEmpty()) {
      throw new IllegalArgumentException("Product parameter cannot be empty");
    }
    if (sellerOptional.getProduct(product) != null) {
      throw new IllegalArgumentException(String.format("Product %s was already added yet(((((", product));
    }
    Product newProduct = new Product();
    newProduct.setProductName(product);
    newProduct.setSeller(sellerOptional);
    sellerOptional.addProduct(newProduct);
    productRepository.save(newProduct);
    sellerRepository.save(sellerOptional);
  }

  public void deleteProduct(String productDelete, String seller) {
    Seller sellerOptional =
        sellerRepository
            .findSellerBySellerName(seller)
            .orElseThrow(
                () -> new SellerNotFoundException(String.format(SELLER_NOT_FOUND_MESSAGE, seller)));
    Product productToDelete = sellerOptional.getProduct(productDelete);
    if ( productToDelete == null) {
      throw new ProductNotFoundException(
          String.format("Product %s doesn't exist(((((", productDelete));
    }
    sellerOptional
        .getProducts()
        .removeIf(product -> product.getProductName().equals(productDelete));
    productRepository.delete(productToDelete);
    sellerRepository.save(sellerOptional);
  }

  public void changeProduct(String oldProductName, String newProductName, String seller) {
    Seller sellerOptional =
        sellerRepository
            .findSellerBySellerName(seller)
            .orElseThrow(
                () -> new SellerNotFoundException(String.format(SELLER_NOT_FOUND_MESSAGE, seller)));

    Product oldProduct = sellerOptional.getProduct(oldProductName);
    if (oldProduct == null) {
      throw new ProductNotFoundException(
          String.format("Product %s doesn't exist(((((", oldProductName));
    }
    Product newProduct = sellerOptional.getProduct(newProductName);
    if (newProduct == null) {
      oldProduct.setProductName(newProductName);
      productRepository.save(oldProduct);
    }
    else{
      throw new IllegalArgumentException(
          String.format("Product with name %s already exist(((((", newProductName));
    }
  }
}
