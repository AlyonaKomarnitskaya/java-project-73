package hexlet.code.controller;

import hexlet.code.dto.UserDto;
import hexlet.code.model.User;
import hexlet.code.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
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
import java.util.List;
import java.util.NoSuchElementException;

import static hexlet.code.controller.UserController.USER_CONTROLLER_PATH;
import static org.springframework.http.HttpStatus.CREATED;

@AllArgsConstructor
@RestController
@RequestMapping("${base-url}" + USER_CONTROLLER_PATH)
public class UserController {
    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String ID = "/{id}";

    private static final String ONLY_OWNER_BY_ID = """
            @userRepository.findById(#id).get().getEmail() == authentication.getName()
        """;

    private final UserService userService;

    @Operation(summary = "Create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The user is created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))})})
    @ResponseStatus(CREATED)
    @PostMapping
    public User createUser(@RequestBody @Valid UserDto userDto) {
        return userService.createNewUser(userDto);
    }


    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "The users are found",
            content = @Content(schema = @Schema(implementation = User.class)))
    @GetMapping
    public List<User> getAll() throws Exception {
        return userService.getAll()
                .stream()
                .toList();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user is found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "The user is not found",
                    content = @Content)})
    @GetMapping(ID)
    @Operation(summary = "Get user")
    public User getUser(@PathVariable long id) throws NoSuchElementException {
        return userService.getUserById(id);
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user is updated",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "The user is not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden to update",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Invalid request",
                    content = @Content)})
    @PreAuthorize(ONLY_OWNER_BY_ID)
    @PutMapping(ID)
    public User updateUser(@PathVariable @Valid long id, @RequestBody @Valid UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The user is deleted"),
            @ApiResponse(responseCode = "404", description = "The user is not found",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden to delete",
                    content = @Content),
            @ApiResponse(responseCode = "422", description = "Data integrity violation",
                    content = @Content)})
    @PreAuthorize(ONLY_OWNER_BY_ID)
    @DeleteMapping(ID)
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }

}
