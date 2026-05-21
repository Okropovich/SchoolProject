package com.school.services;

import com.school.models.Faculty;
import com.school.models.Student;
import com.school.repositories.FacultyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;

@Service
public class FacultyService {
    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty save(Faculty faculty) {
        logger.info("Was invoked method for create/update faculty");
        logger.debug("Saving faculty: {}", faculty.getName());
        Faculty saved = facultyRepository.save(faculty);
        logger.info("Faculty saved with id: {}", saved.getId());
        return saved;
    }

    public Faculty findById(Long id) {
        logger.info("Was invoked method for find faculty by id: {}", id);
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            logger.error("There is not faculty with id = " + id);
            return null;
        }
        logger.debug("Found faculty: {}", faculty.getName());
        return faculty;
    }

    public List<Faculty> findAll() {
        logger.info("Was invoked method for get all faculties");
        List<Faculty> faculties = facultyRepository.findAll();
        logger.debug("Found {} faculties", faculties.size());
        return faculties;
    }

    public void deleteById(Long id) {
        logger.info("Was invoked method for delete faculty by id: {}", id);
        if (!facultyRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existing faculty with id: {}", id);
        }
        facultyRepository.deleteById(id);
        logger.debug("Faculty with id {} deleted", id);
    }

    public List<Faculty> searchByNameOrColor(String query) {
        logger.info("Was invoked method for search faculty by name or color with query: {}", query);
        List<Faculty> faculties = facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(query, query);
        logger.debug("Found {} faculties matching query", faculties.size());
        return faculties;
    }

    public List<Student> getFacultyStudents(Long id) {
        logger.info("Was invoked method for get students of faculty with id: {}", id);
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            logger.error("Faculty with id {} not found", id);
            return null;
        }
        List<Student> students = faculty.getStudents();
        logger.debug("Faculty {} has {} students", faculty.getName(), students != null ? students.size() : 0);
        return students;
    }

    public String getLongestFacultyName() {
        logger.info("Was invoked method for get longest faculty name");
        List<Faculty> faculties = facultyRepository.findAll();

        String longestName = faculties.stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("");

        logger.debug("Longest faculty name: {} (length: {})", longestName, longestName.length());
        return longestName;
    }
}