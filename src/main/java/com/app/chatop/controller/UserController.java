package com.app.chatop.controller;

import java.util.Map;

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
                )
            }
        )

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
    	
    	try {
        UserModel user = userService.findByUserId(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        UserDTO userDTO = new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );

        return ResponseEntity.ok(userDTO);}
    	
    	catch(Exception e) {
    		
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized: Authentication required"));

    	}
    }
    
}

