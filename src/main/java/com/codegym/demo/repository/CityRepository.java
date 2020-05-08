package com.codegym.demo.repository;

import com.codegym.demo.model.Cities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<Cities, Long> {
}
