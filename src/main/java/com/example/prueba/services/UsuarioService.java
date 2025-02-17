package com.example.prueba.services;

import com.example.prueba.models.Rol;
import com.example.prueba.models.Usuario;
import com.example.prueba.models.Persona;
import com.example.prueba.repositories.RolRepository;
import com.example.prueba.repositories.UsuarioRepository;
import com.example.prueba.repositories.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private RolRepository rolRepository;



    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public Usuario registrarUsuario(Usuario usuario) {
        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Asignar rol por defecto si no tiene uno
        if (usuario.getRoles().isEmpty()) {
            Optional<Rol> rolOpt = rolRepository.findByNombre("ROLE_USER");
            rolOpt.ifPresent(rol -> usuario.setRoles(Collections.singleton(rol)));
        }

        return usuarioRepository.save(usuario);
    }


    // Obtener todos los usuarios
    public ArrayList<Usuario> obtenerUsuarios() {
        return (ArrayList<Usuario>) usuarioRepository.findAll();
    }

    // Guardar usuario buscando la persona por cédula y generando su correo
    public Usuario guardarUsuario(Usuario usuario, String identificacion) {
        String identificacionLimpia = identificacion.trim();
        Optional<Persona> personaOpt = personaRepository.findByIdentificacion(identificacionLimpia);

        if (personaOpt.isEmpty()) {
            System.out.println("Persona con cédula '" + identificacionLimpia + "' no encontrada en la base de datos.");
            throw new RuntimeException("Error: Persona con cédula '" + identificacionLimpia + "' no encontrada en la base de datos.");
        }

        Persona persona = personaOpt.get();
        usuario.setPersona(persona);

        // Generar el correo automáticamente
        String correoGenerado = generarCorreo(persona.getNombres(), persona.getApellidos());

        // Verificar si el correo ya existe y agregar un número si es necesario
        int contador = 0;
        String correoFinal = correoGenerado;
        while (usuarioRepository.existsByMail(correoFinal)) {
            contador++;
            correoFinal = correoGenerado.replace("@mail.com", contador + "@mail.com");
        }
        // Encriptar contraseña antes de guardarla
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setMail(correoFinal);
        return usuarioRepository.save(usuario);
    }

    // Método para generar el correo a partir del nombre y apellido
    private String generarCorreo(String nombres, String apellidos) {
        String primeraLetra = nombres.split(" ")[0].substring(0, 1).toLowerCase();
        String primerApellido = apellidos.split(" ")[0].toLowerCase();
        primerApellido = Normalizer.normalize(primerApellido, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        return primeraLetra + primerApellido + "@mail.com";
    }

    public Usuario actualizarUsuario(Integer id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar si la persona ya existe en la base de datos
        if (usuarioActualizado.getPersona() != null) {
            Optional<Persona> personaExistenteOpt = personaRepository.findByIdentificacion(usuarioActualizado.getPersona().getIdentificacion());

            Persona personaExistente = personaExistenteOpt.orElseGet(() -> personaRepository.save(usuarioActualizado.getPersona()));

            usuarioExistente.setPersona(personaExistente);

            // 🔹 Generar correo solo si la persona ha cambiado
            if (!usuarioExistente.getMail().contains("@mail.com") ||
                    !usuarioExistente.getPersona().getIdentificacion().equals(personaExistente.getIdentificacion())) {

                String correoGenerado = generarCorreo(personaExistente.getNombres(), personaExistente.getApellidos());

                // 🔹 Verificar si el correo ya existe y evitar duplicados
                int contador = 0;
                String correoFinal = correoGenerado;
                while (usuarioRepository.existsByMail(correoFinal)) {
                    contador++;
                    correoFinal = correoGenerado.replace("@mail.com", contador + "@mail.com");
                }

                usuarioExistente.setMail(correoFinal);
            }
        }

        // 🔹 Mantener `username` y `mail` si no se envían en la actualización
        if (usuarioActualizado.getUsername() != null && !usuarioActualizado.getUsername().isEmpty()) {
            usuarioExistente.setUsername(usuarioActualizado.getUsername());
        }

        if (usuarioActualizado.getMail() != null && !usuarioActualizado.getMail().isEmpty()) {
            usuarioExistente.setMail(usuarioActualizado.getMail());
        }

        // 🔹 Mantener la contraseña actual si no se envía una nueva
        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }

        // 🔹 Mantener el estado del usuario si no se envía
        if (usuarioActualizado.getStatus() != null) {
            usuarioExistente.setStatus(usuarioActualizado.getStatus());
        }

        // 🔹 Asegurar que la sesión no se active incorrectamente
        usuarioExistente.setSessionActive("0");

        return usuarioRepository.save(usuarioExistente);
    }



/*    public Usuario actualizarUsuario(Integer id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar si la persona ya existe en la base de datos
        if (usuarioActualizado.getPersona() != null) {
            Optional<Persona> personaExistenteOpt = personaRepository.findByIdentificacion(usuarioActualizado.getPersona().getIdentificacion());

            Persona personaExistente;
            if (personaExistenteOpt.isPresent()) {
                personaExistente = personaExistenteOpt.get();
            } else {
                // Si no existe, guardarla primero en la BD
                personaExistente = personaRepository.save(usuarioActualizado.getPersona());
            }

            usuarioExistente.setPersona(personaExistente);

            // 🔹 Generar el correo basado en el nombre y apellido
            String correoGenerado = generarCorreo(personaExistente.getNombres(), personaExistente.getApellidos());

            // 🔹 Verificar si el correo ya existe y evitar duplicados
            int contador = 0;
            String correoFinal = correoGenerado;
            while (usuarioRepository.existsByMail(correoFinal)) {
                contador++;
                correoFinal = correoGenerado.replace("@mail.com", contador + "@mail.com");
            }

            usuarioExistente.setMail(correoFinal);
        }

        // Actualizar datos del usuario
        usuarioExistente.setUsername(usuarioActualizado.getUsername());

        // Mantener la contraseña actual si no se envía una nueva
        if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioActualizado.getPassword()));
        }

        usuarioExistente.setSessionActive(usuarioActualizado.getSessionActive());
        usuarioExistente.setStatus(usuarioActualizado.getStatus());

        return usuarioRepository.save(usuarioExistente);
    }*/

    //Logica para eliminar un usuario
    public boolean eliminarUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            usuario.setStatus("INACTIVO"); // Cambiar el estado a INACTIVO
            usuarioRepository.save(usuario); // Guardar cambios en la BD
            return true;
        }
        return false; // Retorna false si el usuario no existe
    }

    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioRepository.findByStatus("ACTIVO"); // Solo listar usuarios activos
    }






/*    public Usuario actualizarUsuario(Integer id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar datos del usuario
        usuarioExistente.setUsername(usuarioActualizado.getUsername());
        usuarioExistente.setPassword(usuarioActualizado.getPassword());
        usuarioExistente.setMail(usuarioActualizado.getMail());
        usuarioExistente.setSessionActive(usuarioActualizado.getSessionActive());
        usuarioExistente.setStatus(usuarioActualizado.getStatus());

        // Actualizar persona asociada si no es nula
        if (usuarioActualizado.getPersona() != null) {
            usuarioExistente.setPersona(usuarioActualizado.getPersona());
        }

        return usuarioRepository.save(usuarioExistente);
    }*/

}
