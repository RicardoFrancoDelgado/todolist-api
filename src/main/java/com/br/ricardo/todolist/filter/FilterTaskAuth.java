package com.br.ricardo.todolist.filter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.br.ricardo.todolist.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/task/")) {

            // Permite requisições OPTIONS (preflight do CORS) passarem sem autenticação
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                filterChain.doFilter(request, response);
                return;
            }

            var authorizationHeader = request.getHeader("Authorization");

            // Retorna 401 se o header Authorization estiver ausente
            if (authorizationHeader == null || !authorizationHeader.startsWith("Basic ")) {
                response.sendError(401, "Authorization header ausente ou inválido");
                return;
            }

            var baseAuthorization = authorizationHeader.substring("Basic".length()).trim();

            byte[] baseEncoded = Base64.getDecoder().decode(baseAuthorization);

            var baseDecoded = new String(baseEncoded);

            String[] credentials = baseDecoded.split(":");

            if (credentials.length < 2) {
                response.sendError(401, "Credenciais inválidas");
                return;
            }

            String username = credentials[0];
            String password = credentials[1];

            var user = this.userRepository.findByUsername(username);

            if (user == null) {
                response.sendError(401, "Usuário não encontrado");
            } else {
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified) {
                    request.setAttribute("userId", user.getId());
                    filterChain.doFilter(request, response);
                } else {
                    response.sendError(401, "Senha incorreta");
                }
            }

        } else {
            filterChain.doFilter(request, response);
        }
    }
}