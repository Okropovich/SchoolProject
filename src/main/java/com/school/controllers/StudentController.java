package com.school.controllers;

import com.school.models.Student;
import com.school.models.Faculty;
import com.school.services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return studentService.save(student);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> get(@PathVariable Long id) {
        Student student = studentService.findById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping
    public List<Student> getAll() {
        return studentService.findAll();
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        return studentService.save(student);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        studentService.deleteById(id);
    }

    @GetMapping("/age")
    public List<Student> getStudentsByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return studentService.findByAgeBetween(min, max);
    }

    @GetMapping("/{id}/faculty")
    public Faculty getStudentFaculty(@PathVariable Long id) {
        return studentService.getStudentFaculty(id);
    }


    @GetMapping("/count")
    public long getTotalStudentsCount() {
        return studentService.getTotalStudentsCount();
    }

    @GetMapping("/average-age")
    public double getAverageStudentAge() {
        return studentService.getAverageStudentAge();
    }

    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }
}