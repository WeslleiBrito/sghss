package com.example.sghss.config;

import com.example.sghss.dto.response.ErroResponseDTO;
import com.example.sghss.exception.BusinessException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Erros de Validação dos DTOs (@Valid / @NotBlank / @Pattern) -> Status 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errosCampos = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errosCampos.put(error.getField(), error.getDefaultMessage());
        }

        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de Validação",
                "Foram encontrados erros nos campos preenchidos.",
                request.getRequestURI(),
                errosCampos
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // 2. Entidade não encontrada no Banco de Dados -> Status 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroResponseDTO> handleEntityNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso Não Encontrado",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // 3. Exceções customizadas de Regra de Negócio (BusinessException) -> Status 400
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroResponseDTO> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Regra de Negócio Violada",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // 4. Argumentos Inválidos genéricos -> Status 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponseDTO> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Requisição Inválida",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // 5. Erro de Credenciais no Login -> Status 401
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResponseDTO> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Falha de Autenticação",
                "Usuário ou senha inválidos.",
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }

    // 6. Rotas ou Endpoints que não existem (Evita HTML feio do Tomcat) -> Status 404
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResponseDTO> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Endpoint Não Encontrado",
                "A URL ou rota solicitada não existe na API.",
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // 7. Erros Inesperados / Não Mapeados (Cai aqui qualquer erro "surpresa") -> Status 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponseDTO> handleGenericException(Exception ex, HttpServletRequest request) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno do Servidor",
                "Ocorreu um erro inesperado no sistema. Contate o suporte técnico.",
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    // 8. Trata falhas de autorização (403 Forbidden - O usuário está logado, mas não tem permissão para a rota)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroResponseDTO> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        ErroResponseDTO erro = new ErroResponseDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Acesso Negado",
                "Você não possui permissão para executar esta operação ou acessar este recurso.",
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }
}