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
        
 // Méthode savePicture
    public String savePicture(MultipartFile file) throws IOException {        
        String path = "static";
        Path uploadPath = Paths.get(path);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String initialFile = Objects.requireNonNull(file.getOriginalFilename());
        String fileName = StringUtils.cleanPath(initialFile);

        File pictureFile = new File(path + "/" + fileName);
        file.transferTo(pictureFile.toPath());

        return "static/" + fileName; // Retourne le bon chemin
    }


    
    public List<RentalGetDTO> getRentals() {
    	//récupération des rentals
        List<RentalModel> rentals = rentalRepository.findAll();
        
        // Conversion des rentals en dto
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
        
        //retourne les rentals converties
        return rentalDTOs;
        
    }
    
public RentalGetDTO getRentalById(int id){ 	
    	
    	RentalModel rental = rentalRepository.findById(id)
    		    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found: " + id));
        
    		// Conversion de la rental en dto
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
            
            //retourne lA rental convertie
            return rentalDTO;
        
    }
    
    
    public RentalModel createRental(RentalDTO rentalDTO) throws IOException  {
    	
    	// Récupérer l'email de l'utilisateur authentifié
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        int ownerId = userService.findUserIdByEmail(currentUserEmail);

        // Sauvegarde de l'image
        String pictureName = savePicture(rentalDTO.getPicture());
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String picturePath = baseUrl + "/" + pictureName;

        // Création d'un nouvel objet RentalModel
        RentalModel rental = new RentalModel();
        rental.setName(rentalDTO.getName());
        rental.setSurface(rentalDTO.getSurface());
        rental.setPrice(rentalDTO.getPrice());
        rental.setDescription(rentalDTO.getDescription());
        rental.setOwnerId(ownerId);
        rental.setPicture(picturePath);
    	
        return rentalRepository.save(rental);
    }
    
    public RentalModel updateRental(RentalDTO rentalDTO, int id)  {
    	        
        RentalModel existingRental = rentalRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rental not found: " + id));

        existingRental.setName(rentalDTO.getName());
        existingRental.setSurface(rentalDTO.getSurface());
        existingRental.setPrice(rentalDTO.getPrice());
        existingRental.setDescription(rentalDTO.getDescription());
    	
        return rentalRepository.save(existingRental);
    }
    
}

