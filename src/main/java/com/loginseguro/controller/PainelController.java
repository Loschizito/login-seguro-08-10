package com.loginseguro.controller;

import com.loginseguro.model.Role;
import com.loginseguro.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PainelController {

    private final UsuarioService usuarioService;

    public PainelController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/painel")
    public String painel(Authentication auth, Model model) {
        model.addAttribute("usuario", usuarioService.buscarPorEmail(auth.getName()));
        return "painel";
    }

    @GetMapping("/moderador")
    public String moderador(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        return "moderador";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("usuarios", usuarioService.listar());
        model.addAttribute("roles", Role.values());
        return "admin";
    }

    @PostMapping("/admin/usuarios/{id}/role")
    public String alterarRole(@PathVariable String id, @RequestParam Role role) {
        usuarioService.alterarRole(id, role);
        return "redirect:/admin";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "acesso-negado";
    }
}
