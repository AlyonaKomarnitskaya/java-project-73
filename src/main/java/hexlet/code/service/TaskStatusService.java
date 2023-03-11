package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.exception.InvalidElementException;
import hexlet.code.model.TaskStatus;

import java.util.List;

public interface TaskStatusService {

    List<TaskStatus> getAllTaskStatuses();
    TaskStatus getTaskStatusById(long id);
    TaskStatus createTaskStatus(TaskStatusDto taskStatusDto);
    TaskStatus updateTaskStatus(TaskStatusDto taskStatusDto, long id) throws InvalidElementException;
    void deleteTaskStatus(long id);
}
