package com.app.chatop.controller.auth;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.chatop.dto.LoginDTO;
import com.app.chatop.service.JWTService;
import com.app.chatop.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
	
	 @Autowired
	    private UserService userService;

	    @Autowired
	    private JWTService jwtService;
	    
	   
	    @Operation(
	            summary = "User login",
	            description = "Authenticates a user and returns a JWT token if successful.",
	            responses = {
	                @ApiResponse(
	                    description = "Login successful",
	                    responseCode = "200",
	                    content = @Content(
	                        mediaType = "application/json",
	                        examples = @ExampleObject(
	                            value = "{\n  \"token\": \"<JWT_TOKEN>\",\n  \"message\": \"Login successful\"\n}"
	                        )
	                    )
	                ),
	                @ApiResponse(
	                    description = "Unauthorized - Invalid email or password",
	                    responseCode = "401",
	                    content = @Content(
	                        mediaType = "application/json",
	                        examples = @ExampleObject(
	                            value = "{\n  \"message\": \"error\"\n}"
	                        )
	                    )
	                )
	            }
	        )

	@PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
        try {
            String email = loginDTO.getEmail();
            String password = loginDTO.getPassword();

            // Vérifier les identifiants de connexion
            boolean isAuthenticated = userService.authenticateUser(email, password);
            
            if (isAuthenticated) {
                // Créer un objet Authentication manuellement
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());

                // Générer le token
                String token = jwtService.generateToken(authentication);
                                
                return ResponseEntity.ok(Map.of("token", token));

            } else {
                
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "error"));

            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during login: " + e.getMessage());
        }
    }
	
}
