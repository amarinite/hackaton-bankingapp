package com.hackathon.bankingapp.Controllers;

import com.hackathon.bankingapp.Entities.AuthRequest;
import com.hackathon.bankingapp.Entities.AuthResponse;
import com.hackathon.bankingapp.Entities.RegisterRequest;
import com.hackathon.bankingapp.Entities.User;
import com.hackathon.bankingapp.Services.Impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody RegisterRequest request) {
        return  ResponseEntity.ok(userService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest request) {
        return  ResponseEntity.ok(userService.loginUser(request));
    }


//        String identifier = loginRequest.get("identifier");
//        String password = loginRequest.get("password");
//        Optional<User> user = userService.loginUser(identifier, password);
//
//        if (user.isPresent()) {
//            // TODO: generate JWT
//            String token = "generated_jwt_token"; // Placeholder for actual JWT generation
//            return ResponseEntity.ok(Map.of("token", token));
//        }
//
//        return ResponseEntity.status(401).body("Bad credentials");
//    }
}
