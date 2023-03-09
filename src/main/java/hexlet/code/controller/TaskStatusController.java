package hexlet.code.controller;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
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
import java.util.List;

import static hexlet.code.controller.TaskStatusController.STATUS_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + STATUS_CONTROLLER_PATH)
public class TaskStatusController {

    public static final String STATUS_CONTROLLER_PATH = "/statuses";
    public static final String ID = "/{id}";

    private final TaskStatusService taskStatusService;

    @Operation(summary = "Create new task status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The task status is created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskStatus.class))})})
    @PostMapping
    @ResponseStatus(CREATED)
    public TaskStatus createNewTaskStatus(@RequestBody @Valid final TaskStatusDto taskStatusDto) {
        return taskStatusService.createTaskStatus(taskStatusDto);
    }

    @ApiResponse(responseCode = "200", description = "The task statuses are found",
            content = @Content(schema = @Schema(implementation = TaskStatus.class)))
    @GetMapping
    @Operation(summary = "Get all task statuses")
    public List<TaskStatus> getAll() throws Exception {
        return taskStatusService.getAllTaskStatuses()
                .stream()
                .toList();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task status is found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskStatus.class))}),
            @ApiResponse(responseCode = "404", description = "The task status is not found",
                    content = @Content)})
    @GetMapping(ID)
    @Operation(summary = "Get task status")
    public TaskStatus getTaskStatusById(@PathVariable final Long id) {
        return taskStatusService.getTaskStatusById(id);
    }

    @Operation(summary = "Update task status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The task status is updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TaskStatus.class))}),
            @ApiResponse(responseCode = "404", description = "The task status is not found",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid request",
                    content = @Content)})
    @PutMapping(ID)
    public TaskStatus updateStatus(@RequestBody @Valid final TaskStatusDto dto,
                                   @PathVariable long id) {
        return taskStatusService.updateTaskStatus(dto, id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task status has been deleted"),
            @ApiResponse(responseCode = "404", description = "Task status with this id wasn`t found")
    })
    @DeleteMapping(ID)
    @Operation(summary = "Delete task status")
    public void deleteTaskStatus(@PathVariable final long id) throws Exception {
        taskStatusService.deleteTaskStatus(id);
    }
}
