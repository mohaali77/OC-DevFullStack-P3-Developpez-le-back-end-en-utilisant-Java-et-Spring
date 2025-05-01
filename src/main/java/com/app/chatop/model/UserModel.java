package com.app.chatop.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "users")
@Getter
@Setter
public class UserModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="email", unique = true, length = 255, nullable = false)
	@NotNull
	private String email;
	
	@Column(name="name", length = 255, nullable = false)
	@NotNull
	private String name;
	
	@Column(name="password", length = 255, nullable = false)
	@NotNull
	private String password;
	
	@CreationTimestamp
	@Column(name="created_at", updatable = false,  nullable = false)
	@NotNull
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt;
    
    
}
