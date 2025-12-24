package com.RentIt.Rent_it_spring.configuration;

import com.RentIt.Rent_it_spring.services.jwt.UserService;
import com.RentIt.Rent_it_spring.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//This class is a Spring-managed bean
@Component
//automatically generates a constructor with all final fields.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Utility class to handle JWT operations (extract username, validate token, etc.)
    private final JWTUtil jwtUtil;

    // Service to load user details from DB
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Get the "Authorization" header from the request
        final String authHeader = request.getHeader("Authorization");

        final String jwt;
        final String userEmail;

        String requestPath = request.getServletPath();


        if (requestPath.startsWith("/api/customer/")) {
            filterChain.doFilter(request, response);
            return;
        }


        // If the header is missing OR does not start with "Bearer", just continue the filter chain
        if (org.apache.commons.lang3.StringUtils.isEmpty(authHeader) ||
                !org.apache.commons.lang3.StringUtils.startsWith(authHeader, "Bearer")) {

            filterChain.doFilter(request, response);
            return; // stop further execution of this filter
        }

        // Extract JWT token by removing "Bearer " prefix (first 7 characters)
        jwt = authHeader.substring(7);

        // Extract username (email) from the JWT
        userEmail = jwtUtil.extractUserName(jwt);

        // If username exists and no authentication is already set in SecurityContext
        if (StringUtils.isNotEmpty(userEmail) &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user details from DB
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);

            // Validate the JWT against user details
            if (jwtUtil.isTokenValid(jwt, userDetails)) {

                // Create a new empty SecurityContext
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                // Create authentication token with user details and roles
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()

                        );
                System.out.println("Authorities from JWT: " + userDetails.getAuthorities());

                // Attach request details (like IP, session info) to the token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set authentication into the SecurityContext
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }

        // Continue the filter chain (whether authenticated or not)
        filterChain.doFilter(request, response);
        System.out.println("JWT received: " + jwt);

    }
}
