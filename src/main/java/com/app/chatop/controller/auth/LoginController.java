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
	                ), 
	                @ApiResponse(
	                		description = "Internal Server Error",
	                        responseCode = "500",
	                        content = @Content(mediaType = "application/json")
	                )
	            }
	        )
	    
	    /**
	     * Authentifie un utilisateur à partir de son email et mot de passe, puis génère un token JWT s'il est valide.
	     *
	     * @param loginDTO L'objet contenant les identifiants de connexion.
	     * @return Une réponse HTTP 200 contenant un token JWT si l'utilisateur est authentifié avec succès,
	     *         une réponse 401 si les identifiants sont incorrects,
	     *         ou une réponse 500 en cas d'erreur interne.
	     */

		@PostMapping("/login")
	    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginDTO loginDTO) {
	        try {
	        	// Récupération de l'email et du mot de passe depuis le corps de la requête
	            String email = loginDTO.getEmail();
	            String password = loginDTO.getPassword();
	
	            // Appel du service qui vérifie si les identifiants sont valides
	            boolean isAuthenticated = userService.authenticateUser(email, password);
	            
	            // Si les identifiants sont valides
	            if (isAuthenticated) {
	                
	                // Création d'un objet Authentication avec l'email comme identifiant (pas de mot de passe, pas de rôles ici)
	                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
	
	                // Appel du service avec l'objet authentication en paramètre pour générer le token JWT
	                String token = jwtService.generateToken(authentication);
	                                
	                return ResponseEntity.ok(Map.of("token", token));
	
	            } else {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "error"));
	            }
	
	        } catch (Exception e) {
	        	 return ResponseEntity.internalServerError().body(null);
	        }
	    
	    }
	
}
