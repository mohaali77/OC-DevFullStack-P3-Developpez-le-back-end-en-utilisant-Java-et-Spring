package com.app.chatop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.chatop.model.UserModel;
import com.app.chatop.repository.UserRepository;
import java.util.Optional;

@Service
public class UserService  {

		    @Autowired
		    private UserRepository userRepository;
		
		    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		    
		    
		    /**
		     * Recherche un utilisateur par son adresse email et retourne son identifiant.
		     *
		     * @param email L'adresse email de l'utilisateur à rechercher.
		     * @return L'identifiant de l'utilisateur correspondant.
		     * @throws RuntimeException si aucun utilisateur avec cet email n'est trouvé.
		     */
		    
		    public int findUserIdByEmail(String email) {
		    	
		        // Rechercher l'utilisateur dans la base de données par email
		        Optional<UserModel> user = userRepository.findByEmail(email);
		
		        // Si l'utilisateur est trouvé, retourner son ID
		        if (user.isPresent()) {
		        	
		            return user.get().getId();
		            
		        } else {
		        	
		            // Si l'utilisateur n'est pas trouvé, lever une exception
		            throw new RuntimeException("User not found with email: " + email);
		            
		        }
		    }
		
		
		    /**
		     * Enregistre un nouvel utilisateur dans la base de données après avoir encodé son mot de passe.
		     *
		     * @param user L'objet contenant les informations de l'utilisateur à enregistrer.
		     * @return L'objet sauvegardé avec le mot de passe encodé.
		     */
		    public UserModel registerUser(UserModel user) {

		        // Encoder le mot de passe avant de l'enregistrer en base
		        user.setPassword(passwordEncoder.encode(user.getPassword()));

		        // Sauvegarder l'utilisateur dans la base de données
		        return userRepository.save(user);
		    }

		    
		
		    /**
		     * Recherche un utilisateur en base de données à partir de son adresse email.
		     *
		     * @param email L'adresse email de l'utilisateur à rechercher.
		     * @return Un {@link Optional} contenant l'utilisateur s'il existe, ou vide sinon.
		     */
		    
		    public Optional<UserModel> findUserByEmail(String email) {
		    	
		    	// Recherche l'utilisateur avec l'email fourni ou retourne un Optional vide si aucun utilisateur n'est trouvé
		    	
		        return userRepository.findByEmail(email);
		        
		    }
		    
		    
		    /**
		     * Récupère un utilisateur en fonction de son identifiant.
		     *
		     * @param id L'identifiant de l'utilisateur.
		     * @return L'objet correspondant.
		     * @throws RuntimeException si aucun utilisateur avec l'identifiant donné n'est trouvé.
		     */
		    
		    public UserModel findByUserId(int id) {
		    	
		        // Recherche l'utilisateur par son ID, ou lève une exception s'il n'existe pas
		        return userRepository.findById(id)
		            .orElseThrow(() -> new RuntimeException("Error ! User not found"));
		    }
		
		    
		    /**
		     * Vérifie si un utilisateur avec l'email donné existe et si le mot de passe fourni est correct.
		     *
		     * @param email L'adresse email de l'utilisateur.
		     * @param password Le mot de passe en clair saisi par l'utilisateur.
		     * @return TRUE si l'utilisateur existe et que le mot de passe est valide, FALSE sinon.
		     */
		    
		    public boolean authenticateUser(String email, String password) {
		
		        // Recherche l'utilisateur en base de données par son adresse email
		        Optional<UserModel> user = userRepository.findByEmail(email);
		
		        // Vérifie que l'utilisateur existe ET que le mot de passe correspond après encodage
		        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
		    }

}
