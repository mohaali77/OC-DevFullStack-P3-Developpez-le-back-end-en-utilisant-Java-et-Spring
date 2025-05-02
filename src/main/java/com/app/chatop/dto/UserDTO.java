package com.app.chatop.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
	
    private int id;
    
    @NotNull
    private String name;
    
    @NotNull
    private String email;
    
    @NotNull
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    
    @NotNull
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

}

