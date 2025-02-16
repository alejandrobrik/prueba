package com.example.prueba.controllers;
import com.example.prueba.models.Persona;
import com.example.prueba.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persona")
public class PersonaController {

    @Autowired
    private PersonaService personaService;

    @GetMapping()
    public List<Persona> obtenerPersonas() {
        return personaService.obtenerPersonas();
    }

    @PostMapping()
    public Persona guardarPersona(@RequestBody Persona persona) {
        return personaService.guardarPersona(persona);
    }

    @GetMapping("/{id}")
    public Persona obtenerPersonaPorId(@PathVariable int id) {
        return personaService.obtenerPersonaPorId(id);
    }

    @PutMapping("/{id}")
    public Persona actualizarPersona(@PathVariable int id, @RequestBody Persona persona) {
        return personaService.actualizarPersona(id, persona);
    }

    @DeleteMapping("/{id}")
    public String eliminarPersona(@PathVariable int id) {
        return personaService.eliminarPersona(id);
    }
}
