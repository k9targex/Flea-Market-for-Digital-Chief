package com.fleamarket.dao;

import com.fleamarket.model.entity.Seller;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
  Optional<Seller> findSellerBySellerName(String username);

  Boolean existsSellerBySellerName(String username);
}
