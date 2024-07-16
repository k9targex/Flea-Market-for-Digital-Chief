package market.fleaMarket.service;

import com.fleamarket.dao.ProductRepository;
import com.fleamarket.dao.SellerRepository;
import com.fleamarket.exception.ProductNotFoundException;
import com.fleamarket.exception.SellerNotFoundException;
import com.fleamarket.model.entity.Product;
import com.fleamarket.model.entity.Seller;
import com.fleamarket.service.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class SellerServiceTest {
  private static final String EXISTING_SELLER_NAME = "existingSeller";
  private static final String NON_EXISTING_SELLER_NAME = "nonExistingSeller";
  private static final String NEW_SELLER_NAME = "newSeller";
  private static final String EXISTING_PRODUCT_NAME = "existingProduct";
  private static final String NON_EXISTING_PRODUCT_NAME = "nonExistingProduct";
  private static final String NEW_PRODUCT_NAME = "newProduct";
  @Mock private SellerRepository sellerRepository;
  @Mock private ProductRepository productRepository;
  @InjectMocks private SellerService sellerService;
    private Seller existingSeller;
    private Product existingProduct;
    private List<Product> products;

    @BeforeEach
     void setup() {
        MockitoAnnotations.openMocks(this);
        setupSellersAndProducts();
    }

    private void setupSellersAndProducts() {
        existingSeller = new Seller();
        existingSeller.setSellerName(EXISTING_SELLER_NAME);

        existingProduct = new Product();
        existingProduct.setProductName(EXISTING_PRODUCT_NAME);
        existingProduct.setSeller(existingSeller);

        products = new ArrayList<>();
        products.add(existingProduct);

        existingSeller.setProducts(products);
    }

  @Test
   void testGetAllSellers() {
    List<Seller> sellers = new ArrayList<>();
    sellers.add(existingSeller);
    when(sellerRepository.findAll()).thenReturn(sellers);

    List<Seller> result = sellerService.getAllSellers();

    assertEquals(1, result.size());
    verify(sellerRepository, times(1)).findAll();
  }

  @Test
   void testDeleteSeller_Success() {
    when(sellerRepository.findSellerBySellerName(EXISTING_SELLER_NAME))
        .thenReturn(Optional.of(existingSeller));

    assertDoesNotThrow(() -> sellerService.deleteSeller(EXISTING_SELLER_NAME));

    verify(sellerRepository, times(1)).delete(any());
  }

  @Test
   void testDeleteSeller_NotFound() {
    when(sellerRepository.findSellerBySellerName(NON_EXISTING_SELLER_NAME))
        .thenReturn(Optional.empty());

    assertThrows(
        SellerNotFoundException.class, () -> sellerService.deleteSeller(NON_EXISTING_SELLER_NAME));

    verify(sellerRepository, times(0)).delete(any());
  }

  @Test
   void testCreateSeller_Success() {
    when(sellerRepository.existsSellerBySellerName(NEW_SELLER_NAME)).thenReturn(false);

    assertDoesNotThrow(() -> sellerService.createSeller(NEW_SELLER_NAME));

    verify(sellerRepository, times(1)).save(any());
  }

  @Test
   void testCreateSeller_Conflict() {
    when(sellerRepository.existsSellerBySellerName(EXISTING_SELLER_NAME)).thenReturn(true);

    assertThrows(
        ResponseStatusException.class, () -> sellerService.createSeller(EXISTING_SELLER_NAME));

    verify(sellerRepository, times(0)).save(any());
  }
    @Test
     void testCreateSellerThrowsExceptionWhenUsernameIsEmpty() {
        String emptyUsername = "  ";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sellerService.createSeller(emptyUsername);
        });

        assertEquals("Seller parameter cannot be empty", exception.getMessage());
    }
    @Test
    void testGetAllProducts_SellerExists() {
        when(sellerRepository.findSellerBySellerName(EXISTING_SELLER_NAME)).thenReturn(Optional.of(existingSeller));
        List<Product> products = sellerService.getAllProducts(EXISTING_SELLER_NAME);
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals(EXISTING_PRODUCT_NAME, products.get(0).getProductName());
    }

    @Test
    void testGetAllProducts_SellerNotFound() {
        assertThrows(SellerNotFoundException.class, () -> {
            sellerService.getAllProducts(NON_EXISTING_SELLER_NAME);
        });
    }

    @Test
   void testChangeSellerName_Success() {
    when(sellerRepository.findSellerBySellerName(EXISTING_SELLER_NAME))
        .thenReturn(Optional.of(existingSeller));
    when(sellerRepository.existsSellerBySellerName(NEW_SELLER_NAME)).thenReturn(false);

    assertDoesNotThrow(() -> sellerService.changeSellerName(EXISTING_SELLER_NAME, NEW_SELLER_NAME));

    assertEquals(NEW_SELLER_NAME, existingSeller.getSellerName());
    verify(sellerRepository, times(1)).save(any());
  }

  @Test
   void testChangeSellerName_NotFound() {
    when(sellerRepository.findSellerBySellerName(NON_EXISTING_SELLER_NAME))
        .thenReturn(Optional.empty());

    assertThrows(
        SellerNotFoundException.class,
        () -> sellerService.changeSellerName(NON_EXISTING_SELLER_NAME, NEW_SELLER_NAME));

    verify(sellerRepository, times(0)).save(any());
  }

  @Test
   void testChangeSellerName_Conflict() {
    Seller existingSeller = new Seller();
    Seller existingSeller2 = new Seller();
    when(sellerRepository.findSellerBySellerName(EXISTING_SELLER_NAME))
        .thenReturn(Optional.of(existingSeller2));
    when(sellerRepository.existsSellerBySellerName(EXISTING_SELLER_NAME)).thenReturn(true);

    assertThrows(
        IllegalArgumentException.class,
        () -> sellerService.changeSellerName(EXISTING_SELLER_NAME, EXISTING_SELLER_NAME));
  }


    @Test
    void testAddProductToSeller_Success() {

        existingSeller.setProducts(products);

        when(sellerRepository.findSellerBySellerName(EXISTING_SELLER_NAME)).thenReturn(Optional.of(existingSeller));
        when(productRepository.save(any())).thenReturn(existingProduct);

        sellerService.addProductToSeller(NON_EXISTING_PRODUCT_NAME, EXISTING_SELLER_NAME);

        assertEquals(2, existingSeller.getProducts().size());
        assertEquals(NON_EXISTING_PRODUCT_NAME, existingSeller.getProducts().get(1).getProductName());
        verify(productRepository, times(1)).save(any(Product.class));
        verify(sellerRepository, times(1)).save(existingSeller);
    }

    @Test
    void testAddProductToSeller_SellerNotFound() {
        when(sellerRepository.findSellerBySellerName(NON_EXISTING_SELLER_NAME)).thenReturn(Optional.empty());

        SellerNotFoundException exception = assertThrows(SellerNotFoundException.class, () -> {
            sellerService.addProductToSeller(NON_EXISTING_PRODUCT_NAME, NON_EXISTING_SELLER_NAME);
        });

        assertEquals("Seller \"nonExistingSeller\" doesn't exist", exception.getMessage());
    }

    @Test
    void testAddProductToSeller_ProductParameterEmpty() {
        when(sellerRepository.findSellerBySellerName(EXISTING_SELLER_NAME)).thenReturn(Optional.of(existingSeller));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sellerService.addProductToSeller("", EXISTING_SELLER_NAME);
        });

        assertEquals("Product parameter cannot be empty", exception.getMessage());
    }

    @Test
    void testAddProductToSeller_ProductAlreadyAdded() {
        when(sellerRepository.findSellerBySellerName(EXISTING_SELLER_NAME)).thenReturn(Optional.of(existingSeller));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            sellerService.addProductToSeller(EXISTING_PRODUCT_NAME, EXISTING_SELLER_NAME);
        });

        assertEquals("Product existingProduct was already added yet(((((", exception.getMessage());
    }


    @Test
    void testDeleteProduct_Success() {
        existingSeller.setProducts(products);

        when(sellerRepository.findSellerBySellerName(EXISTING_SELLER_NAME)).thenReturn(Optional.of(existingSeller));
        sellerService.deleteProduct(EXISTING_PRODUCT_NAME, EXISTING_SELLER_NAME);


        assertTrue(existingSeller.getProducts().isEmpty());
        verify(productRepository, times(1)).delete(existingProduct);
        verify(sellerRepository, times(1)).save(existingSeller);
    }

    @Test
    void testDeleteProduct_SellerNotFound() {
        when(sellerRepository.findSellerBySellerName(NON_EXISTING_SELLER_NAME)).thenReturn(Optional.empty());

        SellerNotFoundException exception = assertThrows(SellerNotFoundException.class, () -> {
            sellerService.deleteProduct(EXISTING_PRODUCT_NAME,NON_EXISTING_SELLER_NAME);
        });
        assertEquals("Seller \"nonExistingSeller\" doesn't exist", exception.getMessage());
    }

    @Test
    void testDeleteProduct_ProductNotFound() {
        when(sellerRepository.findSellerBySellerName(EXISTING_SELLER_NAME)).thenReturn(Optional.of(existingSeller));
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            sellerService.deleteProduct(NON_EXISTING_PRODUCT_NAME, EXISTING_SELLER_NAME);
        });

        assertEquals("Product nonExistingProduct doesn't exist(((((", exception.getMessage());
    }

    @Test
     void testChangeProduct_Success() {
        when(sellerRepository.findSellerBySellerName(EXISTING_SELLER_NAME)).thenReturn(Optional.of(existingSeller));
        assertDoesNotThrow(() -> sellerService.changeProduct(EXISTING_PRODUCT_NAME, NEW_PRODUCT_NAME, EXISTING_SELLER_NAME));
        assertEquals(NEW_PRODUCT_NAME, existingProduct.getProductName());
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
     void testChangeProduct_ProductNotFound() {
        when(sellerRepository.findSellerBySellerName(EXISTING_SELLER_NAME)).thenReturn(Optional.of(existingSeller));
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> sellerService.changeProduct(NON_EXISTING_PRODUCT_NAME, NEW_PRODUCT_NAME, EXISTING_SELLER_NAME));
        assertEquals(String.format("Product %s doesn't exist(((((", NON_EXISTING_PRODUCT_NAME), exception.getMessage());
    }

    @Test
     void testChangeProduct_DuplicateProductName() {
        Product newProduct = new Product();
        newProduct.setProductName(NEW_PRODUCT_NAME);
        newProduct.setSeller(existingSeller);
        products.add(newProduct);
        existingSeller.setProducts(products);
        when(sellerRepository.findSellerBySellerName(EXISTING_SELLER_NAME)).thenReturn(Optional.of(existingSeller));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> sellerService.changeProduct(EXISTING_PRODUCT_NAME, NEW_PRODUCT_NAME, EXISTING_SELLER_NAME));
        assertEquals(String.format("Product with name %s already exist(((((", NEW_PRODUCT_NAME), exception.getMessage());
    }

    @Test
     void testChangeProduct_SellerNotFound() {
        when(sellerRepository.findSellerBySellerName(NON_EXISTING_SELLER_NAME)).thenReturn(Optional.empty());

        SellerNotFoundException exception = assertThrows(SellerNotFoundException.class,
                () -> sellerService.changeProduct(EXISTING_PRODUCT_NAME, NEW_PRODUCT_NAME, NON_EXISTING_SELLER_NAME));
        assertEquals(String.format("Seller \"%s\" doesn't exist", NON_EXISTING_SELLER_NAME), exception.getMessage());
    }

}