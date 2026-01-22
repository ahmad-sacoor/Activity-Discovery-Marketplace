package com.example.activity.controller;

import com.example.activity.model.Activity;
import com.example.activity.model.Booking;
import com.example.activity.repository.ActivityRepository;
import com.example.activity.repository.BookingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final ActivityRepository activityRepository;

    public BookingController(BookingRepository bookingRepository, ActivityRepository activityRepository) {
        this.bookingRepository = bookingRepository;
        this.activityRepository = activityRepository;
    }

    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Request body is required"));
        }
        if (request.userId == null || request.userId <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "userId must be > 0"));
        }
        if (request.activityId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "activityId is required"));
        }

        Optional<Activity> activityOpt = activityRepository.findById(request.activityId);
        if (activityOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Activity not found: " + request.activityId));
        }

        Booking booking = new Booking();
        booking.setUserId(request.userId);
        booking.setActivity(activityOpt.get());
        booking.setBookedAt(LocalDateTime.now());

        Booking saved = bookingRepository.save(booking);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<?> getBookings(@RequestParam Long userId) {
        if (userId == null || userId <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "userId must be > 0"));
        }

        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return ResponseEntity.ok(bookings);
    }

    // Simple request shape for POST /bookings
    public static class BookingRequest {
        public Long userId;
        public Long activityId;

        public BookingRequest() {
        }
    }
}
