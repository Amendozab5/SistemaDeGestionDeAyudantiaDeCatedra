package com.uteq.sgaac.services;

import com.uteq.sgaac.dto.AutoridadDTO;
import com.uteq.sgaac.dto.UserAdminDTO;
import com.uteq.sgaac.model.*;
import com.uteq.sgaac.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutoridadService {

    @Autowired
    private AuthService authService;

    @Autowired
    private DecanoRepository decanoRepository;

    @Autowired
    private CoordinadorRepository coordinadorRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private FacultadRepository facultadRepository;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    @Transactional
    public void createAutoridad(AutoridadDTO autoridadDTO) {
        UserAdminDTO userAdminDTO = new UserAdminDTO();
        userAdminDTO.setNombres(autoridadDTO.getNombres());
        userAdminDTO.setApellidos(autoridadDTO.getApellidos());
        userAdminDTO.setEmail(autoridadDTO.getEmail());
        userAdminDTO.setCedula(autoridadDTO.getCedula());
        userAdminDTO.setTelefono(autoridadDTO.getTelefono());
        userAdminDTO.setRol(autoridadDTO.getRol());
        userAdminDTO.setEstado("PENDIENTE");

        usuario_sistema nuevoUsuario = authService.crearUsuarioAdmin(userAdminDTO);

        switch (autoridadDTO.getRol()) {
            case "DECANO":
                Facultad facultadDecano = facultadRepository.findById(autoridadDTO.getIdFacultad())
                        .orElseThrow(() -> new RuntimeException("Facultad no encontrada"));
                Decano decano = new Decano();
                decano.setUsuario(nuevoUsuario);
                decano.setFacultad(facultadDecano);
                decanoRepository.save(decano);
                break;
            case "COORDINADOR":
                Facultad facultadCoord = facultadRepository.findById(autoridadDTO.getIdFacultad())
                        .orElseThrow(() -> new RuntimeException("Facultad no encontrada"));
                Carrera carreraCoord = carreraRepository.findById(autoridadDTO.getIdCarrera())
                        .orElseThrow(() -> new RuntimeException("Carrera no encontrada"));
                Coordinador coordinador = new Coordinador();
                coordinador.setUsuario(nuevoUsuario);
                coordinador.setFacultad(facultadCoord);
                coordinador.setCarrera(carreraCoord);
                coordinadorRepository.save(coordinador);
                break;
            case "DOCENTE":
                Facultad facultadDocente = facultadRepository.findById(autoridadDTO.getIdFacultad())
                        .orElseThrow(() -> new RuntimeException("Facultad no encontrada"));
                Carrera carreraDocente = carreraRepository.findById(autoridadDTO.getIdCarrera())
                        .orElseThrow(() -> new RuntimeException("Carrera no encontrada"));
                Asignatura asignaturaDocente = asignaturaRepository.findById(autoridadDTO.getIdAsignatura())
                        .orElseThrow(() -> new RuntimeException("Asignatura no encontrada"));
                Docente docente = new Docente();
                docente.setUsuario(nuevoUsuario);
                docente.setFacultad(facultadDocente);
                docente.setCarrera(carreraDocente);
                docente.setAsignatura(asignaturaDocente);
                docenteRepository.save(docente);
                break;
            default:
                throw new RuntimeException("Rol de autoridad no válido: " + autoridadDTO.getRol());
        }
    }
}