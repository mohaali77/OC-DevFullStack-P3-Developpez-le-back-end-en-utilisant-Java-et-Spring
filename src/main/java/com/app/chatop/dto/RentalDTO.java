package com.app.chatop.dto;

import org.springframework.web.multipart.MultipartFile;

public class RentalDTO {

    private String name;
    private double surface;
    private double price;
    private String description;
    private MultipartFile picture; // Utilisé pour l'image

    // Constructeurs
    public RentalDTO() {}

    public RentalDTO(String name, double surface, double price, String description, MultipartFile picture) {
        this.name = name;
        this.surface = surface;
        this.price = price;
        this.description = description;
        this.picture = picture;
    }

    // Getters et Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}
