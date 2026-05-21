package com.school.services;

import com.school.models.Student;
import com.school.models.Faculty;
import com.school.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public List<String> getStudentNamesStartingWithA() {
        logger.info("Was invoked method for get student names starting with A");
        List<Student> students = studentRepository.findAll();

        List<String> result = students.stream()
                .map(Student::getName)
                .filter(name -> name.startsWith("А") || name.startsWith("A"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());

        logger.debug("Found {} students with name starting with A", result.size());
        return result;
    }

    public double getAverageAgeUsingStream() {
        logger.info("Was invoked method for get average age using stream");
        List<Student> students = studentRepository.findAll();

        double average = students.stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);

        logger.debug("Average age: {}", average);
        return average;
    }

    public long getParallelStreamSum() {
        logger.info("Was invoked method for parallel stream sum");
        long sum = Stream.iterate(1L, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0L, Long::sum);

        logger.debug("Parallel sum result: {}", sum);
        return sum;
    }

    public void printStudentsParallel() {
        logger.info("Was invoked method for print students in parallel mode");
        List<Student> students = studentRepository.findAll();

        if (students.size() < 6) {
            logger.warn("Not enough students for parallel printing. Need at least 6, but found {}", students.size());
            return;
        }

        System.out.println("main-thread - student 1: " + students.get(0).getName());
        System.out.println("main-thread - student 2: " + students.get(1).getName());

        Thread thread1 = new Thread(() -> {
            System.out.println("thread1 - student 3: " + students.get(2).getName());
            System.out.println("thread1 - student 4: " + students.get(3).getName());
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("thread2 - student 5: " + students.get(4).getName());
            System.out.println("thread2 - student 6: " + students.get(5).getName());
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            logger.error("Thread interrupted: {}", e.getMessage());
        }

        logger.info("Parallel printing completed");
    }

    private synchronized void printStudentNameSynchronized(String threadName, String studentName) {
        System.out.println(threadName + " - " + studentName);
    }

    public void printStudentsSynchronized() {
        logger.info("Was invoked method for print students in synchronized mode");
        List<Student> students = studentRepository.findAll();

        if (students.size() < 6) {
            logger.warn("Not enough students for synchronized printing. Need at least 6, but found {}", students.size());
            return;
        }

        printStudentNameSynchronized("main-thread", students.get(0).getName());
        printStudentNameSynchronized("main-thread", students.get(1).getName());

        Thread thread1 = new Thread(() -> {
            printStudentNameSynchronized("synchronized-thread1", students.get(2).getName());
            printStudentNameSynchronized("synchronized-thread1", students.get(3).getName());
        });

        Thread thread2 = new Thread(() -> {
            printStudentNameSynchronized("synchronized-thread2", students.get(4).getName());
            printStudentNameSynchronized("synchronized-thread2", students.get(5).getName());
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            logger.error("Thread interrupted: {}", e.getMessage());
        }

        logger.info("Synchronized printing completed");
    }
}