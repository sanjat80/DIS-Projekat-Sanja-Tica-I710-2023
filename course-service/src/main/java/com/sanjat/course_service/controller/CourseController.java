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
    public ResponseEntity<List<Course>> getAllCourses() {
        try {
            return ResponseEntity.ok(service.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        ResponseEntity<?> response;

        try {
            Course course = service.findById(id).orElse(null);
            if (course == null) {
                response = ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Kurs sa id-em: " + id + " nije pronadjen.");
            } else {
                response = ResponseEntity.ok(course);
            }
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Desila se greska prilikom dohvatanja kursa.");
        }

        return response;
    }

    @PostMapping
    public ResponseEntity<?> createCourse(@RequestBody Course course) {
        try {
            Course saved = service.save(course);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Greska pri kreiranju kursa: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        ResponseEntity<?> response;

        try {
            Course existing = service.findById(id).orElse(null);
            if (existing == null) {
                response = ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Kurs sa id-em: " + id + " nije pronadjen.");
            } else {
                existing.setName(courseDetails.getName());
                existing.setDescription(courseDetails.getDescription());
                existing.setApplicationEnd(courseDetails.getApplicationEnd());
                existing.setApplicationStart(courseDetails.getApplicationStart());
                existing.setCapacity(courseDetails.getCapacity());
                existing.setDurationInClasses(courseDetails.getDurationInClasses());
                existing.setEspb_Points(courseDetails.getEspb_Points());
                existing.setMinPoints(courseDetails.getMinPoints());
                existing.setProfessor(courseDetails.getProfessor());
                existing.setSemester(courseDetails.getSemester());

                Course updated = service.save(existing);
                response = ResponseEntity.ok(updated);
            }
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska prilikom azuriranja kursa: " + e.getMessage());
        }

        return response;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        ResponseEntity<?> response;

        try {
            Course course = service.findById(id).orElse(null);
            if (course == null) {
                response = ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Kurs sa id-em:  " + id + " nije pronadjen.");
            } else {
                service.deleteById(id);
                response = ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri brisanju kursa: " + e.getMessage());
        }

        return response;
    }

    // this endpoint is being used by enrollment service
    @GetMapping("/internal/open-applications")
    public ResponseEntity<?> getCoursesWithOpenApplications() {
        ResponseEntity<?> response;

        try {
            List<Course> openCourses = service.findAllWithOpenApplicationPeriod();
            response = ResponseEntity.ok(openCourses);
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri dohvatanju kurseva: " + e.getMessage());
        }

        return response;
    }

    // this endpoint is being used by enrollment service
    @GetMapping("/internal/{id}/has-capacity")
    public ResponseEntity<?> hasCapacity(@PathVariable Long id, @RequestParam int currentEnrolled) {
        ResponseEntity<?> response;

        try {
            boolean vacancy = service.hasCapacity(id, currentEnrolled);
            response = ResponseEntity.ok(vacancy);
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri provjeravanju kapaciteta : " + e.getMessage());
        }

        return response;
    }

    // this endpoint is being used by enrollment service
    @GetMapping("/internal/{id}/min-points")
    public ResponseEntity<?> hasMinimumPoints(@PathVariable Long id, @RequestParam int points) {
        ResponseEntity<?> response;

        try {
            boolean hasMin = service.hasMinimumPoints(id, points);
            response = ResponseEntity.ok(hasMin);
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Greska pri provjeravanju minimalnog broja poena: " + e.getMessage());
        }

        return response;
    }

    // this endpoint is being used by enrollment service
    @GetMapping("/internal/id")
    public ResponseEntity<?> getCourseIdByName(@RequestParam String name) {
        Long courseId = service.getCourseIdByName(name);
        if (courseId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Kurs: '" + name + "' nije pronadjen.");
        }
        return ResponseEntity.ok(courseId);
    }

}
