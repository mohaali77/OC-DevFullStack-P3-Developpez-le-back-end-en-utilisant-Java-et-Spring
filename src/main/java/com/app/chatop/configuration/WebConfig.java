package com.app.chatop.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
	 // Cette méthode permet de définir des handlers pour servir des ressources statiques
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Associe l'URL "/static/**" aux fichiers situés dans le dossier local "./static/"
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:./static/");
    }
      
}
