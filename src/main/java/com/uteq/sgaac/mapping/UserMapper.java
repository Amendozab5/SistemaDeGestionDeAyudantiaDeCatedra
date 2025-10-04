package com.uteq.sgaac.mapping;

import com.uteq.sgaac.dto.UserDTO;
import com.uteq.sgaac.model.usuario_sistema;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Collections;

@Component
public class UserMapper {

    public UserDTO toDTO(usuario_sistema u) {
        if (u == null) {
            return null;
        }

        String estado;
        if (!u.isConfirmado()) {
            estado = "PENDIENTE";
        } else if (!u.isActivo()) {
            estado = "SUSPENDIDA";
        } else {
            estado = "ACTIVA";
        }

        UserDTO dto = new UserDTO();
        dto.setId(u.getIdUsuario());
        dto.setNombre(u.getNombres());
        dto.setApellido(u.getApellidos());
        dto.setEmail(u.getEmail());
        dto.setCedula(u.getCedula());
        dto.setTelefono(u.getTelefono());
        if (u.getRol() != null) {
            dto.setRoles(Collections.singletonList(u.getRol().getNombre()));
        }
        dto.setEstado(estado);
        if (u.getUltimoAcceso() != null) {
            dto.setUltimoAcceso(u.getUltimoAcceso().atZone(ZoneId.systemDefault()).toInstant());
        }

        return dto;
    }
}
