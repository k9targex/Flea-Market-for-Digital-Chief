package com.fleamarket.controller;

import com.fleamarket.model.entity.Seller;
import com.fleamarket.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
  private ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  /**
   * Endpoint to retrieve the seller of a specific product by its ID.
   *
   * @param productId ID of the product to retrieve the seller for
   */
  @GetMapping("/getProcuctSeller")
  public ResponseEntity<Seller> getProductSeller(@RequestParam Long productId) {
    return ResponseEntity.ok().body(productService.getSellerByProductId(productId));
  }
}
