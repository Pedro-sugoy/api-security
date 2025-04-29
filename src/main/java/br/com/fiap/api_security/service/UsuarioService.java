package br.com.fiap.api_security.service;

import br.com.fiap.api_security.entity.Usuario;
import br.com.fiap.api_security.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    @CachePut(value = "usuarios", key = "#result.id")
    public Usuario ceateUsuario(Usuario usuario){
        return  usuarioRepository.save(usuario);
    }

    @Cacheable(value = "usuarios", key = "#id")
    public Usuario readusuarioById(UUID id){
        return usuarioRepository.findById(id).orElse(null);
    }

    @Cacheable(value = "usuarios", key = "'todos")
    public List<Usuario> readUsuario() {
        return usuarioRepository.findAll();
    }

    @Transactional
    @CachePut(value = "usuarios", key = "#result.id")
    public Usuario updateUsuario(UUID id, Usuario usuario){
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isEmpty()) {
            return null;
        }
        usuario.setId(id);
        return usuarioRepository.save(usuario);
    }

    @CacheEvict(value = "usuarios", key = "result.id")
    public void deleteUsuario(UUID id){
        usuarioRepository.deleteById(id);
        limpatCacheTodosUsuarios();
    }
    @CacheEvict(value = "usuarios", key = "'todos")
    public void limpatCacheTodosUsuarios() {}

    @CacheEvict(value = "usuarios", allEntries = true)
    public void limpatTodoCacheUsuarios() {}
}
