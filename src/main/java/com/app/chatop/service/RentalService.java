package com.app.chatop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.app.chatop.dto.RentalDTO;
import com.app.chatop.dto.RentalGetDTO;
import com.app.chatop.model.RentalModel;
import com.app.chatop.repository.RentalRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
public class RentalService {
	
	    @Autowired
	    private RentalRepository rentalRepository;
	    
	    @Autowired
	    private UserService userService;
	        	    
	    /**
	     * Enregistre une image transmise dans le dossier local "static" et retourne son chemin relatif.
	     *
	     * @param file Le fichier image envoyé via le formulaire (type {@link MultipartFile}).
	     * @return Le chemin relatif du fichier enregistré (ex: "static/image.jpg").
	     * @throws IOException Si une erreur survient lors de la création du répertoire ou de l'écriture du fichier.
	     */
	    public String savePicture(MultipartFile file) throws IOException {      

	        // Définition du chemin du dossier où sera sauvegardée l'image
	        String path = "static";
	        Path uploadPath = Paths.get(path);

	        // Crée le dossier "static" s'il n'existe pas encore
	        if (!Files.exists(uploadPath)) {
	            Files.createDirectories(uploadPath);
	        }

	        // Récupère le nom initial du fichier envoyé
	        String initialFile = Objects.requireNonNull(file.getOriginalFilename());

	        // Nettoie le nom du fichier pour éviter les caractères indésirables ou chemins relatifs
	        String fileName = StringUtils.cleanPath(initialFile);

	        // Crée un fichier local dans le dossier static avec ce nom
	        File pictureFile = new File(path + "/" + fileName);

	        // Sauvegarde le contenu du fichier dans ce chemin
	        file.transferTo(pictureFile.toPath());

	        // Retourne le chemin relatif de l'image enregistrée (utilisé pour la stocker dans la base)
	        return "static/" + fileName;
	    }

	
	    
	    /**
	     * Récupère toutes les annonces de location depuis la base de données
	     * et les convertit en une liste d'objets
	     *
	     * @return Une liste de représentant toutes les annonces disponibles.
	     */
	    
	    public List<RentalGetDTO> getRentals() {
	    	
	    	// Récupération de l'esnsemble des annonces
	        List<RentalModel> rentals = rentalRepository.findAll();
	        
	        // Conversion de la liste d'entités en DTO
	        List<RentalGetDTO> rentalDTOs = rentals.stream().map(rental -> {
	            RentalGetDTO rentalDTO = new RentalGetDTO();
	            rentalDTO.setId(rental.getId());
	            rentalDTO.setName(rental.getName());
	            rentalDTO.setSurface(rental.getSurface());
	            rentalDTO.setPrice(rental.getPrice());
	            rentalDTO.setPicture(rental.getPicture());
	            rentalDTO.setDescription(rental.getDescription());
	            rentalDTO.setOwnerId(rental.getOwnerId());
	            rentalDTO.setCreatedAt(rental.getCreatedAt());
	            rentalDTO.setUpdatedAt(rental.getUpdatedAt());
	            return rentalDTO;
	        }).toList();
	        
	        // 	Retourne la liste des objets convertis
	        return rentalDTOs;
	        
	    }
	    
	    /**
	     * Récupère une annonce de location à partir de son identifiant et la convertit.
	     *
	     * @param id L'identifiant de l'annonce à récupérer.
	     * @return L'objet contenant les détails de l'annonce.
	     *
	     * @throws ResponseStatusException si aucune annonce avec l'identifiant fourni n'est trouvée (HTTP 404).
	     */
	    
	    public RentalGetDTO getRentalById(int id){ 	
	    	
	    		// Récupère l'annonce dans la base de donnée grâce à son id, ou renvoie une erreur si non trouvée
	    		RentalModel rental = rentalRepository.findById(id)
	    		    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found: " + id));
	        
	    		// Conversion de l'entité en DTO
	            RentalGetDTO rentalDTO = new RentalGetDTO();        
	            rentalDTO.setId(rental.getId());
	            rentalDTO.setName(rental.getName());
	            rentalDTO.setSurface(rental.getSurface());
	            rentalDTO.setPrice(rental.getPrice());
	            rentalDTO.setPicture(rental.getPicture());
	            rentalDTO.setDescription(rental.getDescription());
	            rentalDTO.setOwnerId(rental.getOwnerId());
	            rentalDTO.setCreatedAt(rental.getCreatedAt());
	            rentalDTO.setUpdatedAt(rental.getUpdatedAt());
	            
	            // Retourne l'objet convertit 
	            return rentalDTO;
	        
	    }
	    
	    /**
	     * Crée et enregistre une nouvelle annonce à partir des données fournies dans le DTO.
	     *
	     * @param rentalDTO L'objet contenant les informations de l'annonce, y compris l'image, la description, le nom, le prix, etc.
	     * @return L'objet enregistré dans la base de données.
	     */
	    
	    public RentalModel createRental(RentalDTO rentalDTO) throws IOException  {
	    	
	    	// Récupérer l'email de l'utilisateur authentifié
	        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
	        int ownerId = userService.findUserIdByEmail(currentUserEmail);
	
	        // Sauvegarde de l'image dans le dossier static
	        String pictureName = savePicture(rentalDTO.getPicture());
	        
	        // Récupération de l'URL de base de l'application et construction du chemin complet de l'image enregistrée
	        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
	        String picturePath = baseUrl + "/" + pictureName;
	
	        // Création d'un nouvel objet RentalModel à partir du DTO
	        RentalModel rental = new RentalModel();
	        rental.setName(rentalDTO.getName());
	        rental.setSurface(rentalDTO.getSurface());
	        rental.setPrice(rentalDTO.getPrice());
	        rental.setDescription(rentalDTO.getDescription());
	        rental.setOwnerId(ownerId);
	        rental.setPicture(picturePath);
	    	
	        // Enregistre l'annonce
	        return rentalRepository.save(rental);
	    }
	    
	    
	    /**
	     * Met à jour une annonce de location existante avec les nouvelles données fournies par l'objet en paramètre.
	     *
	     * @param rentalDTO L'objet contenant les nouvelles informations à appliquer à l'annonce.
	     * @param id L'identifiant de l'annonce à mettre à jour.
	     * @return L'entité mise à jour et enregistrée en base de données.
	     *
	     * @throws ResponseStatusException si l'annonce avec l'identifiant spécifié n'est pas trouvée (HTTP 404).
	     */
	    
	    public RentalModel updateRental(RentalDTO rentalDTO, int id)  {
	    	
	    	// Récupère l'annonce dans la base de donnée grâce à son id, ou renvoie une erreur si non trouvée
	        RentalModel existingRental = rentalRepository.findById(id)
	                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found: " + id));
	        
	        // Modification des information de l'annonce existante avec les infos de l'objet en paramètre
	        existingRental.setName(rentalDTO.getName());
	        existingRental.setSurface(rentalDTO.getSurface());
	        existingRental.setPrice(rentalDTO.getPrice());
	        existingRental.setDescription(rentalDTO.getDescription());
	    	
	        // Enregistrement de l'annonce
	        return rentalRepository.save(existingRental);
	    }
	    
}

