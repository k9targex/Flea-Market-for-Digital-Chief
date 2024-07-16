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

  /**
   * Retrieves all sellers from the database.
   *
   * @return List of all sellers
   */
  public List<Seller> getAllSellers() {
    return sellerRepository.findAll();
  }

  /**
   * Deletes a seller by their username.
   *
   * @param username Username of the seller to delete
   * @throws SellerNotFoundException if the seller with the given username is not found
   */
  public void deleteSeller(String username) {
    Seller sellerOptional =
            sellerRepository
                    .findSellerBySellerName(username)
                    .orElseThrow(
                            () ->
                                    new SellerNotFoundException(String.format(SELLER_NOT_FOUND_MESSAGE, username)));
    sellerRepository.delete(sellerOptional);
  }

  /**
   * Creates a new seller with the given username.
   *
   * @param username Username of the new seller to create
   * @throws IllegalArgumentException if the username is empty
   * @throws ResponseStatusException with HTTP status Conflict if the username is already taken
   */
  public void createSeller(String username) {
    if (username.trim().isEmpty()) {
      throw new IllegalArgumentException("Seller parameter cannot be empty");
    }
    if (sellerRepository.existsSellerBySellerName(username).booleanValue()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Name \"%s\" already taken (((", username));
    }
    Seller seller = new Seller();
    seller.setSellerName(username);
    sellerRepository.save(seller);
  }

  /**
   * Changes the username of an existing seller.
   *
   * @param oldUsername Current username of the seller
   * @param newUsername New username to change to
   * @throws SellerNotFoundException if the seller with the old username is not found
   * @throws IllegalArgumentException if the new username is already taken
   */
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
    sellerRepository.save(sellerOptional);
  }

  /**
   * Retrieves all products associated with a seller.
   *
   * @param seller Username of the seller to retrieve products for
   * @return List of products associated with the seller
   * @throws SellerNotFoundException if the seller with the given username is not found
   */
  public List<Product> getAllProducts(String seller) {
    Seller sellerOptional =
            sellerRepository
                    .findSellerBySellerName(seller)
                    .orElseThrow(
                            () -> new SellerNotFoundException(String.format(SELLER_NOT_FOUND_MESSAGE, seller)));
    return sellerOptional.getProducts();
  }

  /**
   * Adds a new product to a seller.
   *
   * @param product Name of the product to add
   * @param seller Username of the seller to add the product to
   * @throws IllegalArgumentException if the product name is empty or if the product was already added
   * @throws SellerNotFoundException if the seller with the given username is not found
   */
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

  /**
   * Deletes a product from a seller's inventory.
   *
   * @param productDelete Name of the product to delete
   * @param seller Username of the seller from whose inventory to delete the product
   * @throws ProductNotFoundException if the product with the given name does not exist
   * @throws SellerNotFoundException if the seller with the given username is not found
   */
  public void deleteProduct(String productDelete, String seller) {
    Seller sellerOptional =
            sellerRepository
                    .findSellerBySellerName(seller)
                    .orElseThrow(
                            () -> new SellerNotFoundException(String.format(SELLER_NOT_FOUND_MESSAGE, seller)));
    Product productToDelete = sellerOptional.getProduct(productDelete);
    if (productToDelete == null) {
      throw new ProductNotFoundException(
              String.format("Product %s doesn't exist(((((", productDelete));
    }
    sellerOptional
            .getProducts()
            .removeIf(product -> product.getProductName().equals(productDelete));
    productRepository.delete(productToDelete);
    sellerRepository.save(sellerOptional);
  }

  /**
   * Changes the name of a product associated with a seller.
   *
   * @param oldProductName Current name of the product
   * @param newProductName New name to change to
   * @param seller Username of the seller associated with the product
   * @throws ProductNotFoundException if the product with the old name does not exist
   * @throws IllegalArgumentException if the new product name already exists for the seller
   * @throws SellerNotFoundException if the seller with the given username is not found
   */
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
    } else {
      throw new IllegalArgumentException(
              String.format("Product with name %s already exist(((((", newProductName));
    }
  }
}
