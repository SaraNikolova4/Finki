package mk.ukim.finki.wp.jan2023.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 *  This class is used to configure user login on path '/login' and logout on path '/logout'.
 *  The only public page in the application should be '/'.
 *  All other pages should be visible only for a user with role 'ROLE_ADMIN'.
 *  Furthermore, in the "list.html" template, the 'Edit', 'Delete', 'Add' buttons should only be
 *  visible for a user with role 'ROLE_ADMIN'.
 *  The 'Vote for Candidate' button should only be visible for a user with role 'ROLE_USER'.
 *
 *  For login inMemory users should be used. Their credentials are given below:
 *  [{
 *      username: "user",
 *      password: "user",
 *      role: "ROLE_USER"
 *  },
 *
 *  {
 *      username: "admin",
 *      password: "admin",
 *      role: "ROLE_ADMIN"
 *  }]
 */

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
public class SecurityConfig {

      private final PasswordEncoder passwordEncoder;

        public SecurityConfig(PasswordEncoder passwordEncoder) {
            this.passwordEncoder = passwordEncoder;

        }

/**
 *  This class is used to configure user login on path '/login' and logout on path '/logout'.
 *  The only public page in the application should be '/'.
 *  All other pages should be visible only for a user with role 'ROLE_ADMIN'.
 *  Furthermore, in the "list.html" template, the 'Edit', 'Delete', 'Add' buttons should only be
 *  visible for a user with role 'ROLE_ADMIN'.
 *  The 'Vote for Candidate' button should only be visible for a user with role 'ROLE_USER'. **/
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception  {

//            http
//                    .csrf(AbstractHttpConfigurer::disable)
//                    .authorizeHttpRequests( (requests) -> requests
//                            .requestMatchers(new AntPathRequestMatcher("/","/h2"))
//                            .permitAll()
//                            .requestMatchers(new AntPathRequestMatcher("/candidates/**/vote")).hasRole("USER")
//                            .anyRequest()
//                            .hasRole("ADMIN")
//                    )
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests( (requests) -> requests
                            .requestMatchers(AntPathRequestMatcher.antMatcher("/"),
                                    AntPathRequestMatcher.antMatcher( "/candidates"))
                            .permitAll()
                            .requestMatchers(AntPathRequestMatcher.antMatcher("/candidates/**/vote"))
                            .hasAnyRole( "USER")
                            .requestMatchers(AntPathRequestMatcher.antMatcher("/candidates/**"))
                            .hasAnyRole("ADMIN", "USER")
                            .anyRequest()
                            .authenticated()
                    )


                    .formLogin((form) -> form
                            .permitAll()
                            .failureUrl("/login?error=BadCredentials")
                            .defaultSuccessUrl("/candidates", true)
                    )
                    .logout((logout) -> logout
                            .logoutUrl("/logout")
                            .clearAuthentication(true)
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                            .logoutSuccessUrl("/")
                    );

            return http.build();
        }

        // In Memory Authentication
        @Bean
        public UserDetailsService userDetailsService() {
            UserDetails user1 = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user"))
                    .roles("USER")
                    .build();
            UserDetails user2 = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles("ADMIN")
                    .build();

            return new InMemoryUserDetailsManager(user1, user2);
        }

    }

