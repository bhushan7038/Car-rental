package com.RentIt.Rent_it_spring.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Marks this class as a Spring-managed bean (detected automatically by component scan)
@Component
// Ensures this filter runs first before other filters (highest priority)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SimpleCorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        // Convert the generic request/response to HTTP-specific classes
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        // Get the origin (frontend domain) from request headers
        String OriginHeader = request.getHeader("origin");

        // Set CORS headers so the backend can be accessed from different domains
        response.setHeader("Access-Control-Allow-Origin", OriginHeader); // Allow the requesting origin
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,PUT,OPTIONS,DELETE"); // Allowed HTTP methods
        response.setHeader("Access-Control-Max-Age", "3600"); // Cache the preflight response for 1 hour
        response.setHeader("Access-Control-Allow-Headers", "*"); // Allow all request headers

        // If the request method is OPTIONS (preflight check)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            // Return 200 OK without going further
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // Otherwise continue the normal filter chain (process request as usual)
            chain.doFilter(req, res);
        }
    }


    @Override
    public void init(FilterConfig filterConfig) {

    }
}

