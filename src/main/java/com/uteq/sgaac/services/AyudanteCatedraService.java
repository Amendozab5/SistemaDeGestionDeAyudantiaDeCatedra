package com.uteq.sgaac.services;

import com.uteq.sgaac.model.AyudanteCatedra;
import com.uteq.sgaac.model.Resultado;
import com.uteq.sgaac.repository.AyudanteCatedraRepository;
import com.uteq.sgaac.repository.ResultadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AyudanteCatedraService {

    private final ResultadoRepository resultadoRepository;
    private final AyudanteCatedraRepository ayudanteCatedraRepository;

    public AyudanteCatedraService(ResultadoRepository resultadoRepository, AyudanteCatedraRepository ayudanteCatedraRepository) {
        this.resultadoRepository = resultadoRepository;
        this.ayudanteCatedraRepository = ayudanteCatedraRepository;
    }

    public List<Resultado> findGanadoresSinAyudantia() {
        List<Resultado> ganadores = resultadoRepository.findByEstadoFinal("GANADOR");
        return ganadores.stream()
                .filter(resultado -> {
                    Optional<AyudanteCatedra> ayudantiaExistente = ayudanteCatedraRepository.findByPostulacion(resultado.getPostulacion());
                    return ayudantiaExistente.isEmpty();
                })
                .collect(Collectors.toList());
    }
}