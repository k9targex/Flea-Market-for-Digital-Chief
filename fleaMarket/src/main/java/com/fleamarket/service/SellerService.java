package com.fleamarket.service;

import com.fleamarket.dao.ProductRepository;
import com.fleamarket.dao.SellerRepository;
import com.fleamarket.exception.ProductNotFoundException;
import com.fleamarket.exception.ProductTakenException;
import com.fleamarket.exception.SellerNotFoundException;
import com.fleamarket.exception.SellerTakenException;
import com.fleamarket.model.entity.Product;
import com.fleamarket.model.entity.Seller;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SellerService {

  private static final String SELLER_NOT_FOUND_MESSAGE = "Seller \"%s\" doesn't exist";
  private static final String PRODUCT_EMPTY_MESSAGE = "Product parameter cannot be empty";
  private static final String SELLER_EMPTY_MESSAGE = "Seller parameter cannot be empty";
  private static final String SELLER_TAKEN_MESSAGE = "Name \"%s\" is already taken (((";
  private static final String PRODUCT_ALREADY_EXIST_MESSAGE = "Product %s already exists(((((";
  private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product %s doesn't exist(((((";

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
    Seller sellerOptional = findSeller(username);
    sellerRepository.delete(sellerOptional);
  }

  /**
   * Creates a new seller with the given username.
   *
   * @param username Username of the new seller to create
   * @throws IllegalArgumentException if the username is empty
   * @throws SellerTakenException with HTTP status Conflict if the username is already taken
   */
  public void createSeller(String username) {
    if (username.trim().isEmpty()) {
      throw new IllegalArgumentException(SELLER_EMPTY_MESSAGE);
    }
    if (sellerRepository.existsSellerBySellerName(username).booleanValue()) {
      throw new SellerTakenException(String.format(SELLER_TAKEN_MESSAGE, username));
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
   * @throws IllegalArgumentException if the new name is empty
   * @throws SellerTakenException if the new username is already taken
   */
  public void changeSellerName(String oldUsername, String newUsername) {
    Seller sellerOptional = findSeller(oldUsername);
    if (newUsername.trim().isEmpty()) {
      throw new IllegalArgumentException(SELLER_EMPTY_MESSAGE);
    }
    if (sellerRepository.existsSellerBySellerName(newUsername).booleanValue()) {
      throw new SellerTakenException(String.format(SELLER_TAKEN_MESSAGE, newUsername));
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
    Seller sellerOptional = findSeller(seller);
    return sellerOptional.getProducts();
  }

  /**
   * Adds a new product to a seller.
   *
   * @param product Name of the product to add
   * @param seller Username of the seller to add the product to
   * @throws IllegalArgumentException if the product name is empty
   * @throws ProductTakenException if the product was already added
   * @throws SellerNotFoundException if the seller with the given username is not found
   */
  public void addProductToSeller(String product, String seller) {
    Seller sellerOptional = findSeller(seller);
    if (product.trim().isEmpty()) {
      throw new IllegalArgumentException(PRODUCT_EMPTY_MESSAGE);
    }
    if (sellerOptional.getProduct(product) != null) {
      throw new ProductTakenException(String.format(PRODUCT_ALREADY_EXIST_MESSAGE, product));
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
    Seller sellerOptional = findSeller(seller);
    Product productToDelete = sellerOptional.getProduct(productDelete);
    if (productToDelete == null) {
      throw new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_MESSAGE, productDelete));
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
   * @throws ProductTakenException if the new product name already exists for the seller
   * @throws SellerNotFoundException if the seller with the given username is not found
   * @throws IllegalArgumentException if the product name is empty
   */
  public void changeProduct(String oldProductName, String newProductName, String seller) {
    Seller sellerOptional = findSeller(seller);
    if (newProductName.trim().isEmpty()) {
      throw new IllegalArgumentException(PRODUCT_EMPTY_MESSAGE);
    }

    Product oldProduct = sellerOptional.getProduct(oldProductName);
    if (oldProduct == null) {
      throw new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_MESSAGE, oldProductName));
    }
    Product newProduct = sellerOptional.getProduct(newProductName);
    if (newProduct == null) {
      oldProduct.setProductName(newProductName);
      productRepository.save(oldProduct);
    } else {
      throw new ProductTakenException(String.format(PRODUCT_ALREADY_EXIST_MESSAGE, newProductName));
    }
  }

  /**
   * Util function
   *
   * @param sellerName Username of the seller
   * @throws SellerNotFoundException if the seller with the given username is not found
   */
  private Seller findSeller(String sellerName) {
    return sellerRepository
        .findSellerBySellerName(sellerName)
        .orElseThrow(
            () -> new SellerNotFoundException(String.format(SELLER_NOT_FOUND_MESSAGE, sellerName)));
  }
}
