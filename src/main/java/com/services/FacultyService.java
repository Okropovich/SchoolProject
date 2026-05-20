package com.school.services;

import com.school.models.Faculty;
import com.school.repositories.FacultyRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    public FacultyService(FacultyRepository facultyRepository) { this.facultyRepository = facultyRepository; }
    public Faculty save(Faculty faculty) { return facultyRepository.save(faculty); }
    public Faculty findById(Long id) { return facultyRepository.findById(id).orElse(null); }
    public List<Faculty> findAll() { return facultyRepository.findAll(); }
    public void deleteById(Long id) { facultyRepository.deleteById(id); }
}