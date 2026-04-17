package ateneu.sgcti.shared.config;

import ateneu.sgcti.auth.entity.UsuarioEntity;
import ateneu.sgcti.auth.enums.Role;
import ateneu.sgcti.auth.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String @NonNull ... args) {
        if (usuarioRepository.findByEmail(adminEmail).isEmpty()) {
            UsuarioEntity admin = new UsuarioEntity();
            admin.setNome("SGCTI-ROOT");
            admin.setEmail(adminEmail);
            admin.setSenha(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            usuarioRepository.save(admin);
            log.info("Administrador criado com e-mail: {}", adminEmail);
        } else {
            log.info("Administrador já existe");
        }
    }
}
