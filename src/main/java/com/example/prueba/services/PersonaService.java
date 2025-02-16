package com.example.prueba.services;

import com.example.prueba.models.Persona;
import com.example.prueba.repositories.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    public List<Persona> obtenerPersonas() {
        return personaRepository.findAll();
    }

    public Persona guardarPersona(Persona persona) {
        return personaRepository.save(persona);
    }

    public Persona obtenerPersonaPorId(int id) {
        Optional<Persona> persona = personaRepository.findById(id);
        return persona.orElse(null);
    }

    public Persona obtenerPersonaPorIdentificacion(String identificacion) {
        Optional<Persona> persona = personaRepository.findByIdentificacion(identificacion);
        return persona.orElse(null);
    }

    public Persona actualizarPersona(int id, Persona personaActualizada) {
        return personaRepository.findById(id).map(persona -> {
            persona.setNombres(personaActualizada.getNombres());
            persona.setApellidos(personaActualizada.getApellidos());
            persona.setIdentificacion(personaActualizada.getIdentificacion());
            return personaRepository.save(persona);
        }).orElse(null);
    }

    public String eliminarPersona(int id) {
        if (personaRepository.existsById(id)) {
            personaRepository.deleteById(id);
            return "Persona eliminada correctamente";
        } else {
            return "Persona no encontrada";
        }
    }
}
