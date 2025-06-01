package com.mycookbookapp.recipe_backend.model;

import jakarta.persistence.*; // Import all JPA annotations
import java.time.LocalDateTime; // For created/updated timestamps
// import lombok.Getter; // If you want to use Lombok for getters/setters
// import lombok.Setter; // If you want to use Lombok for getters/setters
// import lombok.NoArgsConstructor; // If you want to use Lombok for constructors
// import lombok.AllArgsConstructor; // If you want to use Lombok for constructors

@Entity // Marks this class as a JPA entity, meaning it maps to a database table
@Table(name = "recipes") // Specifies the name of the database table
// @Getter // If using Lombok
// @Setter // If using Lombok
// @NoArgsConstructor // If using Lombok
// @AllArgsConstructor // If using Lombok (requires a constructor with all fields)
public class Recipe {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increments the ID for new records
    private Long id;

    @Column(nullable = false) // Maps to a database column; 'nullable = false' means it cannot be empty
    private String title;

    @Column(length = 1000) // Specifies the maximum length of the string
    private String description;

    @Column(nullable = false)
    private String ingredients; // For simplicity, we'll store as a single string for now (e.g., comma-separated or JSON)

    @Column(nullable = false, columnDefinition = "TEXT") // TEXT allows for longer strings for instructions
    private String instructions;

    private String prepTime; // e.g., "30 min"
    private String cookTime; // e.g., "1 hour"
    private String servings; // e.g., "4-6"
    private String category; // e.g., "Dessert", "Dinner"
    private String cuisine;  // e.g., "Italian", "Mexican"
    private String imageUrl; // URL to an image

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // --- Constructors ---
    // JPA requires a no-argument constructor
    public Recipe() {
    }

    // You can add a constructor with all fields for convenience (optional)
    public Recipe(String title, String description, String ingredients, String instructions,
                  String prepTime, String cookTime, String servings, String category,
                  String cuisine, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.servings = servings;
        this.category = category;
        this.cuisine = cuisine;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // --- Getters and Setters ---
    // (You can use Lombok for these if you prefer, by uncommenting the @Getter/@Setter annotations above)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // You might also add a toString() method for easier debugging
    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}