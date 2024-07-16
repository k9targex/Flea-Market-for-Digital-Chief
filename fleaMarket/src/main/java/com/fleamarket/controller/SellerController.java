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


    @PostMapping("/crteateSeller")
    ResponseEntity<String> createSeller(@RequestParam(name = "seller", required = true)String seller) {
        sellerService.createSeller(seller);
        return ResponseEntity.ok("Seller was successfully created");
    }
    @GetMapping("/getAllSellers")
    public ResponseEntity<List<Seller>> getAllSellers() {
        return new ResponseEntity<>(sellerService.getAllSellers(), HttpStatus.OK);
    }
    @GetMapping("/getAllSellerPtroducts")
    public ResponseEntity<List<Product>> getAllSellerProducts(@RequestParam String seller) {
        return new ResponseEntity<>(sellerService.getAllProducts(seller), HttpStatus.OK);
    }

    @DeleteMapping("/deleteSeller")
    public ResponseEntity<String> deleteSeller(@RequestParam String seller) {
        sellerService.deleteSeller(seller);
        return ResponseEntity.ok("Seller was successfully deleted");
    }

    @PatchMapping("/updateSellerName")
    public ResponseEntity<String> updateSellerName(@RequestParam String oldUsername,String  newUsername) {
        sellerService.changeSellerName(oldUsername,newUsername);
        return ResponseEntity.ok("Seller was successfully updated");
    }
    @PostMapping("/addProduct")
    public ResponseEntity<String> addProduct(@RequestParam String product,String seller) {
        sellerService.addProductToSeller(product,seller);
        return ResponseEntity.ok("Product was successfully added to seller");
    }
    @DeleteMapping("/deleteProduct")
    public ResponseEntity<String> deleteProduct(@RequestParam String product,String seller) {
        sellerService.deleteProduct(product,seller);
        return ResponseEntity.ok("Product was successfully deleted");
    }
    @PatchMapping("/updateProduct")
    public ResponseEntity<String> updateProduct(@RequestParam String oldProductName, String newProductName,String seller) {
        sellerService.changeProduct(oldProductName,newProductName,seller);
        return ResponseEntity.ok("Product was successfully updated");
    }



}
