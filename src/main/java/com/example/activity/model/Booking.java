package com.example.activity.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Booking = "user X booked activity Y at time Z"
 *
 * This is a JPA Entity, so it becomes a table in the database.
 */


@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;


     // Many bookings can point to the same activity.
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_id", nullable = false) // FK column in bookings table
    private Activity activity;


    private LocalDateTime bookedAt; //  When the booking was created.



    public Booking() {
    }


    public Booking(Long userId, Activity activity, LocalDateTime bookedAt) {
        this.userId = userId;
        this.activity = activity;
        this.bookedAt = bookedAt;
    }

    // Getters / Setters

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Activity getActivity() {
        return activity;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }
}
