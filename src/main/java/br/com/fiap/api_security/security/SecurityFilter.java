package br.com.fiap.api_security.security;

import br.com.fiap.api_security.repository.UsuarioRepository;
import br.com.fiap.api_security.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null) {
            var login = tokenService.validationToken(token);
            UserDetails userDetails = usuarioRepository.findByLogin(login);
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities() );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Autorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer","");
    }
}
