package com.inghub.stock.exchange.service;

import com.inghub.stock.exchange.entity.AppUser;
import com.inghub.stock.exchange.repository.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public Optional<AppUser> getUserById(Long id) {
        return appUserRepository.findById(id);
    }

    public AppUser createUser(AppUser appUser) {
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        return appUserRepository.save(appUser);
    }

    public AppUser updateUser(Long id, AppUser appUserDetails) {
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        appUser.setUsername(appUserDetails.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUserDetails.getPassword()));
        appUser.setRole(appUserDetails.getRole());
        return appUserRepository.save(appUser);
    }

    public void deleteUser(Long id) {
        if (!appUserRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        appUserRepository.deleteById(id);
    }
}
