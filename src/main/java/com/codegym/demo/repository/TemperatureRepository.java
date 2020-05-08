package com.codegym.demo.repository;

import com.codegym.demo.model.Temperatures;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemperatureRepository extends JpaRepository<Temperatures, Long> {
}
