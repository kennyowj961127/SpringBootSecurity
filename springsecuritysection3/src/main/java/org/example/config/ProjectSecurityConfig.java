package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((requests)->requests
                .requestMatchers("/myAccount", "myBalance", "myLoans", "myCards").authenticated()
                .requestMatchers("/notices", "/contact", "/register").permitAll())
                .formLogin(withDefaults())
                .httpBasic(withDefaults());

        return http.build();
    }

    /*
    *   This method is used to create a user with the username "admin" and password "admin" with the role "admin"
    */
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(){
////        Approach 1 - Using User.withDefaultPasswordEncoder()
////        UserDetails admin = User.withDefaultPasswordEncoder()
////                .username("admin")
////                .password("admin")
////                .authorities("admin")
////                .build();;
////
////        UserDetails user = User.withDefaultPasswordEncoder()
////                .username("user")
////                .password("user")
////                .authorities("read")
////                .build();;
////
////        return new InMemoryUserDetailsManager(admin, user);
//
////        Approach 2 - Using NoOpPasswordEncoder
//        UserDetails admin = User.withUsername("admin")
//                .password("admin")
//                .authorities("admin")
//                .build();
//
//        UserDetails user = User.withUsername("user")
//                .password("user")
//                .authorities("read")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }

//    @Bean
//    public UserDetailsService userDetailsService(DataSource dataSource){
//        return new JdbcUserDetailsManager(dataSource);
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password("{noop}HelloPassword@123456").authorities("read").build();
        UserDetails admin = User.withUsername("admin")
                .password("{bcrypt}$2a$12$YhxhAYbpwLarTK9DD3cgTO6vpcblKKhKX.32RU6RNLCL2RtwL9UQK")
                .authorities("admin").build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
       return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /* Spring Security 6.3 above only */
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker(){
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
