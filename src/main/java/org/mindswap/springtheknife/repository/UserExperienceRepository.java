package org.mindswap.springtheknife.repository;

import jakarta.transaction.Transactional;
import org.mindswap.springtheknife.model.UserExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserExperienceRepository extends JpaRepository<UserExperience, Long> {

    List<UserExperience> findAll();

    Optional<UserExperience> findById(Long id);

    @Modifying
    @Transactional
    @Query(value = "ALTER TABLE userExperience AUTO_INCREMENT = 1", nativeQuery = true)
    default void resetId() {
    }

}
