package com.sanjat.grade_service.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.sanjat.grade_service.dtos.*;
import com.sanjat.grade_service.model.*;
import com.sanjat.grade_service.repository.EnrollmentProxy;
import com.sanjat.grade_service.repository.GradeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GradeServiceTest {

    @Mock
    private GradeRepository repository;

    @Mock
    private EnrollmentProxy proxy;

    @Mock
    private NotificationSender notificationSender;

    @InjectMocks
    private GradeService gradeService;

    private GradeDto gradeDto;
    private Enrollment activeEnrollment;

    @BeforeEach
    void setUp() {
        gradeDto = new GradeDto();
        gradeDto.setEnrollmentId(1L);
        gradeDto.setPoints(85.0);

        activeEnrollment = new Enrollment();
        activeEnrollment.setId(1L);
        activeEnrollment.setStatus(Status.POHADJA);
        activeEnrollment.setCourseId(1L);
        activeEnrollment.setEnrollmentDate(LocalDate.of(2025, 07, 10));
        activeEnrollment.setStudentId(3L);
        activeEnrollment.setNumberOfCourseAttempts(1);
    }

    @Test
    void giveGrade_ShouldSaveGrade_WhenEnrollmentIsActive() {
        // Arrange
        when(proxy.getEnrollmentById(1L)).thenReturn(activeEnrollment);

        when(proxy.getCourseByEnrollment(1L)).thenReturn("Matematika");
        when(proxy.getStudentEmailByEnrollmentId(1L)).thenReturn("student@example.com");
        when(repository.save(any(Grade.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Grade result = gradeService.giveGrade(gradeDto);

        // Assert
        assertNotNull(result);
        assertEquals(9, result.getGrade());
        assertEquals(1L, result.getEnrollmentId());
        verify(proxy).updateEnrollmentStatus(1L, Status.ZAVRSIO);
        verify(notificationSender).sendNotification(any(Notification.class));
    }

    @Test
    void giveGrade_ShouldThrowException_WhenEnrollmentNotActive() {
        // Arrange
        activeEnrollment.setStatus(Status.NIJE_POLOZIO);
        when(proxy.getEnrollmentById(1L)).thenReturn(activeEnrollment);

        // Act & Assert
        assertThrows(
                IllegalStateException.class,
                () -> gradeService.giveGrade(gradeDto),
                "Nije moguce ocjeniti studenta koji ne pohadja kurs!");
    }

    @Test
    void getGradesByCourseId_ShouldReturnGrades_WhenEnrollmentsExist() {
        Enrollment enrollment1 = new Enrollment(1L, 1L, 1L, Status.POHADJA, LocalDate.of(2025, 07, 05), 1);
        Enrollment enrollment2 = new Enrollment(2L, 2L, 1L, Status.POHADJA, LocalDate.of(2025, 07, 01), 2);
        when(proxy.getEnrollmentsForCourse(1L))
                .thenReturn(List.of(enrollment1, enrollment2));

        Grade grade1 = new Grade(1L, 1L, LocalDate.of(2025, 07, 23), 84, 9);
        Grade grade2 = new Grade(2L, 2L, LocalDate.of(2025, 07, 23), 88, 9);
        when(repository.findByEnrollmentIdIn(List.of(1L, 2L)))
                .thenReturn(List.of(grade1, grade2));

        // Act
        List<Grade> result = gradeService.getGradesByCourseId(1L);

        // Assert
        assertEquals(2, result.size());
        assertEquals(9, result.get(0).getGrade());
        assertEquals(9, result.get(1).getGrade());
    }

    @Test
    void getGradesByStudentId_ShouldFilterByStatusPOHADJA() {
        Enrollment inactiveEnrollment = new Enrollment();
        when(proxy.getEnrollmentsByStudentId(1L))
                .thenReturn(List.of(activeEnrollment, inactiveEnrollment));

        // Simuliramo ocjenu samo za aktivan enrollment
        Grade grade = new Grade(1L, 1L, LocalDate.of(2025, 07, 23), 85, 9);
        when(repository.findByEnrollmentIdIn(List.of(1L)))
                .thenReturn(List.of(grade));

        // Act
        List<Grade> result = gradeService.getGradesByStudentId(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(9, result.get(0).getGrade());
    }

    @Test
    void updateGrade_ShouldUpdatePointsAndGrade() {
        // Arrange
        Grade existingGrade = new Grade();
        existingGrade.setGradeId(1L);
        existingGrade.setEnrollmentId(1L);
        existingGrade.setPoints(70.0);
        existingGrade.setGrade(7);

        GradeUpdateDto updateDto = new GradeUpdateDto();
        updateDto.setPoints(90.0);

        when(repository.findById(1L)).thenReturn(Optional.of(existingGrade));
        when(repository.save(any(Grade.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Grade result = gradeService.updateGrade(1L, updateDto);

        // Assert
        assertEquals(9, result.getGrade());
        assertEquals(90.0, result.getPoints());
    }
}