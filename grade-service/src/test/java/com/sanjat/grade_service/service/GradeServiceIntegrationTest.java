package com.sanjat.grade_service.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import com.sanjat.grade_service.dtos.GradeDto;
import com.sanjat.grade_service.model.Grade;
import com.sanjat.grade_service.repository.GradeRepository;

@SpringBootTest
@Testcontainers
@WireMockTest(httpPort = 8089)
public class GradeServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("enrollment.service.url", () -> "http://localhost:8089");
    }

    @Autowired
    private GradeService gradeService;

    @Autowired
    private GradeRepository gradeRepository;

    @BeforeEach
    void setUp() {
        gradeRepository.deleteAll();
    }

    @Test
    void giveGrade_ShouldSaveGradeAndUpdateEnrollmentStatus() {
        // Mock EnrollmentProxy response
        stubFor(get(urlEqualTo("/api/enrollments/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "id": 1,
                                    "studentId": 1,
                                    "courseId": 1,
                                    "status": "POHADJA"
                                }
                                """)));

        stubFor(put(urlEqualTo("/api/enrollments/1/status"))
                .willReturn(aResponse().withStatus(200)));

        stubFor(get(urlEqualTo("/api/enrollments/1/course"))
                .willReturn(aResponse().withBody("Math")));

        stubFor(get(urlEqualTo("/api/enrollments/1/student-email"))
                .willReturn(aResponse().withBody("student@example.com")));

        // Act
        GradeDto request = new GradeDto();
        request.setEnrollmentId(1L);
        request.setPoints(85.0);
        Grade result = gradeService.giveGrade(request);

        // Assert
        assertNotNull(result.getGradeId());
        assertEquals(9, result.getGrade());
        assertEquals(1L, result.getEnrollmentId());
    }

    @Test
    void getGradesByCourseId_ShouldReturnGradesFromDatabase() {
        // Mock enrollments
        stubFor(get(urlEqualTo("/api/enrollments/course/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                [
                                    {"id": 1, "studentId": 1, "courseId": 1, "status": "POHADJA"},
                                    {"id": 2, "studentId": 2, "courseId": 1, "status": "POHADJA"}
                                ]
                                """)));

        // Add grades to DB
        Grade grade1 = new Grade();
        grade1.setEnrollmentId(1L);
        grade1.setPoints(80.0);
        grade1.setGrade(8);
        gradeRepository.save(grade1);

        Grade grade2 = new Grade();
        grade2.setEnrollmentId(2L);
        grade2.setPoints(90.0);
        grade2.setGrade(9);
        gradeRepository.save(grade2);

        // Act
        List<Grade> result = gradeService.getGradesByCourseId(1L);

        // Assert
        assertEquals(2, result.size());
        assertEquals(8, result.get(0).getGrade());
        assertEquals(9, result.get(1).getGrade());
    }
}