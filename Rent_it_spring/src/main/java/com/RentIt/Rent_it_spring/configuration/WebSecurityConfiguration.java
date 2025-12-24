package com.RentIt.Rent_it_spring.configuration;


import com.RentIt.Rent_it_spring.services.jwt.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

//Marks the class as a configuration class in Spring.
@Configuration
//Enables Spring Security’s web security support.
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UserService userService;

    //    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(request ->
//                        request.requestMatchers("/api/auth/**").permitAll()
//                                .requestMatchers("/api/admin/**").hasAnyAuthority(UserRole.ADMIN.name()).
//                                requestMatchers("/api/customer/**").hasAnyAuthority(UserRole.CUSTOMER.name()).
//                                anyRequest().authenticated()).sessionManagement(manager ->
//                        manager.sessionCreationPolicy(STATELESS)).
//                authenticationProvider(authenticationProvider()).
//                addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }


    // Expose this method as a Spring bean so Spring Security will use the returned SecurityFilterChain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Start configuring HttpSecurity
        http
                // Disable CSRF protection because this is a stateless REST API using JWT (no browser form logins).
                .csrf(AbstractHttpConfigurer::disable)

//                .cors(cors -> cors.disable())
                // Enable CORS and provide a custom configuration source so Spring Security applies CORS rules.
                .cors(cors -> cors.configurationSource(request -> {
                    // Create a CorsConfiguration instance to define allowed origins, methods, headers, etc.
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();

                    // Allow requests from your Angular dev server (adjust if your frontend URL differs)
                    corsConfig.setAllowedOrigins(java.util.List.of("http://localhost:4200"));

                    // Allow the HTTP methods your frontend will use
                    corsConfig.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

                    corsConfig.setAllowedHeaders(java.util.List.of("Authorization", "Content-Type", "Accept"));

                    // Allow these request headers from the browser (Authorization is needed for Bearer token)
//                    corsConfig.setAllowedHeaders(java.util.List.of("Authorization", "Content-Type", "Accept"));

                    corsConfig.setExposedHeaders(java.util.List.of("Authorization")); // ✅ allow browser to read this header

                    // Allow credentials (cookies, authorization headers). Set to true if you send credentials.
                    corsConfig.setAllowCredentials(true);

                    // Return the configured CorsConfiguration for the current request
                    return corsConfig;
                }))

                // Authorization rules for URL patterns
                .authorizeHttpRequests(request -> request
                                // Public endpoints (login, register, etc.) — no authentication required
                                .requestMatchers("/api/auth/**").permitAll()

                                // Endpoints under /api/admin/** require the ADMIN authority
                                // .requestMatchers("/api/admin/**").hasAnyAuthority(UserRole.ADMIN.name())
                                .requestMatchers("/api/admin/**").permitAll()

                                // Endpoints under /api/customer/** require the CUSTOMER authority
//                        .requestMatchers("/api/customer/**").hasAnyAuthority(UserRole.CUSTOMER.name())
                                .requestMatchers("/api/customer/**").permitAll()

                                // Any other request must be authenticated (i.e., must have a valid JWT)
//                                .anyRequest().authenticated()
                                .anyRequest().permitAll()
                )

                // Use stateless session management because authentication is handled via JWT, not server session
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))

                // Use the custom AuthenticationProvider (DAO provider wired with your UserDetailsService & password encoder)
                .authenticationProvider(authenticationProvider());

        // Add your JWT filter before Spring's UsernamePasswordAuthenticationFilter so JWTs are validated early
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Build and return the SecurityFilterChain bean
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();


    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {

        return config.getAuthenticationManager();
    }

}
