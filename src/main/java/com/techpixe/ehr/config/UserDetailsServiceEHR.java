package com.techpixe.ehr.config;

import ch.qos.logback.core.CoreConstants;
import com.techpixe.ehr.entity.HR;
import com.techpixe.ehr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HR hr = userRepository.findByEmail(username);
//        List<GrantedAuthority> authorityList = List.of(new SimpleGrantedAuthority(customers.getRole()));
        //System.err.println(customers.getAuthorities() + "AuthorityAuthorizationDecision");
        List<GrantedAuthority> authorityList = List.of(new SimpleGrantedAuthority(hr.getRole()));


//        List<GrantedAuthority> authorities = customers.getAuthorities().stream().map(authority -> new
//                SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
//        System.err.println(authorities + "AuthorityAuthorizationDecision");
        return new User(hr.getEmail(), hr.getPassword(), authorityList);
    }
}
