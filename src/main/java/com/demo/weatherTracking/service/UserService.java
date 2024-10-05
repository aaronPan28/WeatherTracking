package com.demo.weatherTracking.service;

import com.demo.weatherTracking.entity.User;
import com.demo.weatherTracking.exceptions.EmailAlreadyExistsException;
import com.demo.weatherTracking.exceptions.EntityNotFoundException;
import com.demo.weatherTracking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(String name, String email) {
        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("Email is already in use.");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }
}
