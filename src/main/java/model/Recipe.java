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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000, columnDefinition = "TEXT") // <--- ADDED columnDefinition = "TEXT"
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT") // <--- ADDED columnDefinition = "TEXT"
    private String ingredients; // For simplicity, we'll store as a single string

    @Column(nullable = false, columnDefinition = "TEXT") // Already had this for instructions
    private String instructions;

    private String prepTime;
    private String cookTime;
    private String servings;
    private String category;
    private String cuisine;

    @Column(columnDefinition = "TEXT") // <--- ADDED columnDefinition = "TEXT" (URLs can be long)
    private String imageUrl;

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