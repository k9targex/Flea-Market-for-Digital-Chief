package com.fleamarket.controller;

import com.fleamarket.model.entity.Product;
import com.fleamarket.model.entity.Seller;
import com.fleamarket.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sellers")
public class SellerController {
  private SellerService sellerService;

  @Autowired
  public SellerController(SellerService sellerService) {
    this.sellerService = sellerService;
  }

  /**
   * Endpoint to create a new seller.
   *
   * @param seller Name of the seller to create
   */
  @PostMapping("/crteateSeller")
  ResponseEntity<String> createSeller(
      @RequestParam String seller) {
    sellerService.createSeller(seller);
    return ResponseEntity.ok("Seller was successfully created");
  }

  /** Endpoint to retrieve all sellers. */
  @GetMapping("/getAllSellers")
  public ResponseEntity<List<Seller>> getAllSellers() {
    return new ResponseEntity<>(sellerService.getAllSellers(), HttpStatus.OK);
  }

  /**
   * Endpoint to retrieve all products of a specific seller.
   *
   * @param seller Name of the seller to retrieve products for
   */
  @GetMapping("/getAllSellerPtroducts")
  public ResponseEntity<List<Product>> getAllSellerProducts(@RequestParam String seller) {
    return new ResponseEntity<>(sellerService.getAllProducts(seller), HttpStatus.OK);
  }

  /**
   * Endpoint to delete a seller.
   *
   * @param seller Name of the seller to delete
   */
  @DeleteMapping("/deleteSeller")
  public ResponseEntity<String> deleteSeller(@RequestParam String seller) {
    sellerService.deleteSeller(seller);
    return ResponseEntity.ok("Seller was successfully deleted");
  }

  /**
   * Endpoint to update the name of a seller.
   *
   * @param oldUsername Current name of the seller
   * @param newUsername New name to update for the seller
   */
  @PatchMapping("/updateSellerName")
  public ResponseEntity<String> updateSellerName(
      @RequestParam String oldUsername,@RequestParam String newUsername) {
    sellerService.changeSellerName(oldUsername, newUsername);
    return ResponseEntity.ok("Seller was successfully updated");
  }

  /**
   * Endpoint to add a product to a seller.
   *
   * @param product Name of the product to add
   * @param seller Name of the seller to add the product to
   */
  @PostMapping("/addProduct")
  public ResponseEntity<String> addProduct(@RequestParam String product,@RequestParam String seller) {
    sellerService.addProductToSeller(product, seller);
    return ResponseEntity.ok("Product was successfully added to seller");
  }

  /**
   * Endpoint to delete a product from a seller.
   *
   * @param product Name of the product to delete
   * @param seller Name of the seller to delete the product from
   */
  @DeleteMapping("/deleteProduct")
  public ResponseEntity<String> deleteProduct(@RequestParam String product,@RequestParam String seller) {
    sellerService.deleteProduct(product, seller);
    return ResponseEntity.ok("Product was successfully deleted");
  }

  /**
   * Endpoint to update the name of a product for a seller.
   *
   * @param oldProductName Current name of the product
   * @param newProductName New name to update for the product
   * @param seller Name of the seller that owns the product
   */
  @PatchMapping("/updateProduct")
  public ResponseEntity<String> updateProduct(
      @RequestParam String oldProductName,@RequestParam String newProductName,@RequestParam String seller) {
    sellerService.changeProduct(oldProductName, newProductName, seller);
    return ResponseEntity.ok("Product was successfully updated");
  }
}
