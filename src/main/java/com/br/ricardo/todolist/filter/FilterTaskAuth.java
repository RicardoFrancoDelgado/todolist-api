package com.br.ricardo.todolist.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = request.getHeader("Authorization");

        var baseAuthorization = authorizationHeader.substring("Basic".length()).trim();

        byte[] baseEncoded = Base64.getDecoder().decode(baseAuthorization);

        var baseDecoded = new String(baseEncoded);

        String[] credentials = baseDecoded.split(":");
        String username = credentials[0];
        String password = credentials[1];

        System.out.println("Authorization header");
        System.out.println(username);
        System.out.println(password);

        filterChain.doFilter(request, response);
    }
}
