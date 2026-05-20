package com.school.services;

import com.school.models.Student;
import com.school.models.Faculty;
import com.school.repositories.StudentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student save(Student student) { return studentRepository.save(student); }
    public Student findById(Long id) { return studentRepository.findById(id).orElse(null); }
    public List<Student> findAll() { return studentRepository.findAll(); }
    public void deleteById(Long id) { studentRepository.deleteById(id); }
    public List<Student> findByAgeBetween(int min, int max) { return studentRepository.findByAgeBetween(min, max); }
    public Faculty getStudentFaculty(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        return student != null ? student.getFaculty() : null;
    }
}