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
}
