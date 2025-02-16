package com.example.prueba.utils;

import com.example.prueba.models.Rol;
import com.example.prueba.models.Usuario;
import com.example.prueba.repositories.UsuarioRepository;
import org.hibernate.Hibernate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Servicio de autenticación basado en la base de datos
/*    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsernameOrMail(username, username);
            if (usuarioOpt.isEmpty()) {
                throw new UsernameNotFoundException("Usuario no encontrado.");
            }
            Usuario usuario = usuarioOpt.get();
            return User.withUsername(usuario.getUsername())
                    .password(usuario.getPassword())
                    .roles(usuario.getRoles().stream().map(rol -> rol.getNombre().replace("ROLE_", "")).toArray(String[]::new))
                    .build();
        };
    }*/


    @Bean
    public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
        return username -> {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsernameOrMail(username, username);
            if (usuarioOpt.isEmpty()) {
                throw new UsernameNotFoundException("Usuario no encontrado.");
            }
            Usuario usuario = usuarioOpt.get();

            // 🔹 Imprimir roles del usuario antes de autenticación
/*            System.out.println("Usuario autenticado: " + usuario.getUsername());
            System.out.println("Roles asignados: " + usuario.getRoles());*/

            // 🔹 FORZAR CARGA DE ROLES
            Hibernate.initialize(usuario.getRoles()); // 💡 IMPORTANTE: Esto inicializa la colección de roles
            Set<Rol> roles = usuario.getRoles();

            // 🔹 IMPRIMIR ROLES PARA DEPURACIÓN
            System.out.println("Usuario autenticado: " + usuario.getUsername());
            System.out.println("Roles asignados: " + roles);

            // 🔹 IMPRIMIR ROLES PARA DEPURACIÓN
            usuario.getRoles().forEach(rol -> System.out.println(" - " + rol.getNombre()));

            return User.withUsername(usuario.getUsername())
                    .password(usuario.getPassword())
                    .authorities(usuario.getRoles().stream()
                            .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                            .collect(Collectors.toSet()))
                    .build();
        };
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF para pruebas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/register", "/auth/logout").permitAll() // Público
                        .requestMatchers("/session-log/**").hasAuthority("ROLE_ADMIN") // Solo ADMIN puede acceder
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.realmName("MyApp")); // Configura Basic Auth

        return http.build();
    }
}








/*

package com.example.prueba.utils;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactiva CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // PERMITE TODO TEMPORALMENTE
                );

        return http.build();
    }

}

*/
