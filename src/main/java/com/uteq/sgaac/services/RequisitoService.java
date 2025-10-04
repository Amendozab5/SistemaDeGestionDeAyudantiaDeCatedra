package com.uteq.sgaac.services;

import com.uteq.sgaac.dto.RequisitoDTO;
import com.uteq.sgaac.mapping.RequisitoMapper;
import com.uteq.sgaac.model.Requisito;
import com.uteq.sgaac.repository.RequisitoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequisitoService {

    private final RequisitoRepository requisitoRepository;
    private final RequisitoMapper requisitoMapper;

    public RequisitoService(RequisitoRepository requisitoRepository, RequisitoMapper requisitoMapper) {
        this.requisitoRepository = requisitoRepository;
        this.requisitoMapper = requisitoMapper;
    }

    @Transactional(readOnly = true)
    public List<RequisitoDTO> findAll() {
        return requisitoRepository.findAll().stream()
                .map(requisitoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public RequisitoDTO save(RequisitoDTO dto) {
        Requisito requisito = requisitoMapper.toEntity(dto);
        requisito = requisitoRepository.save(requisito);
        return requisitoMapper.toDTO(requisito);
    }

    @Transactional
    public RequisitoDTO update(Long id, RequisitoDTO dto) {
        Requisito requisito = requisitoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisito no encontrado"));
        
        requisito.setDescripcion(dto.getDescripcion());
        requisito.setActivo(dto.isActivo());
        requisito.setTipo(dto.getTipo());

        // Solo actualiza la URL de la plantilla si se proporciona una nueva
        if (dto.getUrlPlantilla() != null && !dto.getUrlPlantilla().isEmpty()) {
            requisito.setUrlPlantilla(dto.getUrlPlantilla());
        }

        requisito = requisitoRepository.save(requisito);
        return requisitoMapper.toDTO(requisito);
    }

    @Transactional
    public void updateStatus(Long id, boolean activo) {
        Requisito requisito = requisitoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisito no encontrado"));
        requisito.setActivo(activo);
        requisitoRepository.save(requisito);
    }

    @Transactional
    public void delete(Long id) {
        requisitoRepository.deleteById(id);
    }
}