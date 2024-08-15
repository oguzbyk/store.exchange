package com.inghub.stock.exchange.config;

import com.inghub.stock.exchange.entity.AppUser;
import com.inghub.stock.exchange.repository.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public CustomUserDetailService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser == null || !appUser.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        return User.builder()
                .username(appUser.get().getUsername())
                .password(appUser.get().getPassword())
                .roles(appUser.get().getRole())
                .build();
    }
}
