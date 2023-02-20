package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import hexlet.code.config.SpringConfig;
import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
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
import static hexlet.code.controller.LabelController.LABEL_CONTROLLER_PATH;
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
public class LabelControllerTest {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TestUtils utils;

    @AfterEach
    public void clear() {
        utils.tearDown();
    }

    @Test
    public void registration() throws Exception {
        assertEquals(0, labelRepository.count());
        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME).andExpect(status().isCreated());
        assertEquals(1, labelRepository.count());
    }

    @Test
    public void getLabelById() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultLabel(TestUtils.TEST_USERNAME);
        final Label expectedLabel = labelRepository.findAll().get(0);
        final var response = utils.perform(
                        get(BASE_URL + LABEL_CONTROLLER_PATH + ID,
                                expectedLabel.getId()), TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Label label = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertEquals(expectedLabel.getId(), label.getId());
        assertEquals(expectedLabel.getName(), label.getName());
    }

    @Test
    public void getLabelByIdFails() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME);
        final Label expectedLabel = labelRepository.findAll().get(0);
        Exception exception = assertThrows(
                Exception.class, () -> utils.perform(get(BASE_URL + LABEL_CONTROLLER_PATH + ID,
                        expectedLabel.getId()))
        );
        String message = exception.getMessage();
        assertTrue(message.contains("No value present"));

    }

    @Test
    public void getAllLabels() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME);
        final var response = utils.perform(get(BASE_URL + LABEL_CONTROLLER_PATH), TEST_USERNAME)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final List<Label> labels = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(labels).hasSize(1);
    }

    @Test
    public void twiceRegTheSameLabelFail() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME).andExpect(status().isCreated());
        utils.regDefaultLabel(TEST_USERNAME).andExpect(status().isUnprocessableEntity());

        assertEquals(1, labelRepository.count());
    }

    @Test
    public void updateLabel() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME);
        final Long labelId = labelRepository.findAll().get(0).getId();
        final var labelDto = new LabelDto(TEST_USERNAME_2);
        final var updateRequest = put(BASE_URL + LABEL_CONTROLLER_PATH + ID, labelId)
                .content(asJson(labelDto))
                .contentType(APPLICATION_JSON);

        utils.perform(updateRequest, TEST_USERNAME).andExpect(status().isOk());
        assertTrue(labelRepository.existsById(labelId));
        assertNull(labelRepository.findByName(TEST_LABEL_NAME).orElse(null));
        assertNotNull(labelRepository.findByName(TEST_LABEL_NAME_2).orElse(null));
    }

    @Test
    public void deleteLabel() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME);
        final Long labelId = labelRepository.findAll().get(0).getId();

        utils.perform(delete(BASE_URL + LABEL_CONTROLLER_PATH + ID, labelId), TEST_USERNAME)
                .andExpect(status().isOk());

        assertEquals(0, labelRepository.count());
    }

    @Test
    public void deleteLabelFails() throws Exception {
        utils.regDefaultUser();
        utils.regDefaultLabel(TEST_USERNAME);
        final Long labelId = labelRepository.findAll().get(0).getId() + 1;

        utils.perform(delete(BASE_URL + LABEL_CONTROLLER_PATH + ID, labelId), TEST_USERNAME)
                .andExpect(status().isInternalServerError());
        assertEquals(1, labelRepository.count());
    }
}
