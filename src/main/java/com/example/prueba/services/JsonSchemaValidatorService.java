package com.example.prueba.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Set;

@Service
public class JsonSchemaValidatorService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonSchema schema;

    public JsonSchemaValidatorService() {
        try {
            // Cargar el esquema JSON desde resources
            InputStream schemaStream = getClass().getClassLoader().getResourceAsStream("user-schema.json");

            if (schemaStream == null) {
                throw new RuntimeException("❌ No se encontró el esquema JSON en 'src/main/resources/user-schema.json'.");
            }

            // Especificar la versión Draft-07 de JSON Schema
            JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
            this.schema = schemaFactory.getSchema(schemaStream);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar el esquema JSON: " + e.getMessage(), e);
        }
    }

    public Set<ValidationMessage> validateJson(String jsonString) {
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return schema.validate(jsonNode);
        } catch (Exception e) {
            throw new RuntimeException("Error en la validación del JSON: " + e.getMessage(), e);
        }
    }
}
