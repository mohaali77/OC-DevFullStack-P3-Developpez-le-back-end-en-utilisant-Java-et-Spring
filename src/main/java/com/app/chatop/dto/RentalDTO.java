package com.app.chatop.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RentalDTO {

	@NotNull
    private String name;
	
    private double surface;
    
    private double price;
    
    @NotNull
    private String description;
    
    private MultipartFile picture;

}
