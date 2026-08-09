package com.example.sghss.controller;

import com.example.sghss.dto.response.DicionarioResponseDTO;
import com.example.sghss.model.enums.TipoContato;
import com.example.sghss.model.enums.TipoParentesco;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dicionarios")
public class DicionarioController {

    @GetMapping("/tipos-contato")
    public ResponseEntity<List<DicionarioResponseDTO>> listarTiposContato() {
        List<DicionarioResponseDTO> lista = Arrays.stream(TipoContato.values())
                .map(tipo -> new DicionarioResponseDTO(
                        tipo.name(), // Mantém a string técnica para o Request ("WHATSAPP")
                        tipo.getDescricao() // Pega o nome bonito que você definiu ("WhatsApp")
                ))
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/tipos-parentesco")
    public ResponseEntity<List<DicionarioResponseDTO>> listarTiposParentesco() {
        List<DicionarioResponseDTO> lista = Arrays.stream(TipoParentesco.values())
                .map(tipo -> new DicionarioResponseDTO(
                        tipo.name(),
                        tipo.getDescricao() // Utiliza a descrição já definida no seu Enum
                ))
                .toList();

        return ResponseEntity.ok(lista);
    }

}