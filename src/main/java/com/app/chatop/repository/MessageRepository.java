package com.app.chatop.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.chatop.model.MessageModel;

@Repository
public interface MessageRepository extends CrudRepository<MessageModel, Integer> {
}

