package com.school.test;

import com.school.controllers.FacultyController;
import com.school.models.Faculty;
import com.school.services.FacultyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Test
    public void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty("Гриффиндор", "red");
        faculty.setId(1L);
        when(facultyService.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Гриффиндор\",\"color\":\"red\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    public void testGetFacultyById() throws Exception {
        Faculty faculty = new Faculty("Гриффиндор", "red");
        faculty.setId(1L);
        when(facultyService.findById(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    public void testGetFacultyByIdNotFound() throws Exception {
        when(facultyService.findById(999L)).thenReturn(null);
        mockMvc.perform(get("/faculty/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllFaculties() throws Exception {
        List<Faculty> faculties = Arrays.asList(
                new Faculty("Гриффиндор", "red"),
                new Faculty("Слизерин", "green")
        );
        when(facultyService.findAll()).thenReturn(faculties);

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testUpdateFaculty() throws Exception {
        Faculty faculty = new Faculty("Обновлённый Факультет", "blue");
        faculty.setId(1L);
        when(facultyService.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Обновлённый Факультет\",\"color\":\"blue\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Обновлённый Факультет"))
                .andExpect(jsonPath("$.color").value("blue"));
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearchFacultyByNameOrColor() throws Exception {
        List<Faculty> faculties = Arrays.asList(
                new Faculty("Гриффиндор", "red")
        );
        when(facultyService.searchByNameOrColor("гри")).thenReturn(faculties);

        mockMvc.perform(get("/faculty/search?query=гри"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"));
    }
}