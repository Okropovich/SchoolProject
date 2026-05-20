package com.school.controllers;

import com.school.models.Student;
import com.school.services.StudentService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;
    public StudentController(StudentService studentService) { this.studentService = studentService; }
    @PostMapping public Student create(@RequestBody Student student) { return studentService.save(student); }
    @GetMapping("/{id}") public Student get(@PathVariable Long id) { return studentService.findById(id); }
    @GetMapping public List<Student> getAll() { return studentService.findAll(); }
    @PutMapping("/{id}") public Student update(@PathVariable Long id, @RequestBody Student student) { student.setId(id); return studentService.save(student); }
    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) { studentService.deleteById(id); }
}