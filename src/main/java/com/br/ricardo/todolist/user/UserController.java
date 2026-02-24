package com.br.ricardo.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public UserModel create(@RequestBody UserModel userModel) {

        var user = userRepository.findByUsername(userModel.getUsername());

        if (user != null) {
            System.out.println("Usuário já existe");
            return null;
        }

        var createdUser = this.userRepository.save(userModel);
        return createdUser;
    }
}
