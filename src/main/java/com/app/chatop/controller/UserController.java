package com.app.chatop.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.chatop.dto.UserDTO;
import com.app.chatop.model.UserModel;
import com.app.chatop.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


@RestController
@RequestMapping("/api/user")

public class UserController {

    @Autowired
    private UserService userService;
    
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves the user details by their unique ID.",
            responses = {
                @ApiResponse(
                    description = "OK",
                    responseCode = "200",
                    		content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class))
                ),
                @ApiResponse(
                    description = "Unauthorized",
                    responseCode = "401",
                    content = @Content(mediaType = "application/json")
                ), 
                
                @ApiResponse(
                		description = "User not found",
                	    responseCode = "404",
                	    content = @Content(mediaType = "application/json")
                ), 
                @ApiResponse(
                		description = "Internal Server Error",
                        responseCode = "500",
                        content = @Content(mediaType = "application/json")
                		)
            }
        )

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable int id) {
    	
    	try {
            // Recherche de l'utilisateur en base de données
            UserModel user = userService.findByUserId(id);

            // Si aucun utilisateur trouvé, retourne un 404 Not Found
            if (user == null) {
                return ResponseEntity.notFound().build();
            }

            // Création d’un objet DTO à partir de l’entité UserModel
            UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
            );

            // Retourne le DTO avec un code 200 OK
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            // En cas d’erreur inattendue, retourne un 500
            return ResponseEntity.internalServerError().body(null);
        }
    
    }
    
}

