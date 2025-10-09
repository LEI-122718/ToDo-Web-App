package iscte.todoapp.tasklist.ui;

import iscte.todoapp.tasklist.Task;
import iscte.todoapp.tasklist.TaskService;
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

        // CSS: todo texto maior e título destacado
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; padding: 2em; font-size: 20px; }");
        html.append("h1 { color: #222; font-size: 48px; margin-bottom: 0.2em; }");
        html.append(".meta { color: #666; margin-bottom: 1.2em; font-size: 0.9em; }");
        html.append("ul { list-style-type: none; padding-left: 0; }");
        html.append("li { margin: 1em 0; display: flex; align-items: center; }");
        html.append(".task-text { margin-left: 0.8em; font-size: 1.05em; }");
        html.append(".done { text-decoration: line-through; color: gray; opacity: 0.8; }");
        html.append("button.clear { margin-top: 1em; padding: 6px 12px; }");
        html.append("</style>");

        html.append("</head>");
        html.append("<body>");

        // título + data do documento (topo)
        html.append("<h1>My Tasks</h1>");
        html.append("<div class='meta'>Generated on: ").append(formattedDate).append("</div>");

        html.append("<ul>");
        for (Task task : tasks) {
            // cada item tem checkbox e span com descrição
            html.append("<li>");
            html.append("<input type='checkbox' onclick='toggleDone(this)'>");
            html.append("<span class='task-text'>").append(escapeHtml(task.getDescription())).append("</span>");
            html.append("</li>");
        }
        html.append("</ul>");

        // JavaScript: interatividade para riscar/arriscar
        html.append("<script>");
        html.append("function toggleDone(checkbox) {");
        html.append("  const span = checkbox.nextElementSibling;");
        html.append("  if (!span) return;");
        html.append("  if (checkbox.checked) span.classList.add('done');");
        html.append("  else span.classList.remove('done');");
        html.append("}");
        // opcional: limpar todos
        html.append("function clearAll() { document.querySelectorAll('input[type=checkbox]').forEach(cb=> { cb.checked=false; cb.nextElementSibling.classList.remove('done'); }); }");
        html.append("</script>");

        // botão opcional limpar (se quiseres)
        html.append("<button class='clear' onclick='clearAll()'>Clear all</button>");

        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    // método simples para escapar < e > na descrição (para evitar HTML injection)
    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
