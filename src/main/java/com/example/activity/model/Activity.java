package com.example.activity.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * A JPA Entity represents a table in the database.
 * Each instance of this class is typically a row in that table.
 */
@Entity
@Table(name = "activities")
public class Activity {

    /**
     * Primary key.
     * @GeneratedValue means the database (or JPA provider) generates the id for you.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column lets you define constraints for the DB column.
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String category;

    // BigDecimal is usually better than double for money (avoids floating point issues).
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private Double rating;

    private Integer durationHours;

    /**
     * JPA requires a no-args constructor (it uses reflection to create objects).
     */
    public Activity() {
    }

    /**
     * Convenience constructor for creating Activity objects in code (like seeding).
     * We don't include id here because the database will generate it.
     */
    public Activity(String title, String city, String category, BigDecimal price, Double rating, Integer durationHours) {
        this.title = title;
        this.city = city;
        this.category = category;
        this.price = price;
        this.rating = rating;
        this.durationHours = durationHours;
    }

    // ----- Getters / Setters -----

    public Long getId() {
        return id;
    }

    // Usually you don't setId manually if the DB generates it,
    // but having it doesn't hurt for Day 1 simplicity.
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }
}
