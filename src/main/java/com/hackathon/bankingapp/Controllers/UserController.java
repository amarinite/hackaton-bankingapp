package com.hackathon.bankingapp.Controllers;

import com.hackathon.bankingapp.DTO.AuthRequest;
import com.hackathon.bankingapp.DTO.AuthResponse;
import com.hackathon.bankingapp.DTO.RegisterRequest;
import com.hackathon.bankingapp.DTO.RegisterResponse;
import com.hackathon.bankingapp.Services.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest request) {
        return  ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest request) {
        return  ResponseEntity.ok(userService.loginUser(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Authorization header is missing or invalid.");
        }
        String token = authHeader.substring(7);
        // userService.logout(token);

        return ResponseEntity.ok("User logged out successfully.");
    }
}
