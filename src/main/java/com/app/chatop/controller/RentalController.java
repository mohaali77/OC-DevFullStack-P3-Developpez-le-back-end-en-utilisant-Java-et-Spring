package com.app.chatop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.app.chatop.dto.RentalDTO;
import com.app.chatop.dto.RentalGetDTO;
import com.app.chatop.model.RentalModel;
import com.app.chatop.service.RentalService;
import com.app.chatop.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RentalController {

    @Autowired
    private RentalService rentalService;
    
    @Autowired
    private UserService userService;
    
    
    @Operation(
            summary = "Get all rentals",
            description = "Retrieves all rental listings.",
            responses = {
                @ApiResponse(
                    description = "OK",
                    responseCode = "200",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = RentalGetDTO[].class)
                    )
                ),
                @ApiResponse(
                    description = "Unauthorized",
                    responseCode = "401",
                    content = @Content(mediaType = "application/json")
                )
            }
        )
    
    @GetMapping("/rentals")
    public ResponseEntity<Map<String, List<RentalGetDTO>>> getAllRentals() {
        List<RentalModel> rentals = rentalService.getRentals();
        
        // Convertir RentalModel en RentalGetDTO
        List<RentalGetDTO> rentalDTOs = rentals.stream().map(rental -> {
            RentalGetDTO rentalDTO = new RentalGetDTO();
            rentalDTO.setId(rental.getId());
            rentalDTO.setName(rental.getName());
            rentalDTO.setSurface(rental.getSurface());
            rentalDTO.setPrice(rental.getPrice());
            rentalDTO.setPicture(rental.getPicture());
            rentalDTO.setDescription(rental.getDescription());
            rentalDTO.setOwnerId(rental.getOwnerId());
            rentalDTO.setCreatedAt(rental.getCreatedAt());
            rentalDTO.setUpdatedAt(rental.getUpdatedAt());
            return rentalDTO;
        }).toList();
        
        return ResponseEntity.ok(Map.of("rentals", rentalDTOs));
    }

    
    @Operation(
            summary = "Create a new rental",
            description = "Creates a new rental listing.",
            responses = {
                @ApiResponse(
                    description = "OK",
                    responseCode = "201",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                            type = "object",
                            example = "{ \"message\": \"Rental created successfully!\" }"
                        )
                    )
                ),
                @ApiResponse(
                    description = "Unauthorized",
                    responseCode = "401",
                    content = @Content(
                        mediaType = "application/json"                 
                    )
                )
            }
        )
    
 // Route pour créer une nouvelle annonce
    @PostMapping("/rentals")
    public ResponseEntity<?> createRental(@RequestBody RentalDTO rentalDTO) {
        try {
        	
            // Récupérer l'email de l'utilisateur connecté
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            int ownerId = userService.findUserIdByEmail(currentUserEmail);

            // Sauvegarde de l'image
            String pictureName = rentalService.savePicture(rentalDTO.getPicture());
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String picturePath = baseUrl + "/" + pictureName;

            // Création d'un nouvel objet RentalModel
            RentalModel rental = new RentalModel();
            rental.setName(rentalDTO.getName());
            rental.setSurface(rentalDTO.getSurface());
            rental.setPrice(rentalDTO.getPrice());
            rental.setDescription(rentalDTO.getDescription());
            rental.setOwnerId(ownerId);
            rental.setPicture(picturePath); // Assigner l'image

            // Sauvegarder l'objet Rental dans la base de données
           rentalService.saveRental(rental);
                  
           return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Rental created successfully!"));
   
        } catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during rental creation: " + e.getMessage());
        }
    }
    
    @Operation(
            summary = "Update a rental",
            description = "Updates a rental by its ID, allowing only the owner of the rental to make changes.",
            responses = {
                @ApiResponse(
                    description = "OK",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                    		schema = @Schema(
                                    type = "object",
                                    example = "{ \"message\": \"Rental updated !\" }"
                                ))
                ),
                @ApiResponse(
                    description = "Unauthorized",
                    responseCode = "401",
                    content = @Content(mediaType = "application/json")
                )
                }
              
        )
    
    @PutMapping("/rentals/{id}")
    public ResponseEntity<?> updateRental(@PathVariable int id, @RequestBody RentalDTO rentalDTO) {
        // Récupérer l'annonce à partir de la base de données
        RentalModel existingRental = rentalService.getRentalById(id);

        // Mise à jour des champs disponibles dans le DTO
        existingRental.setName(rentalDTO.getName());
        existingRental.setSurface(rentalDTO.getSurface());
        existingRental.setPrice(rentalDTO.getPrice());
        existingRental.setDescription(rentalDTO.getDescription());

        // Sauvegarder les modifications
        rentalService.saveRental(existingRental);
        
        return ResponseEntity.ok(Map.of("message", "Rental updated ! "));

    }
    
    @Operation(
            summary = "Get rental by ID",
            description = "Fetches details of a rental by its ID.",
            responses = {
                @ApiResponse(
                    description = "Rental found successfully.",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RentalGetDTO.class))
                ),
                @ApiResponse(
                    description = "Unauthorized",
                    responseCode = "401",
                    content = @Content(mediaType = "application/json")
                )
            }
        )
    
    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalGetDTO> getRentalById(@PathVariable int id) {
        RentalModel rental = rentalService.getRentalById(id);

        // Mapping RentalModel to RentalGetDTO
        RentalGetDTO rentalDTO = new RentalGetDTO();
        rentalDTO.setId(rental.getId());
        rentalDTO.setName(rental.getName());
        rentalDTO.setSurface(rental.getSurface());
        rentalDTO.setPrice(rental.getPrice());
        rentalDTO.setPicture(rental.getPicture());
        rentalDTO.setDescription(rental.getDescription());
        rentalDTO.setOwnerId(rental.getOwnerId());
        rentalDTO.setCreatedAt(rental.getCreatedAt());
        rentalDTO.setUpdatedAt(rental.getUpdatedAt());

        return ResponseEntity.ok(rentalDTO);
    }
    
        
}
