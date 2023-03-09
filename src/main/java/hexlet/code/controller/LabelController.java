package hexlet.code.controller;

import hexlet.code.dto.LabelDto;
import hexlet.code.model.Label;
import hexlet.code.service.LabelService;
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

import static org.springframework.http.HttpStatus.CREATED;


@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + LabelController.LABEL_CONTROLLER_PATH)
public class LabelController {

    public static final String LABEL_CONTROLLER_PATH = "/labels";
    public static final String ID = "/{id}";

    private final LabelService labelService;

    @Operation(summary = "Create new label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The label is created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Label.class))})})
    @PostMapping
    @ResponseStatus(CREATED)
    public Label createNewLabel(@RequestBody @Valid final LabelDto labelDto) {
        return labelService.createLabel(labelDto);
    }

    @ApiResponse(responseCode = "200", description = "The labels are found",
            content = @Content(schema = @Schema(implementation = Label.class)))
    @GetMapping
    @Operation(summary = "Get all labels")
    public List<Label> getAll() throws Exception {
        return labelService.getAllLabels()
                .stream()
                .toList();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The label is found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Label.class))})})
    @GetMapping(ID)
    @Operation(summary = "Get label")
    public Label getLabelById(@PathVariable final Long id) {
        return labelService.getLabelById(id);
    }


    @Operation(summary = "Update label")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The label is updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Label.class))}),
            @ApiResponse(responseCode = "422", description = "Invalid request",
                    content = @Content)})
    @PutMapping(ID)
    public Label updateLabel(@RequestBody @Valid LabelDto labelDto,
                             @PathVariable long id) {
        return labelService.updateLabel(labelDto, id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Label has been deleted"),
            @ApiResponse(responseCode = "404", description = "Label with this id wasn`t found")
    })
    @DeleteMapping(ID)
    @Operation(summary = "Delete label")
    public void deleteLabel(@PathVariable final long id) throws Exception {
        labelService.deleteLabel(id);
    }
}
