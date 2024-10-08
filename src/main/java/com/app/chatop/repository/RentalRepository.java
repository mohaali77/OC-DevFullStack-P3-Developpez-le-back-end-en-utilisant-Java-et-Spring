package com.app.chatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.chatop.model.RentalModel;

@Repository
public interface RentalRepository extends JpaRepository<RentalModel, Integer> {
}
