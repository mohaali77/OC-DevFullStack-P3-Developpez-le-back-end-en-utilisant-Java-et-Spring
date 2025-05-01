package com.app.chatop.configuration;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class SecurityConfig {
	

	@Value("${jwt.secret}")
    private String jwtKey;
  

	// Configuration de la sécurité HTTP
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    return http
	        // Désactive la protection CSRF
	        .csrf(csrf -> csrf.disable())

	        // Configure la gestion de session en mode stateless (pas de session, on utilise des JWT)
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

	        // Définition des règles d'autorisation pour les différentes routes
	        .authorizeHttpRequests(auth ->
	            auth
	                // Autorise l'accès sans authentification à ces chemins :
	                .requestMatchers(
	                    "/api/auth/**",       // Routes publiques liées à l'auth (ex: login, register)
	                    "/static/**",         // Accès aux fichiers statiques (images, etc.)
	                    "/v3/api-docs/",      // Documentation OpenAPI
	                    "/v3/api-docs/**",    // Tous les endpoints liés à OpenAPI
	                    "/swagger-ui/**"      // Interface Swagger UI
	                ).permitAll()

	                // Toutes les autres routes nécessitent une authentification
	                .anyRequest().authenticated()
	        )

	        // Active la vérification du token JWT pour les requêtes
	        .oauth2ResourceServer((oauth2) -> oauth2.jwt(withDefaults()))

	        // Construit et retourne l'objet SecurityFilterChain
	        .build();
	}

    
    
    @Bean
    public JwtDecoder jwtDecoder() {
        // Création de la clé secrète à partir de la clé secrète définie dans application.properties
        SecretKeySpec secretKey = new SecretKeySpec(this.jwtKey.getBytes(), 0, this.jwtKey.getBytes().length, "HMac256");

        // Construction d'un décodeur JWT basé sur cette clé, avec l'algorithme HS256
        return NimbusJwtDecoder
            .withSecretKey(secretKey)
            .macAlgorithm(MacAlgorithm.HS256)
            .build();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        // Utilise la même clé secrète pour signer les JWT
        return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtKey.getBytes()));
    }
    
}