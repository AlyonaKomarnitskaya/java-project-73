package hexlet.code.service;

import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;

import java.util.List;

public interface TaskStatusService {

    List<TaskStatus> getAllTaskStatuses();
    TaskStatus getTaskStatusById(long id);
    TaskStatus createTaskStatus(TaskStatusDto taskStatusDto);
    TaskStatus updateTaskStatus(TaskStatusDto taskStatusDto, long id);
    void deleteTaskStatus(long id);
}
