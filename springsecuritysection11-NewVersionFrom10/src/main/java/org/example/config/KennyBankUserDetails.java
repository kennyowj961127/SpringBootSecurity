package org.example.config;

import lombok.RequiredArgsConstructor;
import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KennyBankUserDetails implements UserDetailsService {

    private final CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User details not found for the user: " + username));
        List<GrantedAuthority> grantedAuthorities = customer.getAuthorities().stream().map(authority ->
                new SimpleGrantedAuthority(authority.getName())).collect(Collectors.toList());
        return new User(customer.getEmail(), customer.getPwd(), grantedAuthorities);
    }
}
