package com.app.chatop.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JWTService {

    // Attribut pour encoder (signer) les tokens JWT
    private JwtEncoder jwtEncoder;

    // Constructeur pour injecter le JwtEncoder (défini comme bean dans ta config)
    public JWTService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    // Méthode publique pour générer un token JWT à partir d'une Authentication
    public String generateToken(Authentication authentication) {
        // Instant actuel (date/heure de génération du token)
        Instant now = Instant.now();

        // Définition des "claims" du token JWT
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self")                           // Émetteur du token(MOI)
            .issuedAt(now)                            // Date d’émission
            .expiresAt(now.plus(1, ChronoUnit.DAYS))  // Expiration du token : 24h
            .subject(authentication.getName())        // Sujet du token (EMAIL)
            .build();

        // Prépare les paramètres d'encodage avec l'algorithme HS256
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(
            JwsHeader.with(MacAlgorithm.HS256).build(), claims
        );

        // Encode le token et retourne sa valeur sous forme de chaîne (token signé)
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
	
}