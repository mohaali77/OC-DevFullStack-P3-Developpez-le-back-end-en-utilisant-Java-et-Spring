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
@Table(name = "rentals")
@Getter
@Setter
public class RentalModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "surface")
    private double surface;

    @Column(name = "price")
    private double price;

    @NotNull
    @Column(name = "picture", length = 255, nullable = false)
    private String picture;

    @NotNull
    @Column(name = "description", length = 2000, nullable = false)
    private String description;

    @Column(name = "owner_id")
    private int ownerId;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
