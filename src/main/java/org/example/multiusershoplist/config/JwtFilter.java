package org.example.multiusershoplist.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.multiusershoplist.Service.CustomUserDetailsService;
import org.example.multiusershoplist.Service.JWTService;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    JWTService jwtService;
    ApplicationContext context;




    public JwtFilter(JWTService jwtService, ApplicationContext context) {
        this.jwtService = jwtService;
        this.context = context;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        String token;
        String userName ;
        try {
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring("Bearer ".length());
                userName = jwtService.extractUserName(token);

                if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = context.getBean(CustomUserDetailsService.class).loadUserByUsername(userName);

                    if (jwtService.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

            }

            filterChain.doFilter(request,response);

        }catch (Exception e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(e.getMessage());
        }



    }
}
