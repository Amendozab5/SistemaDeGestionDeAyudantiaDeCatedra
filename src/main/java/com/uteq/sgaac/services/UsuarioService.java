package com.uteq.sgaac.services;

import com.uteq.sgaac.dto.AutoridadSimpleDTO;
import com.uteq.sgaac.dto.EstudianteDTO;
import com.uteq.sgaac.dto.UsuarioSimpleDTO;
import com.uteq.sgaac.model.usuario_sistema;
import com.uteq.sgaac.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<UsuarioSimpleDTO> getEstudiantes() {
        List<usuario_sistema> estudiantes = usuarioRepository.findByRolNombre("ESTUDIANTE");
        return estudiantes.stream()
                .map(user -> new UsuarioSimpleDTO(
                        user.getIdUsuario(),
                        user.getNombres(),
                        user.getApellidos(),
                        user.getEmail(),
                        user.getCedula(),
                        user.isActivo()))
                .collect(Collectors.toList());
    }

    public List<AutoridadSimpleDTO> getAutoridades() {
        List<usuario_sistema> autoridades = usuarioRepository.findByRolNombreIn(List.of("DOCENTE", "DECANO", "COORDINADOR"));
        return autoridades.stream()
                .map(this::toAutoridadSimpleDTO)
                .collect(Collectors.toList());
    }

    public AutoridadSimpleDTO getUsuario(Long id) {
        return usuarioRepository.findById(id)
                .map(this::toAutoridadSimpleDTO)
                .orElse(null);
    }

    public UsuarioSimpleDTO updateUsuario(Long id, EstudianteDTO estudianteDTO) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombres(estudianteDTO.getNombres());
            usuario.setApellidos(estudianteDTO.getApellidos());
            usuario.setEmail(estudianteDTO.getEmail());
            usuario.setCedula(estudianteDTO.getCedula());
            usuario.setTelefono(estudianteDTO.getTelefono());
            usuarioRepository.save(usuario);
            return new UsuarioSimpleDTO(
                usuario.getIdUsuario(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getEmail(),
                usuario.getCedula(),
                usuario.isActivo());
        }).orElse(null);
    }

    public AutoridadSimpleDTO toggleActivo(Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setActivo(!usuario.isActivo());
            usuarioRepository.save(usuario);
            return toAutoridadSimpleDTO(usuario);
        }).orElse(null);
    }

    private AutoridadSimpleDTO toAutoridadSimpleDTO(usuario_sistema user) {
        return new AutoridadSimpleDTO(
                user.getIdUsuario(),
                user.getNombres(),
                user.getApellidos(),
                user.getEmail(),
                user.getCedula(),
                user.isActivo(),
                user.getRol().getNombre());
    }
}
