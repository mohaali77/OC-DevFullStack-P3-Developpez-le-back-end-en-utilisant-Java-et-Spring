package com.app.chatop.controller.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.chatop.dto.UserDTO;
import com.app.chatop.model.UserModel;
import com.app.chatop.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;



@RestController
@RequestMapping("/api/auth")
public class MeController {
	
    
    @Autowired
    private UserRepository userRepository;
    
    
    @Operation(
            summary = "Get current user details",
            description = "Retrieves the details of the currently authenticated user.",
            responses = {
                @ApiResponse(
                    description = "OK",
                    responseCode = "200",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = UserDTO.class)
                    )
                ),
                @ApiResponse(
                    description = "Unauthorized - Invalid token or no user authenticated",
                    responseCode = "401",
                    content = @Content(
                        mediaType = "application/json"
                    )
                )
            }
        )


	   @GetMapping("/me")
	    public ResponseEntity<?> getCurrentUser() {
	        try {
	            // Récupérer le principal (nom d'utilisateur ou email) depuis le contexte de sécurité
	            String username = SecurityContextHolder.getContext().getAuthentication().getName();
	            System.out.println("Username from SecurityContext: " + username);

	            // Rechercher l'utilisateur dans la base de données par email
	            UserModel user = userRepository.findByEmail(username)
	                    .orElseThrow(() -> new RuntimeException("User not found"));

	            // Construire la réponse avec les détails de l'utilisateur
	            UserDTO userResponseDTO = new UserDTO(
	                    user.getId(),
	                    user.getName(),
	                    user.getEmail(),
	                    user.getCreatedAt(),
	                    user.getUpdatedAt()
	            );

	            return ResponseEntity.ok(userResponseDTO);

	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access: " + e.getMessage());
	        }
	    }
	   
}
