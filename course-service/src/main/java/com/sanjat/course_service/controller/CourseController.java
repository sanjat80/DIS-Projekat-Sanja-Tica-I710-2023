package com.sanjat.course_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sanjat.course_service.model.Course;
import com.sanjat.course_service.service.CourseService;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Course createCourse(@RequestBody Course course) {
        return service.save(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        return service.findById(id)
                .map(course -> {
                    course.setName(courseDetails.getName());
                    course.setDescription(courseDetails.getDescription());
                    course.setApplicationEnd(courseDetails.getApplicationEnd());
                    course.setApplicationStart(courseDetails.getApplicationStart());
                    course.setCapacity(courseDetails.getCapacity());
                    course.setDurationInClasses(courseDetails.getDurationInClasses());
                    course.setEspb_Points(courseDetails.getEspb_Points());
                    course.setMinPoints(courseDetails.getMinPoints());
                    course.setProfessor(courseDetails.getProfessor());
                    course.setSemester(courseDetails.getSemester());
                    return ResponseEntity.ok(service.save(course));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        if (service.findById(id).isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // this endpoint is being used by enrollment service
    @GetMapping("/open-applications")
    public ResponseEntity<List<Course>> getCoursesWithOpenApplications() {
        List<Course> openCourses = service.findAllWithOpenApplicationPeriod();
        return ResponseEntity.ok(openCourses);
    }

    // this endpoint is being used by enrollment service
    @GetMapping("/{id}/has-vacancy")
    public ResponseEntity<Boolean> hasVacancy(@PathVariable Long id, @RequestParam int currentEnrolled) {
        boolean vacancy = service.hasVacancy(id, currentEnrolled);
        return ResponseEntity.ok(vacancy);
    }

    // this endpoint is being used by enrollment service
    @GetMapping("/{id}/min-points")
    public ResponseEntity<Boolean> hasMinimumPoints(@PathVariable Long id, @RequestParam int points) {
        boolean hasMin = service.hasMinimumPoints(id, points);
        return ResponseEntity.ok(hasMin);
    }

    // this endpoint is being used by enrollment service
    @GetMapping("/id")
    public ResponseEntity<Long> getCourseIdByName(@RequestParam String name) {
        try {
            Long courseId = service.getCourseIdByName(name);
            return ResponseEntity.ok(courseId);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
