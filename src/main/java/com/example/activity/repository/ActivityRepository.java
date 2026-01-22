package com.example.activity.repository;

import com.example.activity.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Activity entity.
 *
 * By extending JpaRepository, Spring Data JPA automatically provides
 * implementations for common database operations without you writing any code!
 *
 * You get for free:
 * - save(entity)          → INSERT or UPDATE
 * - findById(id)          → SELECT by ID
 * - findAll()             → SELECT all rows
 * - deleteById(id)        → DELETE by ID
 * - count()               → COUNT rows
 * - and many more!
 *
 * @Repository: Marks this as a Spring Data repository (though it's optional
 *              when extending JpaRepository, it's good practice for clarity)
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    // No methods needed! Spring Data JPA provides everything we need for Day 1.
    // Later, you can add custom queries like:
    // List<Activity> findByCity(String city);
    // List<Activity> findByCategoryIgnoreCase(String category);
}