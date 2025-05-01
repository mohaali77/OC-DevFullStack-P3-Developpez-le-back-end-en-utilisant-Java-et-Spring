package com.app.chatop.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RentalGetDTO {
	
    private int id;
    
    @NotNull
    private String name;
    
    private double surface;
    
    private double price;
    
    @NotNull
    private String picture;
    
    @NotNull
    private String description;

    @JsonProperty("owner_id")
    private int ownerId; // Champ existant mais on le renomme pour la réponse JSON

    @JsonProperty("created_at")
    private LocalDateTime createdAt; // Champ existant mais on le renomme pour la réponse JSON

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt; // Champ existant mais on le renomme pour la réponse JSON

    
}
