package org.iesch.ad.jwtdemo.controlador;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.iesch.ad.jwtdemo.servicio.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j


@RequestMapping("/api/")
public class RestJwtControler {
    @Autowired
    JwtService jwtService;

    @GetMapping("publico/genera")
    public ResponseEntity<?> generaTokenPublico(){
        log.info("Generando token público");
        String jwt = jwtService.creaJwt();
        Map<String,String> contenido = new HashMap<>();
        contenido.put("jwt",jwt);
        log.info("Token generado: {}",jwt);
        return ResponseEntity.ok(contenido);
    }
    @GetMapping("publico/comprueba")
    public ResponseEntity<?> compruebaTokenPublico(@RequestParam String jwt){
        log.info("Comprobando token público");
        log.info("Token recibido: {}",jwt);

        return ResponseEntity.ok(jwtService.compruebaJwt(jwt));
    }
    @GetMapping("admin")
    public ResponseEntity<?> getMensajeAdmin(){
        log.info("Mensaje para admin");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Datos del usuario: {}",auth.getPrincipal());
        log.info("Usuario: {}", auth.getName());
        log.info("Roles: {}", auth.getAuthorities());
        log.info("Esta autenticado: {}", auth.isAuthenticated());
        Map<String,String> mensaje = new HashMap<>();
        mensaje.put("contenido","Mensaje que solo puede ver el admin");
        return ResponseEntity.ok(mensaje);
    }

}

