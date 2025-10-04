package com.uteq.sgaac.controller;

import com.uteq.sgaac.dto.UserAdminDTO;
import com.uteq.sgaac.dto.UserDTO;
import com.uteq.sgaac.mapping.UserMapper;
import com.uteq.sgaac.model.usuario_sistema;
import com.uteq.sgaac.repository.UsuarioRepository;
import com.uteq.sgaac.repository.UsuarioSpecification;
import com.uteq.sgaac.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN', 'MODERADOR')")
public class AdminController {
  private final UsuarioRepository repo;
  private final UserMapper mapper;
  private final AuthService authService;

  public AdminController(UsuarioRepository r, UserMapper m, AuthService authService){
    this.repo=r;
    this.mapper=m;
    this.authService = authService;
  }

  @GetMapping(value="/users", produces="application/json")
  public Page<UserDTO> listar(
      @RequestParam(defaultValue="0") int page,
      @RequestParam(defaultValue="10") int size,
      @RequestParam(defaultValue="apellidos,asc") String sort,
      @RequestParam(required = false) String q,
      @RequestParam(required = false) String rol,
      @RequestParam(required = false) String estado) { 
    String[] p=sort.split(",");
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(p.length>1?p[1]:"asc"), p[0]));
    
    Specification<usuario_sistema> spec = UsuarioSpecification.findByCriteria(q, rol, estado);
    
    return repo.findAll(spec, pageable).map(mapper::toDTO);
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
      return repo.findById(id)
              .map(mapper::toDTO)
              .map(ResponseEntity::ok)
              .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/users")
  public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserAdminDTO userDTO) {
      usuario_sistema newUser = authService.crearUsuarioAdmin(userDTO);
      UserDTO resultDTO = mapper.toDTO(newUser);
      return ResponseEntity.created(URI.create("/admin/users/" + resultDTO.getId()))
              .body(resultDTO);
  }

  @PutMapping("/users/{id}")
  public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserAdminDTO userDTO) {
      usuario_sistema updatedUser = authService.actualizarUsuarioAdmin(id, userDTO);
      return ResponseEntity.ok(mapper.toDTO(updatedUser));
  }

}
