
// Defines the development-profile web security rules; this is configuration, not product authentication logic.
package com.platform.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// @Configuration marks this as Spring configuration; @Profile("dev") loads it only when the dev profile is active.
@Configuration
@Profile("dev")
public class SecurityConfig {

    /**
     * Builds the development HTTP security filter chain.
     *
     * @param http Spring's object for configuring HTTP security behavior
     * @return the configured security filter chain used by Spring Security
     * @throws Exception if Spring Security cannot build the chain
     */
    // @Bean registers the returned chain with Spring so it can apply these rules to requests.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            // Disable CSRF protection in this development configuration.
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Let error dispatches reach error handling without a separate login requirement.
                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                // These development product URLs are public; permitAll does not authenticate callers.
                .requestMatchers(
                    "/api/businesses/*/products",
                    "/api/businesses/*/products/*",
                    "/api/businesses/*/products/*/inventory",
                    "/api/businesses/*/products/*/inventory/*",
                    "/api/businesses/*/customers",
                    "/api/businesses/*/customers/*"
                ).permitAll()
                // Other requests still require authentication under the configured Spring Security setup.
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
