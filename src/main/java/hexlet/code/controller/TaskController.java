package hexlet.code.controller;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    private final TaskRepository taskRepository;

    @Operation(summary = "Create new task")
    @ApiResponse(responseCode = "201", description = "Task created")
    @PostMapping
    @ResponseStatus(CREATED)
    public Task createNewTask(@RequestBody @Valid final TaskDto taskDto) {
        return taskService.createNewTask(taskDto);
    }

    @Operation(summary = "Get all tasks")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema =
    @Schema(implementation = Task.class))
    ))
    @GetMapping
    public Iterable<Task> getAllTasks(@QuerydslPredicate final Predicate predicate) {
        return predicate == null ? taskRepository.findAll() : taskRepository.findAll(predicate);
    }

    @ApiResponses(@ApiResponse(responseCode = "200"))
    @GetMapping(ID)
    @Operation(summary = "Get task")
    public Task geTaskById(@PathVariable final Long id) {
        return taskRepository.findById(id).get();
    }

    @PutMapping(ID)
    @Operation(summary = "Update task")
    public Task updateTask(@PathVariable final long id,
                           @RequestBody @Valid final TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @DeleteMapping(ID)
    @Operation(summary = "Delete task")
    @PreAuthorize(TASK_OWNER)
    public void deleteTask(@PathVariable final long id) {
        taskRepository.deleteById(id);
    }

}
