package market.fleaMarket.service;

import com.fleamarket.dao.ProductRepository;
import com.fleamarket.exception.ProductNotFoundException;
import com.fleamarket.model.entity.Product;
import com.fleamarket.model.entity.Seller;
import com.fleamarket.service.ProductService;
import com.fleamarket.service.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
  private static final String PRODUCT_NOT_FOUND_MESSAGE = "Product with ID = \"%d\" doesn't exist";
  private static long nonexistingProductId = 2L;
  private static long existingProductId = 1L;

  @Mock private ProductRepository productRepository;

  @InjectMocks private ProductService productService;

  private Product existingProduct;
  private Seller existingSeller;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    setupProductAndSeller();
  }

  private void setupProductAndSeller() {
    existingSeller = new Seller();
    existingSeller.setSellerName("existingSeller");

    existingProduct = new Product();
    existingProduct.setId(1L);
    existingProduct.setProductName("existingProduct");
    existingProduct.setSeller(existingSeller);
  }

  @Test
  void testGetSellerByProductId_Success() {
    when(productRepository.findProductById(existingProductId))
        .thenReturn(Optional.of(existingProduct));
    Seller seller = productService.getSellerByProductId(existingProductId);
    assertEquals(existingSeller, seller);
  }

  @Test
  void testGetSellerByProductId_ProductNotFound() {
    when(productRepository.findProductById(nonexistingProductId)).thenReturn(Optional.empty());

    ProductNotFoundException exception =
        assertThrows(
            ProductNotFoundException.class,
            () -> productService.getSellerByProductId(nonexistingProductId));
    assertEquals(
        String.format(PRODUCT_NOT_FOUND_MESSAGE, nonexistingProductId), exception.getMessage());
  }
}
