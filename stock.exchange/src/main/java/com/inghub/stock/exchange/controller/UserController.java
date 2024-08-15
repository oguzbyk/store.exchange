package com.inghub.stock.exchange.controller;

import com.inghub.stock.exchange.entity.AppUser;
import com.inghub.stock.exchange.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Retrieve all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppUser.class))})
    })
    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Retrieve a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppUser.class))}),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppUser.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser appUser) {
        AppUser createdUser = userService.createUser(appUser);
        return ResponseEntity.status(201).body(createdUser);
    }

    @Operation(summary = "Update an existing user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = AppUser.class))}),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long id, @RequestBody AppUser appUserDetails) {
        AppUser updatedUser = userService.updateUser(id, appUserDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete a user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
