package com.app.chatop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.chatop.model.MessageModel;
import com.app.chatop.repository.MessageRepository;

@Service
public class MessageService {
	@Autowired
	private MessageRepository messageRepository;
	
	 public MessageModel saveMessage(MessageModel message) {
	        return messageRepository.save(message);
	 }
}
