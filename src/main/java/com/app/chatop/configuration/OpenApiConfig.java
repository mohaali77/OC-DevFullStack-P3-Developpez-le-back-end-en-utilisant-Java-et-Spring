package com.app.chatop.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;


@OpenAPIDefinition(
		info = @Info(

		description = "API Documentation for Chatop app",
		title = "Chatop API Documentation"
		),
			
		
		servers = {
				
			@Server(
					
			description = "Local ENV",
			url = "http://localhost:3001"
			
			)
		},
		security = {
				@SecurityRequirement(
						name = "bearerAuth"
				)
		}
	)

@SecurityScheme(
		name = "bearerAuth",
		description = "JWT auth using OAuth2",
		scheme = "bearer",
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		in = SecuritySchemeIn.HEADER
)

public class OpenApiConfig {

}
