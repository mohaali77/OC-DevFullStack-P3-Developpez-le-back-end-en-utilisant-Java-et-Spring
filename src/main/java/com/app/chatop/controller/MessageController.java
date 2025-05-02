package com.app.chatop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.chatop.dto.MessageDTO;
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
                    description = "Internal server error",
                    responseCode = "500",
                    content = @Content(mediaType = "application/json")
                )
            }
        )
    
    
    /**
     * Envoie un message à partir des données fournies dans le corps de la requête.
     *
     * @param messageDTO L'objet contenant les informations du message à envoyer.
     * @return Une réponse HTTP 200 si le message est envoyé avec succès,
     *         ou 400 si le message est invalide,
     *         ou 500 en cas d'erreur interne.
     */
    
    @PostMapping("/messages")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody MessageDTO messageDTO) {
        try {
        	
            // Appel du service avec le DTO en paramètre pour le convertir et enregistrer le message
            messageService.saveMessage(messageDTO);
            
            return ResponseEntity.ok(Map.of("message", "Message sent with success"));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid message content"));
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error"));
        }
    }
    
}
