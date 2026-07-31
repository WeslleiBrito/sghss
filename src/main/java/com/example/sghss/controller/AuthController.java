package com.example.sghss.controller;

import com.example.sghss.dto.request.LoginRequestDTO;
import com.example.sghss.dto.response.TokenResponseDTO;
import com.example.sghss.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        TokenResponseDTO response = authService.autenticar(dto);
        return ResponseEntity.ok(response);
    }
}