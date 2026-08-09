package com.example.sghss.service;

import com.example.sghss.model.LogAuditoria;
import com.example.sghss.repository.LogAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final LogAuditoriaRepository logAuditoriaRepository;

    // O @Async joga a gravação do log para uma thread paralela!
    @Async
    public void registrarLogLeitura(String usuario, String endpoint, String parametros, String ip) {
        LogAuditoria log = new LogAuditoria();
        log.setUsuarioLogin(usuario);
        log.setEndpointAcessado(endpoint);
        log.setParametrosBuscados(parametros);
        log.setIpOrigem(ip);

        logAuditoriaRepository.save(log);
    }
}