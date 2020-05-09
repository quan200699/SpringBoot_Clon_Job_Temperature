package com.codegym.demo.repository;

import com.codegym.demo.model.Cities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<Cities, Long> {
    Optional<Cities> findByName(String name);
}
