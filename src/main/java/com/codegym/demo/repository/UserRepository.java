package com.codegym.demo.repository;

import com.codegym.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Iterable<User> findAllByEnableIsTrue();

    Iterable<User> findAllByEnableIsFalse();
}
