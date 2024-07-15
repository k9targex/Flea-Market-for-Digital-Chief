package com.fleamarket.service;

import com.fleamarket.dao.ProductRepository;
import com.fleamarket.dao.SellerRepository;
import com.fleamarket.exception.ProductNotFoundException;
import com.fleamarket.exception.SellerNotFoundException;
import com.fleamarket.model.entity.Product;
import com.fleamarket.model.entity.Seller;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class SellerService {
  private static final String SELLER_NOT_FOUND_MESSAGE = "Seller \"%s\" doesn't exist";
  private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product with ID = \"%s\" doesn't exist";
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
    if (sellerRepository.existsSellerBySellerName(username).booleanValue()) {
      throw new SellerNotFoundException(String.format("Name \"%s\" already taken (((", username));
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
      throw new SellerNotFoundException(
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

  public String addProductToSeller(String product, String seller) {
    Seller sellerOptional =
        sellerRepository
            .findSellerBySellerName(seller)
            .orElseThrow(
                () -> new SellerNotFoundException(String.format(SELLER_NOT_FOUND_MESSAGE, seller)));

    if (sellerOptional.getProduct(product) != null) {
      return String.format("Product %s was already added yet(((((", product);
    }
    Product newProduct = new Product();
    newProduct.setProductName(product);
    sellerOptional.addProduct(newProduct);
    sellerRepository.save(sellerOptional);
    return String.format("Product %s was successfully added ", product);
  }

  public void deleteProduct(String productDelete, String seller) {
    Seller sellerOptional =
        sellerRepository
            .findSellerBySellerName(seller)
            .orElseThrow(
                () -> new SellerNotFoundException(String.format(SELLER_NOT_FOUND_MESSAGE, seller)));
    if (sellerOptional.getProduct(productDelete) == null) {
      throw new ProductNotFoundException(
          String.format("Product %s doesn't exist(((((", productDelete));
    }
    sellerOptional
        .getProducts()
        .removeIf(product -> product.getProductName().equals(productDelete));
    sellerRepository.save(sellerOptional);
  }

  public void changeProduct(String oldProductName, String newProductName, String seller) {
    Seller sellerOptional =
        sellerRepository
            .findSellerBySellerName(seller)
            .orElseThrow(
                () -> new SellerNotFoundException(String.format(SELLER_NOT_FOUND_MESSAGE, seller)));

    Product product = sellerOptional.getProduct(oldProductName);
    if (product == null) {
      throw new ProductNotFoundException(
          String.format("Product %s doesn't exist(((((", oldProductName));
    }
    product.setProductName(newProductName);
    productRepository.save(product);
  }
}
