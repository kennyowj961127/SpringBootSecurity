package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.Customer;
import org.example.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        Customer savedCustomer = null;
        ResponseEntity<String> response = null;
        try {
            String hashPwd = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPwd);
            savedCustomer = customerRepository.save(customer);
            if (savedCustomer.getId() > 0) {
                System.out.println("User registered successfully with id: " + savedCustomer.getId());
                response = ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("User registered successfully with id: " + savedCustomer.getId());
            }else {
                response = ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Error occurred while registering user");
            }
        } catch (Exception e) {
            response = ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while registering user: " + e.getMessage());
        }
        return response;
    }
}
