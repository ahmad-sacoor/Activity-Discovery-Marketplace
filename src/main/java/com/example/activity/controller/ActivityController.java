package com.example.activity.controller;

import com.example.activity.modal.Activity;
import com.example.activity.repository.ActivityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST Controller for Activity endpoints.
 *
 * @RestController: Combination of @Controller and @ResponseBody.
 *                  Tells Spring: "this class handles HTTP requests and returns JSON".
 *                  Every method's return value is automatically converted to JSON.
 *
 * @RequestMapping: Base URL path for all endpoints in this controller.
 *                  All methods here will start with "/activities"
 *
 * @CrossOrigin: Enables CORS (Cross-Origin Resource Sharing) for this controller.
 *               Allows requests from http://localhost:3000 (your future frontend).
 *               Without this, browsers would block requests from different origins.
 */
@RestController
@RequestMapping("/activities")
@CrossOrigin(origins = "http://localhost:3000")
public class ActivityController {

    private final ActivityRepository activityRepository;

    /**
     * Constructor injection of ActivityRepository.
     * Spring automatically provides the repository instance.
     */
    public ActivityController(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * GET /activities
     * Returns all activities, with optional filtering by city, category, and maxPrice.
     *
     * @GetMapping: Marks this method as handling HTTP GET requests to "/activities"
     *
     * @RequestParam: Extracts query parameters from the URL.
     *                Example: /activities?city=Lisbon&maxPrice=50
     *                'required = false' means these parameters are optional.
     *                If not provided, the value will be null.
     */
    @GetMapping
    public List<Activity> getAllActivities(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal maxPrice
    ) {
        // Start with all activities from the database
        List<Activity> activities = activityRepository.findAll();

        // Apply filters using Java streams (simple approach for Day 1)
        // In production, you'd use database queries for better performance

        // Filter by city (case-insensitive, partial match)
        if (city != null && !city.isEmpty()) {
            activities = activities.stream()
                    .filter(activity -> activity.getCity().toLowerCase()
                            .contains(city.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Filter by category (case-insensitive, exact match)
        // You could also use "contains" like city if you prefer
        if (category != null && !category.isEmpty()) {
            activities = activities.stream()
                    .filter(activity -> activity.getCategory().equalsIgnoreCase(category))
                    .collect(Collectors.toList());
        }

        // Filter by maxPrice (keep only activities <= maxPrice)
        if (maxPrice != null) {
            activities = activities.stream()
                    .filter(activity -> activity.getPrice().compareTo(maxPrice) <= 0)
                    .collect(Collectors.toList());
        }

        return activities;  // Spring automatically converts this List to JSON
    }

    /**
     * GET /activities/{id}
     * Returns a single activity by ID, or 404 if not found.
     *
     * @PathVariable: Extracts the {id} from the URL path.
     *                Example: /activities/5 → id = 5
     *
     * ResponseEntity: Allows us to control the HTTP status code.
     *                 We can return 200 OK with data, or 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        // findById returns an Optional<Activity> (might be empty if ID doesn't exist)
        return activityRepository.findById(id)
                .map(activity -> ResponseEntity.ok(activity))  // Found → 200 OK with activity
                .orElse(ResponseEntity.notFound().build());    // Not found → 404 Not Found
    }
}