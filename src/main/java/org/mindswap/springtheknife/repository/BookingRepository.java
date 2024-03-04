package org.mindswap.springtheknife.repository;
import jakarta.transaction.Transactional;
import org.mindswap.springtheknife.model.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;




@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAll(Pageable pageable);
    Optional<Booking> findByBookingTime(LocalDateTime localDateTime);
    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE booking AUTO_INCREMENT = 1", nativeQuery = true)
    default void resetId() {
    }
}
