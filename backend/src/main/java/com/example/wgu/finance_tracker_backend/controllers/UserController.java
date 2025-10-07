package com.example.wgu.finance_tracker_backend.controllers;

import com.example.wgu.finance_tracker_backend.DTOs.UserLoginRequest;
import com.example.wgu.finance_tracker_backend.DTOs.UserLoginResponse;
import com.example.wgu.finance_tracker_backend.DTOs.UserRegistrationRequest;
import com.example.wgu.finance_tracker_backend.DTOs.UserResponse;
import com.example.wgu.finance_tracker_backend.exceptions.UserAlreadyExistsException;
import com.example.wgu.finance_tracker_backend.services.interfaces.AuthService;
import com.example.wgu.finance_tracker_backend.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    // Dependency Injection via Constructor
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }



    // MANAGEMENT: PUT /api/users/{id}/password
//    @PutMapping("/{id}/password")
//    public ResponseEntity<UserResponse> updatePassword(
//            @PathVariable Long id,
//            @RequestBody UpdatePasswordRequest updateRequest) {
//
//        // Note: In a secure application, the 'id' would be ignored, and the user's ID
//        // would be pulled from the authenticated security context instead of the path.
//        UserResponse userResponse = userService.updatePassword(id, updateRequest);
//        return ResponseEntity.ok(userResponse);
//    }

    // MANAGEMENT: DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content for successful deletion
    }

    // Although not strictly necessary for MVP, a method to get the logged-in user's profile is often useful
    // MANAGEMENT: GET /api/users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        // We assume the service has a method to retrieve a UserResponse by ID
        // This is often used to pull profile data after a login or token verification
        // UserResponse userResponse = userService.getUserById(id);
        // return ResponseEntity.ok(userResponse);

        // Since we didn't define getById, we'll placeholder this for now, but keep the mapping.
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
