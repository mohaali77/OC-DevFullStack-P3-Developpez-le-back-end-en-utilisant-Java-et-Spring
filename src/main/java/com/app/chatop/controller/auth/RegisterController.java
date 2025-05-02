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


    /**
     * Enregistre un nouvel utilisateur à partir des données fournies, puis retourne un token JWT s'il est créé avec succès.
     *
     * @param registerDTO L'objet contenant les informations d'enregistrement.
     * @return Une réponse HTTP 200 contenant un token JWT si l'enregistrement réussit,
     *         une réponse 400 si l'email est déjà utilisé,
     *         ou une réponse 500 en cas d'erreur interne.
     */
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody RegisterDTO registerDTO) {
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

            // Appel du service pour enregistrer un nouvel utilisateur
            userService.registerUser(newUser);

            // Créer un objet Authentication pour l'utilisateur nouvellement créé
            Authentication authentication = new UsernamePasswordAuthenticationToken(newUser.getEmail(), null, new ArrayList<>());

            // Appel du service avec l'objet authentication en paramètre pour générer le token JWT
            String token = jwtService.generateToken(authentication);

            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
        	return ResponseEntity.internalServerError().body(null);
        }
    }

}




