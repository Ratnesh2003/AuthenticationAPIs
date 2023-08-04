package com.example.authentication.service.jwt;

import com.example.authentication.exceptions.TokenDoesNotExistException;
import com.example.authentication.model.User;
import com.example.authentication.service.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestTokenHeader = request.getHeader("Authorization");
        String email = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                email = this.jwtUtil.getEmailFromToken(jwtToken);
            } catch(IllegalArgumentException e){
                System.out.println("Unable to get Jwt token");
            }
            catch(ExpiredJwtException e){
                System.out.println("Jwt token has expired");
            }
            catch(MalformedJwtException e){
                System.out.println("Invalid Jwt");
            } catch (TokenDoesNotExistException e){
                System.out.println("Invalid token");
            }
        } else {
            System.out.println("JWT does not begin with Bearer");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userDetailsService.loadUserByUsername(email);
            if (this.jwtUtil.validateToken(jwtToken, user)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                System.out.println("Invalid JWT Token");
            }
        } else {
            System.out.println("User is null or context is not null");
        }
        filterChain.doFilter(request, response);
    }
}
