package org.mindswap.springtheknife.repository;

import jakarta.transaction.Transactional;
import org.mindswap.springtheknife.model.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Page<City> findAll(Pageable pageable);

    Optional<City> findByName(String cityName);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE city AUTO_INCREMENT = 1", nativeQuery = true)
    default void resetId() {
    }
}
