package com.br.ricardo.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class FirstMessage {

    /*
     * Métodos HTTP
     * GET
     * POST
     * PUT
     * PATCH
     * DELETE
     */

    @GetMapping("/")
    public String sendMessage() {
        return "Olá, mundo!";
    }

}
