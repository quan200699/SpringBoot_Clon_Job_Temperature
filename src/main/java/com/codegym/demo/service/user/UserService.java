package com.codegym.demo.service.user;

import com.codegym.demo.model.User;
import com.codegym.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Iterable<User> findAllByStatusIsTrue() {
        return userRepository.findAllByStatusIsTrue();
    }

    @Override
    public Iterable<User> findAllByStatusIsFalse() {
        return userRepository.findAllByStatusIsFalse();
    }
}
