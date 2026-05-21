package com.school.repositories;

import com.school.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAgeBetween(int min, int max);


    @Query("SELECT COUNT(s) FROM Student s")
    long getTotalStudentsCount();


    @Query("SELECT AVG(s.age) FROM Student s")
    double getAverageStudentAge();


    @Query("SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> findLastFiveStudents();
}