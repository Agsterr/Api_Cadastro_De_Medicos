package med.voll.api.infra.security;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecuryFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService service;

    @Autowired
    private UsuarioRepository repository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       var tokenJWT = recuperarToken(request);
// só quando esta vindo o cabeçalho se não segue o fluxo
       if (tokenJWT != null) {
           //validando token
           var subject = service.getSubject(tokenJWT);
//autenticação forçada
           var usuario = repository.findByLogin(subject);
           //classe do spring para autenticar
          var authentication = new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());
           SecurityContextHolder.getContext().setAuthentication(authentication);

       }


        filterChain.doFilter(request,response);
    }


    //verificar se não é nulo
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {

            return authorizationHeader.replace("Bearer", "");
        }
        return null;
    }
}
