package com.invoice.api.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.invoice.api.entity.CartItem;

@Repository
public interface RepoCartItem extends JpaRepository<CartItem, Integer> {
    @Query(value = "SELECT * FROM cart_item WHERE user_id = :user_id AND status = 1", nativeQuery = true)
    List<CartItem> findByUserId(@Param("user_id") Integer user_id);

    @Query(value = "SELECT * FROM cart_item WHERE user_id = :user_id AND gtin = :gtin AND status = 1", nativeQuery = true)
    Optional<CartItem> findByUserIdAndGtin(@Param("user_id") Integer user_id, @Param("gtin") String gtin);

    @Modifying
    @Query(value = "DELETE FROM cart_item WHERE user_id = :user_id", nativeQuery = true)
    void deleteByUserId(@Param("user_id") Integer user_id);
}