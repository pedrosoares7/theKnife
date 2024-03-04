package org.mindswap.springtheknife.repository;

import org.mindswap.springtheknife.model.RestaurantImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantImageRepository extends JpaRepository<RestaurantImage, Long> {
}
