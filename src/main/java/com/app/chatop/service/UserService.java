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


    // Méthode pour enregistrer un nouvel utilisateur
    public UserModel registerUser(UserModel user) {
        // Encoder le mot de passe avant de l'enregistrer
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Sauvegarder l'utilisateur dans la base de données
        return userRepository.save(user);
    }

    // Méthode pour trouver un utilisateur par son email
    public Optional<UserModel> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public UserModel findByUserId(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Error ! User not found"));
    }
    
 // Méthode pour vérifier les identifiants de connexion
    public boolean authenticateUser(String email, String password) {
        Optional<UserModel> user = userRepository.findByEmail(email);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }
}
