package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfig;
import hexlet.code.dto.TaskStatusDto;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.utils.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static hexlet.code.config.SpringConfig.TEST_PROFILE;
import static hexlet.code.controller.UserController.ID;
import static hexlet.code.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ActiveProfiles(TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfig.class)
public class TaskStatusControllerTest {

    public static final String STATUS_CONTROLLER_PATH = "/statuses";

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TestUtils utils;

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void registration() throws Exception {
        assertEquals(0, taskStatusRepository.count());
        utils.regDefaultUser().andExpect(status().isCreated());
        assertEquals(1, taskStatusRepository.count());
    }

    @Test
    public void getStatusById() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultStatus(TestUtils.TEST_USERNAME);
        final TaskStatus expectedStatus = taskStatusRepository.findAll().get(0);
        final var response = utils.perform(
                        get(BASE_URL + STATUS_CONTROLLER_PATH + ID,
                                expectedStatus.getId()), TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final TaskStatus taskStatus = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedStatus.getId(), taskStatus.getId());
        assertEquals(expectedStatus.getName(), taskStatus.getName());
    }

    @Test
    public void getStatusByIdFails() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultStatus(TEST_USERNAME);
        final TaskStatus expectedStatus = taskStatusRepository.findAll().get(0);
        Exception exception = assertThrows(
                Exception.class, () -> utils.perform(get(BASE_URL + STATUS_CONTROLLER_PATH + ID,
                        expectedStatus.getId()))
        );
        String message = exception.getMessage();
        assertTrue(message.contains("No value present"));

    }

    @Test
    public void getAllStatuses() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultStatus(TEST_USERNAME);
        final var response = utils.perform(get(BASE_URL + STATUS_CONTROLLER_PATH), TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<TaskStatus> taskStatuses = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(taskStatuses).hasSize(1);
    }

    @Test
    public void twiceRegTheSameStatusFail() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultStatus(TEST_USERNAME).andExpect(status().isCreated());
        utils.regDefaultStatus(TEST_USERNAME).andExpect(status().isBadRequest());

        assertEquals(1, taskStatusRepository.count());
    }

    @Test
    public void updateStatus() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultStatus(TEST_USERNAME);
        final Long statusId = taskStatusRepository.findAll().get(0).getId();
        final var statusDto = new TaskStatusDto(TEST_USERNAME_2);
        final var updateRequest = put(BASE_URL + STATUS_CONTROLLER_PATH + ID, statusId)
                .content(asJson(statusDto))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());
        assertTrue(taskStatusRepository.existsById(statusId));
        assertNull(taskStatusRepository.findByName(TEST_STATUS_NAME).orElse(null));
        assertNotNull(taskStatusRepository.findByName(TEST_STATUS_NAME_2).orElse(null));
    }

    @Test
    public void deleteStatus() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultStatus(TEST_USERNAME);
        final Long statusId = taskStatusRepository.findAll().get(0).getId();

        utils.perform(delete(BASE_URL + STATUS_CONTROLLER_PATH + ID, statusId), TEST_USERNAME)
                .andExpect(status().isOk());

        assertEquals(0, taskStatusRepository.count());
    }

    @Test
    public void deleteStatusFails() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultStatus(TEST_USERNAME);
        final Long statusId = taskStatusRepository.findAll().get(0).getId() + 1;

        utils.perform(delete(BASE_URL + STATUS_CONTROLLER_PATH + ID, statusId), TEST_USERNAME)
                .andExpect(status().isInternalServerError());
        assertEquals(1, taskStatusRepository.count());
    }
}
