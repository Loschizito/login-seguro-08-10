package com.loginseguro.service;

import com.loginseguro.dto.CadastroDto;
import com.loginseguro.model.Role;
import com.loginseguro.model.Usuario;
import com.loginseguro.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public void cadastrar(CadastroDto dto) {
        String email = dto.getEmail().trim().toLowerCase();

        if (!dto.getSenha().equals(dto.getConfirmarSenha())) {
            throw new IllegalArgumentException("As senhas não conferem");
        }
        if (repository.existsByEmail(email)) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        Usuario usuario = new Usuario(dto.getNome().trim(), email, passwordEncoder.encode(dto.getSenha()), Role.USUARIO);
        repository.save(usuario);
    }

    public void criarAdminSeNaoExistir(String email, String senha) {
        if (!repository.existsByEmail(email)) {
            repository.save(new Usuario("Administrador", email, passwordEncoder.encode(senha), Role.ADMIN));
        }
    }

    public Usuario buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    public List<Usuario> listar() {
        return repository.findAll();
    }

    public void alterarRole(String id, Role role) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        usuario.setRole(role);
        repository.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = buscarPorEmail(email.trim().toLowerCase());
        return User.withUsername(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(usuario.getRole().name())
                .build();
    }
}
