package com.eshop.client.service.impl;

import com.eshop.client.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = userRepository.findByUserNameOrEmail(login, login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username/email: " + login));

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return user.getRoles()
                        .stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRole()))
                        .collect(Collectors.toList());
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getUserName();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return user.isActive();
            }

            // You can add custom methods to get additional user information
            public String getEmail() {
                return user.getEmail();
            }
        };
    }
}