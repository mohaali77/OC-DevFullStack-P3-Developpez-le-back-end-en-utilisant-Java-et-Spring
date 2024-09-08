package com.app.chatop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.app.chatop.model.RentalModel;
import com.app.chatop.service.RentalService;

@SpringBootApplication
public class ChatopApplication implements CommandLineRunner {

	@Autowired
	private RentalService rentalService;
	
	public static void main(String[] args) {
		SpringApplication.run(ChatopApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {		
		Iterable<RentalModel> rentals = rentalService.getRentals();
		rentals.forEach(rental -> System.out.println(rental.getName()));
		
	}

}
