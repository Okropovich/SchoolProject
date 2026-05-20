package com.school.controllers;

import com.school.models.Faculty;
import com.school.models.Student;
import com.school.services.FacultyService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping public Faculty create(@RequestBody Faculty faculty) { return facultyService.save(faculty); }
    @GetMapping("/{id}") public Faculty get(@PathVariable Long id) { return facultyService.findById(id); }
    @GetMapping public List<Faculty> getAll() { return facultyService.findAll(); }
    @PutMapping("/{id}") public Faculty update(@PathVariable Long id, @RequestBody Faculty faculty) {
        faculty.setId(id);
        return facultyService.save(faculty);
    }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { facultyService.deleteById(id); }
    @GetMapping("/search") public List<Faculty> searchByNameOrColor(@RequestParam String query) {
        return facultyService.searchByNameOrColor(query);
    }
    @GetMapping("/{id}/students") public List<Student> getFacultyStudents(@PathVariable Long id) {
        return facultyService.getFacultyStudents(id);
    }
}