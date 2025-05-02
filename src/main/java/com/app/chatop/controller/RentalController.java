package com.app.chatop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.chatop.dto.RentalDTO;
import com.app.chatop.dto.RentalGetDTO;
import com.app.chatop.service.RentalService;

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
                ),
                @ApiResponse(
                		description = "Internal Server Error",
                        responseCode = "500",
                        content = @Content(mediaType = "application/json")
                )
            }
        )
    
    /**
     * Récupère la liste de toutes les annonces de location disponibles.
     *
     * @return Une réponse HTTP 200 contenant une map avec la clé "rentals"
     *         et une liste d'objets si la récupération réussit,
     *         ou une réponse 500 en cas d'erreur interne.
     */
    
    @GetMapping("/rentals")
    public ResponseEntity<Map<String, List<RentalGetDTO>>> getAllRentals() {
    	
    	try{
    	
    	//Appel du service pour récupérer l'ensemble des annonces
    	List<RentalGetDTO> rentalDTOs = rentalService.getRentals();
        
        return ResponseEntity.ok(Map.of("rentals", rentalDTOs));
        }
    	
    	catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
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
                ),
                @ApiResponse(
                		description = "Internal Server Error",
                        responseCode = "500",
                        content = @Content(mediaType = "application/json")
                )
            }
        )
    
    /**
     * Crée une nouvelle annonce à partir des données fournies dans le corps de la requête.
     *
     * @param rentalDTO L'objet contenant les informations de l'annonce à créer.
     * @return Une réponse HTTP 201 si l'annonce est créée avec succès,
     *         ou une réponse 500 en cas d'erreur interne.
    */
    
    @PostMapping("/rentals")
    public ResponseEntity<Map<String, String>> createRental(@RequestBody RentalDTO rentalDTO) {
        try {
        	
           // Appel du service pour créer et d'enregistrer l'annonce
           rentalService.createRental(rentalDTO);
                  
           return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Rental created successfully!"));
   
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error"));
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
                ),
                @ApiResponse(
                		description = "Internal Server Error",
                        responseCode = "500",
                        content = @Content(mediaType = "application/json")
                )
                }
              
        )
    
    /**
     * Met à jour une annonce existante avec les nouvelles données fournies.
     *
     * @param id L'identifiant de l'annonce à mettre à jour.
     * @param rentalDTO L'objet contenant les nouvelles informations de l'annonce.
     * @return Une réponse HTTP 200 avec un message de confirmation si la mise à jour réussit,
     *         ou une réponse 500 en cas d'erreur interne.
     */
    
    @PutMapping("/rentals/{id}")
    public ResponseEntity<Map<String, String>> updateRental(@PathVariable int id, @RequestBody RentalDTO rentalDTO) {
        
    	try {
    		
        // Appel du service pour modifier et enregistrer l'annonce
        rentalService.updateRental(rentalDTO, id);
        
        return ResponseEntity.ok(Map.of("message", "Rental updated ! "));
        
        }
    	catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Internal server error"));
        }
        

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
                ),
                @ApiResponse(
                	    description = "Rental not found",
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
    
    /**
     * Récupère une annonce spécifique en fonction de son identifiant.
     *
     * @param id L'identifiant de l'annonce à récupérer.
     * @return Une réponse HTTP 200 contenant les données de l'annonce si elle est trouvée,
     *         ou une réponse 500 en cas d'erreur interne.
     */

    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalGetDTO> getRentalById(@PathVariable int id) {
    	
    	try {
    		
    		// Appel du service pour récupérer les informations de l'annonce par son ID
    		RentalGetDTO rentalDTO = rentalService.getRentalById(id);

    		return ResponseEntity.ok(rentalDTO);
            
    		}
        
        
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
        
}
