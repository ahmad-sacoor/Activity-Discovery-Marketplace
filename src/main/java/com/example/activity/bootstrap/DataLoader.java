package com.example.activity.bootstrap;

import com.example.activity.modal.Activity;
import com.example.activity.repository.ActivityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * CommandLineRunner runs once when the application starts.
 *
 * We use it here to seed initial data for Day 1 learning.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final ActivityRepository activityRepository;

    // Constructor injection:
    // Spring sees ActivityRepository is a bean, and passes it in automatically.
    public DataLoader(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void run(String... args) {
        // Only seed if empty (so restarts don't duplicate rows).
        if (activityRepository.count() > 0) {
            return;
        }

        List<Activity> sample = List.of(
                new Activity("Lisbon Food Tour", "Lisbon", "Food", new BigDecimal("35.00"), 4.7, 3),
                new Activity("Porto Wine Tasting", "Porto", "Food", new BigDecimal("55.00"), 4.8, 2),
                new Activity("Sintra Day Trip", "Lisbon", "Culture", new BigDecimal("65.00"), 4.6, 8),
                new Activity("Barcelona Gaudí Walk", "Barcelona", "Culture", new BigDecimal("25.00"), 4.5, 2),
                new Activity("Rome Colosseum Skip-the-Line", "Rome", "Culture", new BigDecimal("75.00"), 4.9, 3),
                new Activity("Algarve Kayak Caves", "Lagos", "Adventure", new BigDecimal("50.00"), 4.8, 3),
                new Activity("Madeira Levada Hike", "Funchal", "Nature", new BigDecimal("45.00"), 4.7, 5),
                new Activity("Berlin Techno Night", "Berlin", "Nightlife", new BigDecimal("30.00"), 4.3, 4),
                new Activity("Paris Museum Pass Day", "Paris", "Culture", new BigDecimal("90.00"), 4.4, 6),
                new Activity("Amsterdam Canal Cruise", "Amsterdam", "Adventure", new BigDecimal("20.00"), 4.2, 2)
        );

        activityRepository.saveAll(sample);
    }
}
