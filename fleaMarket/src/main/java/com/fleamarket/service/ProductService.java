package com.fleamarket.service;

import com.fleamarket.dao.ProductRepository;
import com.fleamarket.exception.ProductNotFoundException;
import com.fleamarket.model.entity.Product;
import com.fleamarket.model.entity.Seller;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ProductService {
    private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product with ID = \"%s\" doesn't exist";
    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    /**
     * Retrieves the seller associated with a product given its ID.
     *
     * @param productId ID of the product to retrieve the seller for
     * @return Seller associated with the product
     * @throws ProductNotFoundException if the product with the given ID is not found
     */
    public Seller getSellerByProductId(Long productId) {
        Product productOptional =
                productRepository
                        .findProductById(productId)
                        .orElseThrow(
                                () ->
                                        new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_MESSAGE, productId)));
        return productOptional.getSeller();
    }





}
