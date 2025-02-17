package com.example.prueba.services;

import com.example.prueba.models.Persona;
import com.example.prueba.models.Rol;
import com.example.prueba.models.SessionLog;
import com.example.prueba.models.Usuario;
import com.example.prueba.repositories.PersonaRepository;
import com.example.prueba.repositories.RolRepository;
import com.example.prueba.repositories.SessionLogRepository;
import com.example.prueba.repositories.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private SessionLogRepository sessionLogRepository;

    @Autowired
    private RolRepository rolRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Map<String, Object> register(String username, String password, String identificacion, String nombres, String apellidos, String fechaNacimiento) {
        // Verificar si el usuario ya existe
        if (usuarioRepository.findByUsername(username).isPresent()) {
            return Map.of("error", "El nombre de usuario ya está registrado.");
        }

        // Encriptar la contraseña
        String encodedPassword = passwordEncoder.encode(password);

        // Crear Persona
        Persona persona = new Persona();
        persona.setIdentificacion(identificacion);
        persona.setNombres(nombres);
        persona.setApellidos(apellidos);

        // Convertir fechaNacimiento de String a LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        persona.setFechaNacimiento(LocalDate.parse(fechaNacimiento, formatter));

        // Guardar Persona primero
        persona = personaRepository.save(persona);

        // Generar correo automáticamente
        String correoGenerado = generarCorreo(nombres, apellidos);

        // Verificar si el correo ya existe y modificarlo si es necesario
        int contador = 0;
        String correoFinal = correoGenerado;
        while (usuarioRepository.existsByMail(correoFinal)) {
            contador++;
            correoFinal = correoGenerado.replace("@mail.com", contador + "@mail.com");
        }

        // Crear Usuario y asociarlo con la Persona
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(encodedPassword);
        usuario.setMail(correoFinal);  // Correo generado automáticamente
        usuario.setSessionActive("0");
        usuario.setStatus("activo");
        usuario.setIntentosFallidos(0);
        usuario.setPersona(persona);

        // Asignar rol por defecto
        Optional<Rol> rolOpt = rolRepository.findByNombre("ROLE_USER");
        rolOpt.ifPresent(rol -> usuario.setRoles(Collections.singleton(rol)));
        Rol rolUsuario;
        if (rolOpt.isPresent()) {
            rolUsuario = rolOpt.get();
        } else {
            // Si el rol no existe, crearlo automáticamente
            rolUsuario = new Rol();
            rolUsuario.setNombre("ROLE_USER");
            rolUsuario = rolRepository.save(rolUsuario);
        }

        // Asignar el rol al usuario
        usuario.setRoles(Collections.singleton(rolUsuario));

        // Guardar Usuario
        usuarioRepository.save(usuario);

        // Obtener los roles en formato String
        Set<String> roles = usuario.getRoles().stream().map(Rol::getNombre).collect(Collectors.toSet());

        // Devolver los datos del usuario registrado junto con su rol
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Usuario registrado exitosamente.");
        response.put("id", usuario.getIdUsuario());
        response.put("username", usuario.getUsername());
        response.put("mail", usuario.getMail());
        response.put("roles", roles);

        return response;
    }

    // Método para generar correo a partir del nombre y apellido
    private String generarCorreo(String nombres, String apellidos) {
        String primeraLetra = nombres.split(" ")[0].substring(0, 1).toLowerCase();
        String primerApellido = apellidos.split(" ")[0].toLowerCase();
        primerApellido = Normalizer.normalize(primerApellido, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        return primeraLetra + primerApellido + "@mail.com";
    }
    // Método para autenticación

    public String login(String usernameOrMail, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsernameOrMail(usernameOrMail, usernameOrMail);

        if (usuarioOpt.isEmpty()) {
            return "Usuario no encontrado.";
        }

        Usuario usuario = usuarioOpt.get();

        // 🔹 Verificar datos de usuario
        System.out.println("✅ Usuario autenticado: " + usuario.getUsername());
        System.out.println("📧 Correo: " + usuario.getMail());
        System.out.println("🔐 Contraseña encriptada: " + usuario.getPassword());
        System.out.println("🟢 Estado: " + usuario.getStatus());

        // 🔹 Verificar si los roles están cargados
        System.out.println("🎭 Roles asignados:");
        usuario.getRoles().forEach(rol -> System.out.println("   - " + rol.getNombre()));

        if (usuario.getRoles().isEmpty()) {
            return "❌ Error: El usuario no tiene roles asignados.";
        }


        // Verificar si el usuario está bloqueado
        if ("bloqueado".equalsIgnoreCase(usuario.getStatus())) {
            return "Usuario bloqueado. Contacte al administrador.";
        }
        if (usuario.getSessionActive().equals("1")) {
            return "El usuario ya tiene una sesión activa.";
        }

        // Verificar la contraseña
        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            // Incrementar intentos fallidos
            usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);

            // Bloquear usuario si excede 3 intentos
            if (usuario.getIntentosFallidos() >= 3) {
                usuario.setStatus("bloqueado");
                usuarioRepository.save(usuario);
                return "Usuario bloqueado por múltiples intentos fallidos.";
            }

            usuarioRepository.save(usuario);
            return "Contraseña incorrecta. Intentos restantes: " + (3 - usuario.getIntentosFallidos());
        }

        // Restablecer intentos fallidos si la contraseña es correcta
        usuario.setIntentosFallidos(0);
        usuario.setSessionActive("1");
        usuarioRepository.save(usuario);

        // Registrar inicio de sesión
        SessionLog sessionLog = new SessionLog();
        sessionLog.setUsuario(usuario);
        sessionLog.setFechaIngreso(LocalDateTime.now());
        sessionLogRepository.save(sessionLog);


        return "Inicio de sesión exitoso.";
    }


    // Método para cerrar sesión
    public String logout(String usernameOrMail) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsernameOrMail(usernameOrMail, usernameOrMail);

        if (usuarioOpt.isEmpty()) {
            return "Usuario no encontrado.";
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.getSessionActive().equals("0")) {
            return "El usuario ya está desconectado.";
        }

        // Desactivar la sesión del usuario
        usuario.setSessionActive("0");
        usuarioRepository.save(usuario);

        // Registrar la hora de cierre de sesión en la última sesión activa
        Optional<SessionLog> lastSessionOpt = sessionLogRepository.findTopByUsuarioOrderByFechaIngresoDesc(usuario);
        if (lastSessionOpt.isPresent()) {
            SessionLog lastSession = lastSessionOpt.get();
            lastSession.setFechaCierre(LocalDateTime.now());
            sessionLogRepository.save(lastSession);
        }

        return "Sesión cerrada correctamente.";
    }
    // 🔹 MÉTODO PARA CREAR UN ADMINISTRADOR POR DEFECTO
    @PostConstruct
    @Transactional
    public void crearAdminPorDefecto() {
        // 1️⃣ Verificar si el usuario admin ya existe




        if (usuarioRepository.findByUsername("admin").isPresent()) {
            System.out.println("✅ Usuario Admin ya existe. No se crea otro.");
            return;
        }

        // 2️⃣ Crear Persona para Admin
        Persona persona = new Persona();
        persona.setIdentificacion("0000000000");
        persona.setNombres("Admin");
        persona.setApellidos("Usuario");
        persona.setFechaNacimiento(LocalDate.parse("1990-01-01"));
        persona = personaRepository.save(persona);

        // 3️⃣ Crear Usuario Admin
        Usuario admin = new Usuario();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setMail("admin@mail.com");
        admin.setSessionActive("0");
        admin.setStatus("activo");
        admin.setIntentosFallidos(0);
        admin.setPersona(persona);

        // 4️⃣ Verificar si el Rol ADMIN existe, si no, crearlo
        Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                .orElseGet(() -> {
                    Rol nuevoRol = new Rol();
                    nuevoRol.setNombre("ROLE_ADMIN");
                    return rolRepository.save(nuevoRol);
                });

        // 5️⃣ Asignar el rol al usuario (usando un Set mutable)
        Set<Rol> rolesAdmin = new HashSet<>();
        rolesAdmin.add(rolAdmin);
        admin.setRoles(rolesAdmin);

        // 6️⃣ Guardar el Usuario Admin
        usuarioRepository.save(admin);

        System.out.println("🔹 Usuario Admin creado con éxito.");
    }

}
