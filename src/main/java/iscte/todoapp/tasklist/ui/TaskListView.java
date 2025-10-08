package iscte.todoapp.tasklist.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import iscte.todoapp.base.ui.component.ViewToolbar;
import iscte.todoapp.tasklist.PDFService;
import iscte.todoapp.tasklist.Task;
import iscte.todoapp.tasklist.TaskService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.io.ByteArrayInputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Collection;
import java.util.Optional;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.streams.DownloadEvent;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;


import static com.vaadin.flow.spring.data.VaadinSpringDataHelpers.toSpringPageRequest;

@Route("")
@PageTitle("Task List")
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "Task List")
class TaskListView extends Main {

    private final TaskService taskService;
    private final PDFService pdfService;

    final TextField description;
    final TextField searchField;
    final DatePicker dueDate;
    final Button createBtn;
    final Grid<Task> taskGrid;

    TaskListView(TaskService taskService, PDFService pdfService) {
        this.taskService = taskService;
        this.pdfService = pdfService;

        description = new TextField();
        description.setPlaceholder("What do you want to do?");
        description.setAriaLabel("Task description");
        description.setMaxLength(Task.DESCRIPTION_MAX_LENGTH);
        description.setMinWidth("20em");

        searchField = new TextField();
        searchField.setPlaceholder("Search");
        searchField.setAriaLabel("Search");
        searchField.setMaxLength(Task.DESCRIPTION_MAX_LENGTH);
        searchField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        searchField.setMinWidth("10em");
        searchField.addValueChangeListener(event -> {
            if (event.isFromClient()) {
               searchTasks(searchField.getValue());
            }
        });

        dueDate = new DatePicker();
        dueDate.setPlaceholder("Due date");
        dueDate.setAriaLabel("Due date");

        createBtn = new Button("Create", event -> createTask());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Botão e link para exportar PDF
        var exportBtn = new Button("Exportar para PDF");

// Cria um link de download que gera o PDF dinamicamente
        Anchor downloadAnchor = new Anchor((DownloadEvent event) -> {
            String nomeUtilizador = "Utilizador Exemplo"; // podes substituir pelo nome real do user

            // Obter todas as tarefas
            List<String> tarefas = taskService.getAllTasks().stream()
                    .map(Task::getDescription)
                    .toList();

            // Configurar resposta HTTP
            event.setFileName("relatorio_tarefas.pdf");
            event.getResponse().setHeader("Content-Type", "application/pdf");

            // Escrever o PDF diretamente no output
            try (OutputStream os = event.getOutputStream();
                 InputStream in = pdfService.gerarRelatorio(nomeUtilizador, tarefas)) {
                in.transferTo(os);
            } catch (IOException ex) {
                throw new RuntimeException("Erro ao gerar PDF", ex);
            }
        }, "");

// Adiciona o botão visual dentro do link de download
        downloadAnchor.add(exportBtn);


        var dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(getLocale())
                .withZone(ZoneId.systemDefault());
        var dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(getLocale());

        taskGrid = new Grid<>();
        taskGrid.setItems(query -> taskService.list(toSpringPageRequest(query)).stream());
        taskGrid.addColumn(Task::getDescription).setHeader("Description");
        taskGrid.addColumn(task -> Optional.ofNullable(task.getDueDate()).map(dateFormatter::format).orElse("Never"))
                .setHeader("Due Date");
        taskGrid.addColumn(task -> dateTimeFormatter.format(task.getCreationDate())).setHeader("Creation Date");
        taskGrid.setSizeFull();

        setSizeFull();
        addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Padding.MEDIUM, LumoUtility.Gap.SMALL);

        add(new ViewToolbar("Task List",
                ViewToolbar.group(description, dueDate, createBtn, searchField, downloadAnchor)));

        add(taskGrid);
    }

    private void createTask() {
        if (description.isEmpty()) {
            Notification.show("Description is required", 3000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        taskService.createTask(description.getValue(), dueDate.getValue());
        taskGrid.getDataProvider().refreshAll();
        description.clear();
        dueDate.clear();
        Notification.show("Task added", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

   private void searchTasks(String value) {
        taskService.searchTask(value.trim());
        taskGrid.getDataProvider().refreshAll();
       Notification.show("Searched by " + value, 3000, Notification.Position.BOTTOM_END)
               .addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    }

}
