package com.br.ricardo.todolist.task;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var idUser = request.getAttribute("userId");
        taskModel.setUserId((UUID) idUser);
        var task = this.taskRepository.save(taskModel);

        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início / data de término devem ser maiores que a data atual");
        }

        if (task.getStartAt().isAfter(task.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início não pode maior que a data de término");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("userId");
        var tasks = this.taskRepository.findByUserId((UUID) idUser);
        return tasks;
    }
}
