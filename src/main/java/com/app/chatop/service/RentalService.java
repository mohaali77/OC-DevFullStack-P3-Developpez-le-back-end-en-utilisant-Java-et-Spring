package com.app.chatop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

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


    
    public List<RentalModel> getRentals() {
        return rentalRepository.findAll();
    }
    
    public RentalModel getRentalById(int id){
        return rentalRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Error ! Rental not found : " + id)
        );
    }
    
    
    public RentalModel createRental(RentalModel rental) {
        return rentalRepository.save(rental);
    }
    
 // Méthode pour sauvegarder le Rental dans la base de données
    public RentalModel saveRental(RentalModel rental) {
        return rentalRepository.save(rental);
    }
}
