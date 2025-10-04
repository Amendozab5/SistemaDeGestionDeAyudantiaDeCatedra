package com.uteq.sgaac.services;

import com.uteq.sgaac.dto.RegisterRequest;
import com.uteq.sgaac.dto.UserAdminDTO;
import com.uteq.sgaac.model.Carrera;
import com.uteq.sgaac.model.Estudiante;
import com.uteq.sgaac.model.roles;
import com.uteq.sgaac.model.usuario_sistema;
import com.uteq.sgaac.repository.CarreraRepository;
import com.uteq.sgaac.repository.EstudianteRepository;
import com.uteq.sgaac.repository.RolRepository;
import com.uteq.sgaac.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Value("${app.base-url}")
    private String appBaseUrl;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Transactional
    public void recordLogin(usuario_sistema usuario) {
        if (usuario != null) {
            usuario.setUltimoAcceso(LocalDateTime.now());
            usuarioRepository.save(usuario);
            log.info("Último acceso actualizado para el usuario: {}", usuario.getUsername());
        }
    }

    @Transactional
    public String updateUserPhoto(String username, MultipartFile file) {
        usuario_sistema usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado para actualizar foto"));

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = StringUtils.getFilenameExtension(originalFilename);
        String newFilename = username + "_" + System.currentTimeMillis() + "." + extension;

        fileStorageService.save(file, newFilename);

        String fileUrl = "/uploads/" + newFilename;
        usuario.setFotoUrl(fileUrl);
        usuarioRepository.save(usuario);

        return fileUrl;
    }

    @Transactional
    public usuario_sistema registrar(RegisterRequest request) {

        // Validar correo único
        if (usuarioRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        // Validar cédula única
        if (usuarioRepository.existsByCedula(request.getCedula())) {
            throw new IllegalArgumentException("La cédula ya está registrada");
        }

        usuario_sistema nuevo = new usuario_sistema();

        // --- Generación de Username (Lógica del Trigger replicada en Java) ---
        String nombres = request.getNombres();
        String apellidos = request.getApellidos();

        String primerNombre = "";
        if (nombres != null && !nombres.trim().isEmpty()) {
            primerNombre = nombres.trim().split("\\s+")[0];
        }

        String primerApellido = "";
        String segundoApellido = "";
        if (apellidos != null && !apellidos.trim().isEmpty()) {
            String[] partesApellido = apellidos.trim().split("\\s+");
            primerApellido = partesApellido[0];
            if (partesApellido.length > 1) {
                segundoApellido = partesApellido[1];
            }
        }

        String inicialNombre = primerNombre.isEmpty() ? "" : primerNombre.substring(0, 1);
        String inicialSegundoApellido = segundoApellido.isEmpty() ? "" : segundoApellido.substring(0, 1);

        String baseUsername = (inicialNombre + primerApellido + inicialSegundoApellido).toLowerCase(Locale.ROOT);
        
        // Mejora: Verificar unicidad y agregar número si es necesario
        String finalUsername = baseUsername;
        int counter = 1;
        while (usuarioRepository.existsByUsername(finalUsername)) {
            finalUsername = baseUsername + counter;
            counter++;
        }
        nuevo.setUsername(finalUsername);
        // --- Fin de Generación de Username ---

        // Datos principales
        nuevo.setEmail(request.getEmail().trim().toLowerCase(Locale.ROOT));

        // Generar contraseña temporal
        SecureRandom random = new SecureRandom();
        int randomDigits = 1000 + random.nextInt(9000); // 4 dígitos
        String tempPassword = request.getCedula() + "*" + randomDigits;

        // Cifrar y guardar la contraseña
        nuevo.setPasswordHash(passwordEncoder.encode(tempPassword));

        nuevo.setActivo(true);
        nuevo.setConfirmado(false); // El usuario debe cambiar la contraseña en el primer login
        nuevo.setCreadoEn(LocalDateTime.now());

        // Token de confirmación
        String token = UUID.randomUUID().toString();
        nuevo.setTokenConfirmacion(token);

        // Datos personales
        nuevo.setNombres(request.getNombres());
        nuevo.setApellidos(request.getApellidos());
        nuevo.setCedula(request.getCedula());
        nuevo.setTelefono(request.getTelefono());

        // Rol por defecto: ESTUDIANTE
        roles rolAlumno = rolRepository.findByNombre("ESTUDIANTE")
                .orElseThrow(() -> new RuntimeException("Rol ESTUDIANTE no existe en la base de datos"));
        nuevo.setRol(rolAlumno);

        // Guardar la entidad con el username ya establecido
        usuario_sistema guardado = usuarioRepository.save(nuevo);

        // Asignar carrera y facultad aleatoria
        List<Carrera> carreras = carreraRepository.findAll();
        if (carreras.isEmpty()) {
            throw new RuntimeException("No hay carreras disponibles para asignar.");
        }
        Random rand = new Random();
        Carrera carreraAleatoria = carreras.get(rand.nextInt(carreras.size()));

        Estudiante estudiante = new Estudiante();
        estudiante.setUsuario(guardado);
        estudiante.setCarrera(carreraAleatoria);
        estudiante.setFacultad(carreraAleatoria.getFacultad());
        estudianteRepository.save(estudiante);

        // Intentar enviar el correo
        try {
            String verificationUrl = appBaseUrl + "/verify?token=" + token;
            emailService.enviarCorreo(
                    guardado.getEmail(),
                    "Bienvenido al SGAAC - Credenciales de Acceso",
                    "Hola " + guardado.getNombres() + " " + guardado.getApellidos() + ",\n\n" +
                            "Te has registrado exitosamente en el SGAAC.\n\n" +
                            "Tus credenciales de acceso son:\n" +
                            "Usuario: " + guardado.getUsername() + "\n" +
                            "Contraseña temporal: " + tempPassword + "\n\n" +
                            "Para activar tu cuenta, por favor haz clic en el siguiente enlace:\n" +
                            verificationUrl + "\n\n" +
                            "Por tu seguridad, se te pedirá que cambies tu contraseña la primera vez que inicies sesión.\n"
            );
        } catch (Exception e) {
            log.error("Fallo al enviar correo de registro para {}", guardado.getEmail(), e);
            throw new IllegalStateException("El usuario fue creado, pero ocurrió un error al enviar el correo de verificación. Por favor, contacte a un administrador.", e);
        }

        return guardado;
    }

    public usuario_sistema loginPorUsuario(String username, String password) {
        usuario_sistema usuario = usuarioRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado o inactivo"));

        // Ahora se usa BCrypt para comparar
        if (!passwordEncoder.matches(password, usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Contraseña incorrecta");
        }

        // La lógica de 'confirmado' se manejará en el success handler después del login

        return usuario;
    }

    public void changePassword(String username, String newPassword) {
        usuario_sistema usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));

        usuario.setPasswordHash(passwordEncoder.encode(newPassword));
        usuario.setConfirmado(true); // Marcar como confirmado para no volver a pedir el cambio
        usuarioRepository.save(usuario);
    }

    @Transactional
    public usuario_sistema crearUsuarioAdmin(UserAdminDTO dto) {
        if (usuarioRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        if (usuarioRepository.existsByCedula(dto.getCedula())) {
            throw new IllegalArgumentException("La cédula ya está registrada");
        }

        usuario_sistema nuevo = new usuario_sistema();

        // --- Generación de Username (Lógica idéntica a la de registro) ---
        String nombres = dto.getNombres();
        String apellidos = dto.getApellidos();
        String primerNombre = "";
        if (nombres != null && !nombres.trim().isEmpty()) {
            primerNombre = nombres.trim().split("\\s+")[0];
        }
        String primerApellido = "";
        String segundoApellido = "";
        if (apellidos != null && !apellidos.trim().isEmpty()) {
            String[] partesApellido = apellidos.trim().split("\\s+");
            primerApellido = partesApellido[0];
            if (partesApellido.length > 1) {
                segundoApellido = partesApellido[1];
            }
        }
        String inicialNombre = primerNombre.isEmpty() ? "" : primerNombre.substring(0, 1);
        String inicialSegundoApellido = segundoApellido.isEmpty() ? "" : segundoApellido.substring(0, 1);
        String baseUsername = (inicialNombre + primerApellido + inicialSegundoApellido).toLowerCase(Locale.ROOT);
        String finalUsername = baseUsername;
        int counter = 1;
        while (usuarioRepository.existsByUsername(finalUsername)) {
            finalUsername = baseUsername + counter++;
        }
        nuevo.setUsername(finalUsername);

        nuevo.setNombres(dto.getNombres());
        nuevo.setApellidos(dto.getApellidos());
        nuevo.setEmail(dto.getEmail().trim().toLowerCase(Locale.ROOT));
        nuevo.setCedula(dto.getCedula());
        nuevo.setTelefono(dto.getTelefono());

        roles rol = rolRepository.findByNombre(dto.getRol()).orElseThrow(() -> new RuntimeException("Rol no existe"));
        nuevo.setRol(rol);

        switch (dto.getEstado()) {
            case "ACTIVA":
                nuevo.setActivo(true);
                nuevo.setConfirmado(true);
                break;
            case "PENDIENTE":
                nuevo.setActivo(true);
                nuevo.setConfirmado(false);
                break;
            case "SUSPENDIDA":
                nuevo.setActivo(false);
                nuevo.setConfirmado(true);
                break;
        }

        SecureRandom random = new SecureRandom();
        int randomDigits = 1000 + random.nextInt(9000);
        String tempPassword = dto.getCedula() + "*" + randomDigits;
        nuevo.setPasswordHash(passwordEncoder.encode(tempPassword));

        String token = UUID.randomUUID().toString();
        nuevo.setTokenConfirmacion(token);

        usuario_sistema guardado = usuarioRepository.save(nuevo);

        try {
            String verificationUrl = appBaseUrl + "/verify?token=" + token;
            emailService.enviarCorreo(guardado.getEmail(), "Cuenta Creada en SGAAC", "Hola " + guardado.getNombres() + ",\n\nUn administrador ha creado una cuenta para usted.\nUsuario: " + guardado.getUsername() + "\nContraseña temporal: " + tempPassword + "\n\nPara activar su cuenta, haga clic en: " + verificationUrl);
        } catch (Exception e) {
            log.error("Error al enviar correo de creación de cuenta", e);
            throw new RuntimeException("El usuario fue creado, pero ocurrió un error al enviar el correo de verificación. Causa: " + e.getMessage(), e);
        }

        return guardado;
    }

    @Transactional
    public usuario_sistema actualizarUsuarioAdmin(Long id, UserAdminDTO dto) {
        usuario_sistema usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getEmail().equalsIgnoreCase(dto.getEmail()) && usuarioRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        if (!usuario.getCedula().equals(dto.getCedula()) && usuarioRepository.existsByCedula(dto.getCedula())) {
            throw new IllegalArgumentException("La cédula ya está registrada");
        }

        usuario.setNombres(dto.getNombres());
        usuario.setApellidos(dto.getApellidos());
        usuario.setEmail(dto.getEmail().trim().toLowerCase(Locale.ROOT));
        usuario.setCedula(dto.getCedula());
        usuario.setTelefono(dto.getTelefono());

        roles rol = rolRepository.findByNombre(dto.getRol()).orElseThrow(() -> new RuntimeException("Rol no existe"));
        usuario.setRol(rol);

        switch (dto.getEstado()) {
            case "ACTIVA":
                usuario.setActivo(true);
                usuario.setConfirmado(true);
                break;
            case "PENDIENTE":
                usuario.setActivo(true);
                usuario.setConfirmado(false);
                break;
            case "SUSPENDIDA":
                usuario.setActivo(false);
                usuario.setConfirmado(true);
                break;
        }

        return usuarioRepository.save(usuario);
    }
}
