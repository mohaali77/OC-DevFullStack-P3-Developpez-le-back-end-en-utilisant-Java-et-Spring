package com.app.chatop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
	
	@NotNull
    private String message;
	    
    private int rental_id;
    
}

