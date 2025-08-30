package com.saorim.flashcard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saorim.flashcard.model.User;
import com.saorim.flashcard.service.UserService;
import com.saorim.flashcard.service.UserService.UserStats;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User", description = "Operações relacionadas a usuários")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<User> getCurrentUserProfile(Authentication authentication) {
        User user = userService.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get current user statistics")
    public ResponseEntity<UserStats> getCurrentUserStats(Authentication authentication) {
        UserStats stats = userService.getUserStats(authentication.getName());
        return ResponseEntity.ok(stats);
    }

    @PutMapping("/profile")
    @Operation(summary = "Update current user profile")
    public ResponseEntity<User> updateCurrentUserProfile(@RequestBody User userDetails, 
                                                        Authentication authentication) {
        User currentUser = userService.getUserByUsername(authentication.getName());
        User updatedUser = userService.updateUser(currentUser.getId(), userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/password")
    @Operation(summary = "Update current user password")
    public ResponseEntity<String> updateCurrentUserPassword(@RequestBody PasswordUpdateRequest request, 
                                                           Authentication authentication) {
        userService.updatePasswordByUsername(
            authentication.getName(), 
            request.getCurrentPassword(), 
            request.getNewPassword()
        );
        return ResponseEntity.ok("Senha atualizada com sucesso!");
    }

    @DeleteMapping("/profile")
    @Operation(summary = "Delete current user account")
    public ResponseEntity<String> deleteCurrentUserAccount(Authentication authentication) {
        userService.deleteUserByUsername(authentication.getName());
        return ResponseEntity.ok("Conta deletada com sucesso!");
    }

    // DTO para atualização de senha
    public static class PasswordUpdateRequest {
        private String currentPassword;
        private String newPassword;

        public String getCurrentPassword() { return currentPassword; }
        public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }
        
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}