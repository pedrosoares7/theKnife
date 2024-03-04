package org.mindswap.springtheknife.repository;

import jakarta.transaction.Transactional;
import org.mindswap.springtheknife.model.RestaurantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface RestaurantTypeRepository extends JpaRepository<RestaurantType, Long> {
    Optional<RestaurantType> findByType(String type);

    Page<RestaurantType> findAll(Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE restaurant_type AUTO_INCREMENT = 1", nativeQuery = true)
    default void resetId() {
    }
}
