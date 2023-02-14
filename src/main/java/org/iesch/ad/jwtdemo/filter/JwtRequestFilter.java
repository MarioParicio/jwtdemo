package org.iesch.ad.jwtdemo.filter;

import lombok.extern.slf4j.Slf4j;
import org.iesch.ad.jwtdemo.servicio.JwtService;
import org.iesch.ad.jwtdemo.servicio.UsuarioDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    UsuarioDetailsService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Filtro de peticiones");
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            log.info("JWT: {}", jwt);
            username = jwtService.extractUsername(jwt);
        }
        if (authorizationHeader != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetailService = this.userDetailService.loadUserByUsername(username);
            if (jwtService.validateToken(jwt, userDetailService)) {
                log.info("Token válido");
                var authentication = jwtService.getAuthentication(jwt, SecurityContextHolder.getContext().getAuthentication(), userDetailService);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

    }
}
