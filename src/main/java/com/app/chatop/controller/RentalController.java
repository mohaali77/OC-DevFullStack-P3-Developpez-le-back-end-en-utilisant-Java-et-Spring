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
    
    @GetMapping("/rentals")
    public ResponseEntity<Map<String, List<RentalGetDTO>>> getAllRentals() {
    	
    	try{
    	
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
    
    // Route pour créer une nouvelle annonce
    @PostMapping("/rentals")
    public ResponseEntity<Map<String, String>> createRental(@RequestBody RentalDTO rentalDTO) {
        try {
        	
           // on appelle le service qui se chargera de créer et d'enregistrer le rental
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
    
    @PutMapping("/rentals/{id}")
    public ResponseEntity<Map<String, String>> updateRental(@PathVariable int id, @RequestBody RentalDTO rentalDTO) {
        
    	try {
    		
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
    
    @GetMapping("/rentals/{id}")
    public ResponseEntity<RentalGetDTO> getRentalById(@PathVariable int id) {
    	
    	try {
    		
    		RentalGetDTO rentalDTO = rentalService.getRentalById(id);

    		return ResponseEntity.ok(rentalDTO);
            
    		}
        
        
        catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }
    
        
}
