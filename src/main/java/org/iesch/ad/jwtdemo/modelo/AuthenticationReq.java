package org.iesch.ad.jwtdemo.modelo;


import lombok.Data;

@Data
public class AuthenticationReq {

    private String usuario;
    private String clave;
}
