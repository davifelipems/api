package com.minsait.api.controller;

import com.minsait.api.controller.dto.GetTokenRequest;
import com.minsait.api.controller.dto.GetTokenResponse;
import com.minsait.api.repository.UsuarioRepository;
import com.minsait.api.security.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    JWTUtil jwtUtil;

    @PostMapping("/get-token")
    public ResponseEntity<GetTokenResponse> getToken(@RequestBody GetTokenRequest request) {
        final var usuarioEntity = usuarioRepository.findByLogin(request.getUserName());
        if (usuarioEntity != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(request.getPassword(), usuarioEntity.getSenha())) {
                final ArrayList<String> permissions = new ArrayList<>();
                Collections.addAll(permissions, usuarioEntity.getPermissoes().split(","));
                final var token = jwtUtil.generateToken(usuarioEntity.getLogin(), permissions, usuarioEntity.getId().intValue());
                return new ResponseEntity<>(GetTokenResponse.builder()
                        .accessToken(token)
                        .build(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(GetTokenResponse.builder().build(), HttpStatus.UNAUTHORIZED);
    }
}
