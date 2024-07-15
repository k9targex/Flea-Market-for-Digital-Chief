package com.fleamarket.controller;

import com.fleamarket.dao.ProductRepository;
import com.fleamarket.model.entity.Seller;
import com.fleamarket.service.ProductService;
import com.fleamarket.service.SellerService;
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

    @GetMapping("/getProcuctSeller")
    public ResponseEntity<Seller> getProductSeller(@RequestParam Long productId) {
        return ResponseEntity.ok().body(productService.getSellerByProductId(productId));
    }











}
