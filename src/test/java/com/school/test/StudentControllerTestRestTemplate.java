package com.school.test;

import com.school.models.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void testCreateStudent() {
        Student student = new Student("Тест Студент", 20);
        ResponseEntity<Student> response = restTemplate.postForEntity(getBaseUrl() + "/student", student, Student.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Тест Студент", response.getBody().getName());
        assertEquals(20, response.getBody().getAge());
    }

    @Test
    public void testGetStudentById() {

        Student student = new Student("Для поиска", 15);
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(getBaseUrl() + "/student", student, Student.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Student> response = restTemplate.getForEntity(getBaseUrl() + "/student/" + id, Student.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetStudentByIdNotFound() {
        ResponseEntity<Student> response = restTemplate.getForEntity(getBaseUrl() + "/student/999", Student.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAllStudents() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(getBaseUrl() + "/student", Student[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateStudent() {

        Student student = new Student("До обновления", 10);
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(getBaseUrl() + "/student", student, Student.class);
        Long id = createResponse.getBody().getId();

        Student updatedStudent = new Student("После обновления", 25);
        HttpEntity<Student> entity = new HttpEntity<>(updatedStudent);
        ResponseEntity<Student> response = restTemplate.exchange(getBaseUrl() + "/student/" + id, HttpMethod.PUT, entity, Student.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("После обновления", response.getBody().getName());
        assertEquals(25, response.getBody().getAge());
    }

    @Test
    public void testDeleteStudent() {

        Student student = new Student("Для удаления", 10);
        ResponseEntity<Student> createResponse = restTemplate.postForEntity(getBaseUrl() + "/student", student, Student.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Void> response = restTemplate.exchange(getBaseUrl() + "/student/" + id, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetStudentsByAgeBetween() {
        ResponseEntity<Student[]> response = restTemplate.getForEntity(getBaseUrl() + "/student/age?min=10&max=20", Student[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}