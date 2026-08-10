package com.example.sghss.config;

import com.example.sghss.service.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuditoriaInterceptor implements HandlerInterceptor {

    private final AuditoriaService auditoriaService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {

        if ("GET".equalsIgnoreCase(request.getMethod())) {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {

                String usuario = auth.getName();
                String endpoint = request.getRequestURI();
                String parametros = request.getQueryString();
                String ip = request.getRemoteAddr();

                auditoriaService.registrarLogLeitura(usuario, endpoint, parametros, ip);
            }
        }

        return true;
    }
}