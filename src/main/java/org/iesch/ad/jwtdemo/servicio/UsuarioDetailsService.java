package org.iesch.ad.jwtdemo.servicio;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class UsuarioDetailsService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Map<String, String> usuarios = Map.of(
                "Juanma", "USER",
                "admin", "ADMIN"
        );

        var rol = usuarios.get(username);

        if (rol == null)  throw new UsernameNotFoundException("Usuario no encontrado");
         else {
            return User.withUsername(username)
                    .password("{noop}1234")
                    .roles(rol).build();
        }
    }
}
