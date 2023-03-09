package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static hexlet.code.controller.TaskController.TASK_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;


@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + TASK_CONTROLLER_PATH)
public class TaskController {

    public static final String ID = "/{id}";
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    private static final String TASK_OWNER =
            "@taskRepository.findById(#id).get().getAuthor().getEmail() == authentication.getName()";

    private final TaskService taskService;

    @Operation(summary = "Create new task")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The task is created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @PostMapping
    @ResponseStatus(CREATED)
    public Task createNewTask(@RequestBody @Valid final TaskDto taskDto) {
        return taskService.createNewTask(taskDto);
    }

    @Operation(summary = "Get all tasks")
    @ApiResponse(responseCode = "200", description = "The tasks are found",
            content = @Content(schema = @Schema(implementation = Task.class)))
    @GetMapping
    public Iterable<Task> getAllTasks(@QuerydslPredicate final Predicate predicate) {
        return predicate == null ? taskService.getAllTasks() : taskService.getAllTasks(predicate);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task  is found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "404", description = "The task is not found",
                    content = @Content)})
    @GetMapping(ID)
    @Operation(summary = "Get task")
    public Task getTaskById(@PathVariable final Long id) {
        return taskService.getTaskById(id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task  is updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content)})
    @PutMapping(ID)
    @Operation(summary = "Update task")
    public Task updateTask(@PathVariable final long id,
                           @RequestBody @Valid final TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task is deleted"),
            @ApiResponse(responseCode = "404", description = "The task is not found",
                    content = @Content)})
    @DeleteMapping(ID)
    @Operation(summary = "Delete task")
    @PreAuthorize(TASK_OWNER)
    public void deleteTask(@PathVariable final long id) {
        taskService.deleteTask(id);
    }

}
