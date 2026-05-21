package com.school.test;

import com.school.controllers.StudentController;
import com.school.models.Student;
import com.school.services.StudentService;
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

@WebMvcTest(StudentController.class)
public class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Test
    public void testCreateStudent() throws Exception {
        Student student = new Student("Тест Студент", 20);
        student.setId(1L);
        when(studentService.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Тест Студент\",\"age\":20}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Тест Студент"))
                .andExpect(jsonPath("$.age").value(20));
    }

    @Test
    public void testGetStudentById() throws Exception {
        Student student = new Student("Гарри Поттер", 11);
        student.setId(1L);
        when(studentService.findById(1L)).thenReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(11));
    }

    @Test
    public void testGetStudentByIdNotFound() throws Exception {
        when(studentService.findById(999L)).thenReturn(null);
        mockMvc.perform(get("/student/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllStudents() throws Exception {
        List<Student> students = Arrays.asList(
                new Student("Гарри Поттер", 11),
                new Student("Гермиона Грейнджер", 11)
        );
        when(studentService.findAll()).thenReturn(students);

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testUpdateStudent() throws Exception {
        Student student = new Student("Обновлённый Студент", 25);
        student.setId(1L);
        when(studentService.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Обновлённый Студент\",\"age\":25}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Обновлённый Студент"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetStudentsByAgeBetween() throws Exception {
        List<Student> students = Arrays.asList(
                new Student("Гарри Поттер", 11),
                new Student("Рон Уизли", 11)
        );
        when(studentService.findByAgeBetween(10, 20)).thenReturn(students);

        mockMvc.perform(get("/student/age?min=10&max=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}