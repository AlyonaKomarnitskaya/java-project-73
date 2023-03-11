package hexlet.code.service;

import com.querydsl.core.types.Predicate;
import hexlet.code.dto.TaskDto;
import hexlet.code.exception.InvalidElementException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskStatusService taskStatusService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final LabelService labelService;
    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getAllTasks(Predicate predicate) {
        return StreamSupport
                .stream(taskRepository.findAll(predicate).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Task getTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> InvalidElementException.invalidElement("Task not found"));
    }

    @Override
    public void deleteTask(long id) {
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }

    @Override
    public Task createNewTask(TaskDto dto) {
        User executor = null;
        if (Objects.nonNull(dto.getExecutorId())) {
            executor = userRepository
                    .findById(dto.getExecutorId())
                    .orElseThrow(() -> InvalidElementException.invalidElement("Executor not found"));
        }

        List<Label> labels = null;
        if (Objects.nonNull(dto.getLabelIds()) && dto.getLabelIds().size() > 0) {
            labels = dto.getLabelIds().stream()
                    .map(labelService::getLabelById)
                    .collect(Collectors.toList());
        }

        Task newTask = Task.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .taskStatus(taskStatusService.getTaskStatusById(dto.getTaskStatusId()))
                .author(userService.getCurrentUser())
                .labels(labels)
                .executor(executor).build();

        return taskRepository.save(newTask);
    }


    @Override
    public Task updateTask(long id, TaskDto dto) {
        Task task = getTaskById(id);
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());

        if (Objects.nonNull(dto.getExecutorId())) {
            User executor = userRepository
                    .findById(dto.getExecutorId())
                    .orElseThrow(() -> InvalidElementException.invalidElement("Executor not found"));
            task.setExecutor(executor);
        }

        TaskStatus taskStatus = taskStatusService.getTaskStatusById(
                dto.getTaskStatusId()
        );
        task.setTaskStatus(taskStatus);

        if (Objects.nonNull(dto.getLabelIds()) && dto.getLabelIds().size() > 0) {
            List<Label> labels = dto.getLabelIds().stream()
                    .map(labelService::getLabelById)
                    .collect(Collectors.toList());
            task.setLabels(labels);
        }
        return taskRepository.save(task);
    }
}
