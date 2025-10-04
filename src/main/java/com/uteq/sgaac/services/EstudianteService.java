package com.uteq.sgaac.services;

import com.uteq.sgaac.dto.EstudianteDTO;
import com.uteq.sgaac.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstudianteService {

    @Autowired
    private AuthService authService;

    @Transactional
    public void createEstudiante(EstudianteDTO estudianteDTO) {
        // 1. Create RegisterRequest from EstudianteDTO
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setNombres(estudianteDTO.getNombres());
        registerRequest.setApellidos(estudianteDTO.getApellidos());
        registerRequest.setEmail(estudianteDTO.getEmail());
        registerRequest.setCedula(estudianteDTO.getCedula());
        registerRequest.setTelefono(estudianteDTO.getTelefono());

        // 2. Create user using AuthService.registrar
        authService.registrar(registerRequest);
    }
}