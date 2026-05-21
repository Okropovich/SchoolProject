package com.school.services;

import com.school.models.Student;
import com.school.models.Faculty;
import com.school.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student save(Student student) {
        logger.info("Was invoked method for create/update student");
        logger.debug("Saving student: {}", student.getName());
        Student saved = studentRepository.save(student);
        logger.info("Student saved with id: {}", saved.getId());
        return saved;
    }

    public Student findById(Long id) {
        logger.info("Was invoked method for find student by id: {}", id);
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            logger.error("There is not student with id = " + id);
            return null;
        }
        logger.debug("Found student: {}", student.getName());
        return student;
    }

    public List<Student> findAll() {
        logger.info("Was invoked method for get all students");
        List<Student> students = studentRepository.findAll();
        logger.debug("Found {} students", students.size());
        return students;
    }

    public void deleteById(Long id) {
        logger.info("Was invoked method for delete student by id: {}", id);
        if (!studentRepository.existsById(id)) {
            logger.warn("Attempt to delete non-existing student with id: {}", id);
        }
        studentRepository.deleteById(id);
        logger.debug("Student with id {} deleted", id);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for find students by age between {} and {}", min, max);
        List<Student> students = studentRepository.findByAgeBetween(min, max);
        logger.debug("Found {} students in age range", students.size());
        return students;
    }

    public Faculty getStudentFaculty(Long id) {
        logger.info("Was invoked method for get faculty of student with id: {}", id);
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            logger.error("Cannot get faculty: student with id {} not found", id);
            return null;
        }
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            logger.warn("Student with id {} has no faculty assigned", id);
        }
        return faculty;
    }

    public long getTotalStudentsCount() {
        logger.info("Was invoked method for get total students count");
        long count = studentRepository.getTotalStudentsCount();
        logger.debug("Total students count: {}", count);
        return count;
    }

    public double getAverageStudentAge() {
        logger.info("Was invoked method for get average student age");
        double avg = studentRepository.getAverageStudentAge();
        logger.debug("Average student age: {}", avg);
        return avg;
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        List<Student> students = studentRepository.findLastFiveStudents();
        logger.debug("Found {} last students", students.size());
        return students;
    }
}