package com.loginseguro.config;

import com.loginseguro.service.UsuarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInicial implements CommandLineRunner {

    private final UsuarioService usuarioService;

    @Value("${app.admin.email}")
    private String email;

    @Value("${app.admin.senha}")
    private String senha;

    public AdminInicial(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void run(String... args) {
        usuarioService.criarAdminSeNaoExistir(email.toLowerCase(), senha);
    }
}
