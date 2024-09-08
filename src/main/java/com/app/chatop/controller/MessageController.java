package com.app.chatop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.chatop.dto.MessageDTO;
import com.app.chatop.model.MessageModel;
import com.app.chatop.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

@RestController
@RequestMapping("/api")
public class MessageController {
	
    @Autowired
    private MessageService messageService;
    
    
    @Operation(
            summary = "Send a message",
            description = "Allows a user to send a message to another user or rental.",
            responses = {
                @ApiResponse(
                    description = "Message sent successfully",
                    responseCode = "200",
                    		content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                        value = "{\n  \"message\": \"Message sent with success\"\n}"
                                    )
                                )
                ),
                @ApiResponse(
                    description = "Bad request - Invalid message content",
                    responseCode = "400",
                    content = @Content(mediaType = "application/json")
                ),
                @ApiResponse(
                    description = "Unauthorized - Authentication required",
                    responseCode = "401",
                    content = @Content(mediaType = "application/json")
                )
            }
        )
    	    

    @PostMapping("/messages")
    public ResponseEntity<?> sendMessage(@RequestBody MessageDTO messageDTO) {
        try {
            // Créer une nouvelle instance de MessageModel avec les données fournies par le DTO
            MessageModel message = new MessageModel();
            message.setMessage(messageDTO.getMessage());
            message.setUserId(messageDTO.getUser_id());
            message.setRentalId(messageDTO.getRental_id());

            // Sauvegarder l'objet Message dans la base de données
            messageService.saveMessage(message);
            
            return ResponseEntity.ok(Map.of("message", "Message sent with success"));

        } catch (IllegalArgumentException e) {
            // Retourne une réponse 400 si le contenu du message est invalide
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid message content"));
        } catch (Exception e) {
            // Retourne une réponse 401 si l'authentification est requise
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized: Authentication required"));
        }
    }
    
}
