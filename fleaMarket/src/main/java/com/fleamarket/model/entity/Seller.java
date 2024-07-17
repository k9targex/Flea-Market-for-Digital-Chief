package com.fleamarket.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "seller")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Seller {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "seller")
  private String sellerName;

  @OneToMany(
      mappedBy = "seller",
      fetch = FetchType.EAGER,
      cascade = {
        CascadeType.DETACH,
        CascadeType.REFRESH,
        CascadeType.MERGE,
        CascadeType.PERSIST,
        CascadeType.REMOVE
      })
  @JsonIgnore
  private List<Product> products;

  public Product getProduct(String productName) {
    return products.stream()
        .filter(p -> p.getProductName().equals(productName))
        .findFirst()
        .orElse(null);
  }

  public void addProduct(Product product) {
    products.add(product);
  }
}
