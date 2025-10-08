package iscte.todoapp.tasklist;

import jakarta.validation.constraints.Email;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final EmailService emailService;

    TaskService(TaskRepository taskRepository, EmailService emailService) {

        this.taskRepository = taskRepository;
        this.emailService= emailService;
    }

    @Transactional
    public void createTask(String description, @Nullable LocalDate dueDate, String userEmail) {
        if ("fail".equals(description)) {
            throw new RuntimeException("This is for testing the error handler");
        }
        var task = new Task(description, Instant.now());
        task.setDueDate(dueDate);
        taskRepository.saveAndFlush(task);


        // --- Envio de email ---
        String subject = "Tarefa Criada: " + description;
        String body = "Olá!\n\n" +
                "A seguinte ação foi realizada na sua tarefa:\n\n" +
                "Título: " + description + "\n" +
                "Data: " + Instant.now() + "\n" +
                "Ação: Criada\n\n" +
                "Obrigado por usar a aplicação!";

        emailService.sendTaskNotification(userEmail, subject, body);


    }

    @Transactional(readOnly = true)
    public List<Task> list(Pageable pageable) {
        return taskRepository.findAllBy(pageable).filter(Task::isShown).toList();
    }

    @Transactional
    public void searchTask(String query) {
        List<Task> tasks = taskRepository.findAll();
        tasks.forEach(task -> task.setShown(false));
        tasks.stream().filter(task -> query.isEmpty() || FuzzySearch.ratio(query, task.getDescription()) > 30).forEach(task -> task.setShown(true));
        taskRepository.saveAll(tasks);
    }

    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        // devolve tudo (ou aplica filtros que aches que faz sentido, ex: only shown)
        return taskRepository.findAll().stream()
                .filter(Task::isShown) // se quiseres apenas as visíveis (opcional)
                .toList();
    }

    @Transactional
    public void completeTask(Task task, String userEmail) {
        // Aqui só faz sentido enviar email
        String subject = "Tarefa Concluída: " + task.getDescription();
        String body = "Ação: Concluída em " + Instant.now();
        emailService.sendTaskNotification(userEmail, subject, body);
    }


}
