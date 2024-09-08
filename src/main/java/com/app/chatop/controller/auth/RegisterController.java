package com.app.chatop.controller.auth;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.app.chatop.dto.RegisterDTO;
import com.app.chatop.model.UserModel;
import com.app.chatop.service.JWTService;
import com.app.chatop.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTService jwtService;
    
    @Operation(
    	    summary = "User Registration",
    	    description = "Registers a new user, generates a JWT token upon successful registration, and returns it.",
    	    responses = {
    	        @ApiResponse(
    	            description = "User registered successfully. A JWT token is returned.",
    	            responseCode = "201",
    	            		content = @Content(
    	        	                mediaType = "application/json",
    	        	                examples = {
    	        	                    @ExampleObject(
    	        	                        value = "{\"token\": \"jwt\"}"
    	        	                    )
    	        	                }
    	        	            )
    	        ),
    	        @ApiResponse(
    	            description = "Email already registered.",
    	            responseCode = "400",
    	            content = @Content(
    	                mediaType = "application/json",
    	                examples = {
    	                    @ExampleObject(
    	                        value = "{\"message\": \"Email already registered\"}"
    	                    )
    	                }
    	            )
    	        )
    	        
    	    }
    	)


    // Route pour l'inscription
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
        try {
            // Vérifier si l'utilisateur existe déjà en recherchant par email
            if (userService.findUserByEmail(registerDTO.getEmail()).isPresent()) {
                
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Email already registered"));
            }

            // Créer un nouvel objet UserModel à partir du RegisterDTO
            UserModel newUser = new UserModel();
            newUser.setName(registerDTO.getName());
            newUser.setEmail(registerDTO.getEmail());
            newUser.setPassword(registerDTO.getPassword());

            // Enregistrer le nouvel utilisateur
            userService.registerUser(newUser);

            // Créer un objet Authentication manuellement pour l'utilisateur nouvellement créé
            Authentication authentication = new UsernamePasswordAuthenticationToken(newUser.getEmail(), null, new ArrayList<>());

            // Générer le token
            String token = jwtService.generateToken(authentication);

            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            // En cas d'erreur, retournez un message d'erreur plus détaillé
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during registration: " + e.getMessage());
        }
    }

}




