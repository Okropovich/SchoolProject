package com.school.test;

import com.school.models.Faculty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestRestTemplate {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

    @Test
    public void testCreateFaculty() {
        Faculty faculty = new Faculty("Тест Факультет", "blue");
        ResponseEntity<Faculty> response = restTemplate.postForEntity(getBaseUrl() + "/faculty", faculty, Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Тест Факультет", response.getBody().getName());
        assertEquals("blue", response.getBody().getColor());
    }

    @Test
    public void testGetFacultyById() {

        Faculty faculty = new Faculty("Для поиска", "red");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(getBaseUrl() + "/faculty", faculty, Faculty.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Faculty> response = restTemplate.getForEntity(getBaseUrl() + "/faculty/" + id, Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetFacultyByIdNotFound() {
        ResponseEntity<Faculty> response = restTemplate.getForEntity(getBaseUrl() + "/faculty/999", Faculty.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetAllFaculties() {
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(getBaseUrl() + "/faculty", Faculty[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateFaculty() {

        Faculty faculty = new Faculty("До обновления", "gray");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(getBaseUrl() + "/faculty", faculty, Faculty.class);
        Long id = createResponse.getBody().getId();

        Faculty updatedFaculty = new Faculty("После обновления", "green");
        HttpEntity<Faculty> entity = new HttpEntity<>(updatedFaculty);
        ResponseEntity<Faculty> response = restTemplate.exchange(getBaseUrl() + "/faculty/" + id, HttpMethod.PUT, entity, Faculty.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("После обновления", response.getBody().getName());
        assertEquals("green", response.getBody().getColor());
    }

    @Test
    public void testDeleteFaculty() {

        Faculty faculty = new Faculty("Для удаления", "gray");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(getBaseUrl() + "/faculty", faculty, Faculty.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Void> response = restTemplate.exchange(getBaseUrl() + "/faculty/" + id, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testSearchFacultyByNameOrColor() {
        ResponseEntity<Faculty[]> response = restTemplate.getForEntity(getBaseUrl() + "/faculty/search?query=гриф", Faculty[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}