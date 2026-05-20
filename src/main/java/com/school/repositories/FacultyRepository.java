package com.school.repositories;

import com.school.models.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(String name, String color);
}