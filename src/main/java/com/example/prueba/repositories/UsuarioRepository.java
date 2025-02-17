package com.example.prueba.repositories;

import com.example.prueba.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // 🔹 Cargar usuario y sus roles con una consulta optimizada
    @Query("SELECT u FROM Usuario u JOIN FETCH u.roles r WHERE u.username = :username")
    Optional<Usuario> findByUsername(@Param("username") String username);

    // 🔹 Buscar usuario por correo
    boolean existsByMail(String mail);

    // 🔹 Buscar usuario por username o mail, asegurando que los roles también se carguen
    @Query("SELECT u FROM Usuario u JOIN FETCH u.roles r WHERE u.username = :username OR u.mail = :mail")
    Optional<Usuario> findByUsernameOrMail(@Param("username") String username, @Param("mail") String mail);

    // 🔹 Depuración: consulta para verificar si los roles se están cargando
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles r WHERE u.username = :username")
    Optional<Usuario> findByUsernameWithRolesDebug(@Param("username") String username);

    // 🔹 Verificar si existe un usuario con una identificación específica
    boolean existsByPersonaIdentificacion(String identificacion);

    //Buscar usuario por ID
    Optional<Usuario> findById(Integer id);


    List<Usuario> findByStatus(String status);




}
