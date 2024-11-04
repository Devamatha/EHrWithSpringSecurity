package com.techpixe.ehr.config;

import com.techpixe.ehr.entity.Clients;
import com.techpixe.ehr.repository.ClientsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceEHR implements UserDetailsService {
    @Autowired
    private final ClientsRepository clientsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Clients clients = clientsRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + username));
        List<GrantedAuthority> authorityList = List.of(new SimpleGrantedAuthority(clients.getRole()));
        return new User(clients.getEmail(), clients.getPassword(), authorityList);
    }
}
