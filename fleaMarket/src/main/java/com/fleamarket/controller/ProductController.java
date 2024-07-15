package com.fleamarket.controller;

import com.fleamarket.dao.ProductRepository;
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
    public ResponseEntity<Object> getProductSeller(@RequestParam String product) {
        return ResponseEntity.ok().body(productService.getSellerByProduct(product));
    }
    @PostMapping("/addProduct")
    public ResponseEntity<String> addProduct(@RequestParam String product,String seller) {
        productService.addProductToSeller(product,seller);
        return ResponseEntity.ok("Product was successfully added to seller");
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<String> deleteProduct(@RequestParam String product,String seller) {
        productService.deleteProduct(product,seller);
        return ResponseEntity.ok("Product was successfully deleted");
    }
    @PatchMapping("/updateProduct")
    public ResponseEntity<String> updateProduct(@RequestParam String oldProduct,String  newProduct) {
        productService.changeProduct(oldProduct,newProduct);
        return ResponseEntity.ok("Product was successfully updated");
    }









}
