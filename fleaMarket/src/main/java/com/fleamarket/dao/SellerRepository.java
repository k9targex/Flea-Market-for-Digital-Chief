package com.fleamarket.dao;

import com.fleamarket.model.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findSellerByUsername(String username);
    Boolean existsSellerByUsername(String username);
}
