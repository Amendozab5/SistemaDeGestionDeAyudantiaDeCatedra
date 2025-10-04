
package com.uteq.sgaac.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uteq.sgaac.dto.RegisterRequest;
import com.uteq.sgaac.model.usuario_sistema;
import com.uteq.sgaac.repository.RolRepository;
import com.uteq.sgaac.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    @Test
    void registrar_ConEmailExistente_DebeLanzarExcepcion() {
        // Arrange (Preparar)
        // 1. Creamos una solicitud de registro con un email de prueba
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        
        request.setNombres("Test");
        request.setApellidos("User");
        request.setCedula("1234567890");
        request.setTelefono("0987654321");

        // 2. Simulamos que el repositorio encuentra un usuario con ese email
        when(usuarioRepository.existsByEmailIgnoreCase(request.getEmail())).thenReturn(true);

        // Act & Assert (Actuar y Afirmar)
        // 3. Ejecutamos el método y esperamos que lance una excepción
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            authService.registrar(request);
        });

        // 4. Verificamos que el mensaje de la excepción es el esperado
        assertEquals("El correo ya está registrado", exception.getMessage());

        // 5. Nos aseguramos de que no se guardó ningún usuario ni se envió ningún correo
        verify(usuarioRepository, never()).save(any(usuario_sistema.class));
        verify(emailService, never()).enviarCorreo(anyString(), anyString(), anyString());
    }
}
