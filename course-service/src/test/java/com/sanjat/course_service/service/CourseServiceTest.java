package com.sanjat.course_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sanjat.course_service.model.Course;
import com.sanjat.course_service.repository.CourseRepository;

public class CourseServiceTest {
    private CourseRepository repository;
    private CourseService service;

    @BeforeEach
    void setUp() {
        repository = mock(CourseRepository.class);
        service = new CourseService(repository);
    }

    @Test
    void testFindAll() {
        when(repository.findAll()).thenReturn(List.of(new Course(), new Course()));

        List<Course> result = service.findAll();

        assertEquals(2, result.size());
        verify(repository).findAll();
    }

    @Test
    void testFindByIdExists() {
        Course course = new Course();
        course.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(course));

        Optional<Course> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testFindByIdNotExists() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Course> result = service.findById(99L);

        assertFalse(result.isPresent());
    }

    @Test
    void testSave() {
        Course course = new Course();
        when(repository.save(course)).thenReturn(course);

        Course result = service.save(course);

        assertEquals(course, result);
    }

    @Test
    void testDeleteById() {
        service.deleteById(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void testHasVacancyTrue() {
        Course course = new Course();
        course.setCapacity(30);
        when(repository.findById(1L)).thenReturn(Optional.of(course));

        boolean result = service.hasVacancy(1L, 20);

        assertTrue(result);
    }

    @Test
    void testHasVacancyFalse() {
        Course course = new Course();
        course.setCapacity(20);
        when(repository.findById(1L)).thenReturn(Optional.of(course));

        boolean result = service.hasVacancy(1L, 25);

        assertFalse(result);
    }

    @Test
    void testHasVacancyCourseNotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        boolean result = service.hasVacancy(2L, 5);

        assertFalse(result);
    }

    @Test
    void testIsApplicationPeriodOpenTrue() {
        Course course = new Course();
        course.setApplicationStart(LocalDate.now().minusDays(1));
        course.setApplicationEnd(LocalDate.now().plusDays(1));
        when(repository.findById(1L)).thenReturn(Optional.of(course));

        boolean result = service.isApplicationPeriodOpen(1L);

        assertTrue(result);
    }

    @Test
    void testIsApplicationPeriodOpenFalse() {
        Course course = new Course();
        course.setApplicationStart(LocalDate.now().minusDays(10));
        course.setApplicationEnd(LocalDate.now().minusDays(1));
        when(repository.findById(1L)).thenReturn(Optional.of(course));

        boolean result = service.isApplicationPeriodOpen(1L);

        assertFalse(result);
    }

    @Test
    void testIsApplicationPeriodOpenCourseNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        boolean result = service.isApplicationPeriodOpen(1L);

        assertFalse(result);
    }

    @Test
    void testHasMinimumPointsTrue() {
        Course course = new Course();
        course.setMinPoints(70);
        when(repository.findById(1L)).thenReturn(Optional.of(course));

        boolean result = service.hasMinimumPoints(1L, 80);

        assertTrue(result);
    }

    @Test
    void testHasMinimumPointsFalse() {
        Course course = new Course();
        course.setMinPoints(70);
        when(repository.findById(1L)).thenReturn(Optional.of(course));

        boolean result = service.hasMinimumPoints(1L, 60);

        assertFalse(result);
    }

    @Test
    void testHasMinimumPointsCourseNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        boolean result = service.hasMinimumPoints(1L, 60);

        assertFalse(result);
    }

    @Test
    void testFindAllWithOpenApplicationPeriod() {
        LocalDate now = LocalDate.now();
        Course openCourse = new Course();
        openCourse.setApplicationStart(now.minusDays(1));
        openCourse.setApplicationEnd(now.plusDays(1));

        Course closedCourse = new Course();
        closedCourse.setApplicationStart(now.minusDays(10));
        closedCourse.setApplicationEnd(now.minusDays(5));

        when(repository.findAll()).thenReturn(List.of(openCourse, closedCourse));

        List<Course> result = service.findAllWithOpenApplicationPeriod();

        assertEquals(1, result.size());
        assertEquals(openCourse, result.get(0));
    }

    @Test
    void testGetCourseIdByNameFound() {
        Course course = new Course();
        course.setId(1L);
        when(repository.findByName("Java")).thenReturn(Optional.of(course));

        Long result = service.getCourseIdByName("Java");

        assertEquals(1L, result);
    }

    @Test
    void testGetCourseIdByNameNotFound() {
        when(repository.findByName("Python")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.getCourseIdByName("Python");
        });

        assertEquals("Kurs sa datim imenom ne postoji!", exception.getMessage());
    }
}
