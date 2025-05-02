package com.app.chatop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.app.chatop.dto.MessageDTO;
import com.app.chatop.model.MessageModel;
import com.app.chatop.model.UserModel;
import com.app.chatop.repository.MessageRepository;
import com.app.chatop.repository.UserRepository;

@Service
public class MessageService {
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 * Crée et enregistre un message dans la base de données à partir des données fournies.
	 *
	 * @param messageDTO L'objet contenant le contenu du message et l'identifiant de l'annonce concernée.
	 * @return L'objet {@link MessageModel} enregistré.
	 */
	public MessageModel saveMessage(MessageDTO messageDTO) {
		 
		 	//Renvoie une erreur si le contenu du message est vide 
		 	if (messageDTO.getMessage().isBlank()) {
			    throw new IllegalArgumentException("Le contenu du message ne peut pas être vide.");
			}

		 	//Renvoie une erreur si l'identifiant de l'annonce est invalide.
	        if (messageDTO.getRental_id() <= 0) {
	            throw new IllegalArgumentException("L'identifiant de l'annonce est invalide.");
	        }
		 
	        // Récupération de l'email l'utilisateur authentifié 
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String email = authentication.getName();

	        // Recherche de l'utilisateur en base de données
	        UserModel user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable."));
		 
	        // Conversion du message de DTO à entité
	        MessageModel message = new MessageModel();
	        message.setMessage(messageDTO.getMessage());
	        message.setRentalId(messageDTO.getRental_id());
	        message.setUserId(user.getId());
	        
	        //Enregistrement du message
	        return messageRepository.save(message);
	}
}
