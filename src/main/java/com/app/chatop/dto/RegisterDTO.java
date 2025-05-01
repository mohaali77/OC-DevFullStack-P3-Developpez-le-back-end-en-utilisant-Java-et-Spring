package com.app.chatop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {
	
	@NotNull
	private String name;
	
	@NotNull
	private String email; 
	
	@NotNull
	private String password;
	
	
}
