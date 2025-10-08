package iscte.todoapp.tasklist.ui;

import iscte.todoapp.tasklist.TaskService;
import iscte.todoapp.tasklist.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;

@RestController
public class TasksHtmlController {

    private final TaskService taskService;

    public TasksHtmlController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks-html")
    public String getTasksPage() {
        List<Task> tasks = taskService.getAllTasks();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = now.format(formatter);

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html lang='en'>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        html.append("<title>My Tasks</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; padding: 2em; font-size: 20px; }"); // texto maior
        html.append("h1 { color: #333; font-size: 48px; margin-bottom: 1em; }"); // título maior
        html.append("ul { list-style-type: none; padding-left: 0; }");
        html.append("li { margin: 1em 0; }");
        html.append(".done { text-decoration: line-through; color: gray; }");
        html.append(".task-text { margin-left: 0.5em; }"); // espaço entre checkbox e texto
        html.append(".task-date { color: #555; font-size: 0.9em; margin-left: 0.5em; }"); // estilo da data
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<h1>Estas são as tuas tarefas:</h1>");
        html.append("<p>Geradas em: ").append(formattedDate).append("</p>");
        html.append("<ul>");

        for (Task task : tasks) {
            html.append("<li>");
            html.append("<input type='checkbox' onclick='toggleDone(this)'> ");
            html.append("<span class='task-text'>").append(task.getDescription()).append("</span>");
            // Adiciona a data (creationDate) no formato padrão

        }


        html.append("</ul>");

        html.append("<script>");
        html.append("function toggleDone(checkbox) {");
        html.append("  const span = checkbox.nextElementSibling;");
        html.append("  if(checkbox.checked) { span.classList.add('done'); }");
        html.append("  else { span.classList.remove('done'); }");
        html.append("}");
        html.append("</script>");

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }


}
