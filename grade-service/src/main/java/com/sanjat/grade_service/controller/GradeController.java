package com.sanjat.grade_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sanjat.grade_service.dtos.GradeDto;
import com.sanjat.grade_service.dtos.GradeUpdateDto;
import com.sanjat.grade_service.model.Grade;
import com.sanjat.grade_service.service.GradeService;

import feign.FeignException;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/grades")
public class GradeController {

    private GradeService service;

    public GradeController(GradeService service) {
        this.service = service;
    }

    /*
     * @PostMapping
     * public ResponseEntity<Grade> createGrade(@RequestBody GradeDto dto) {
     * Grade createdGrade = service.giveGrade(dto);
     * return new ResponseEntity<>(createdGrade, HttpStatus.CREATED);
     * }
     */
    @PostMapping
    public ResponseEntity<?> createGrade(@RequestBody GradeDto grade) {
        // try {
        Grade createdGrade = service.giveGrade(grade);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGrade);
        /*
         * } catch (FeignException.NotFound ex) {
         * return ResponseEntity.status(HttpStatus.BAD_REQUEST)
         * .body(Map.of(
         * "error", "Invalid enrollment reference",
         * "details", "The referenced enrollment does not exist"));
         * } catch (FeignException ex) {
         * return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
         * .body(Map.of(
         * "error", "Enrollment service unavailable",
         * "details", "Could not communicate with enrollment service"));
         * } catch (Exception ex) {
         * return ResponseEntity.internalServerError()
         * .body(Map.of(
         * "error", "Internal server error",
         * "message", ex.getMessage()));
         * }
         */
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGrade(@PathVariable Long id, @RequestBody GradeUpdateDto newGrade) {
        Grade updatedGrade = service.updateGrade(id, newGrade);
        return ResponseEntity.ok(updatedGrade);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        if (service.getById(id).isPresent()) {
            service.deleteGrade(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getGradesByCourseId(@PathVariable Long courseId) {
        try {
            List<Grade> grades = service.getGradesByCourseId(courseId);
            return grades.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(grades);
        } catch (FeignException.NotFound ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Error retrieving grades");
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getGradesByStudentId(@PathVariable Long studentId) {
        try {
            List<Grade> grades = service.getGradesByStudentId(studentId);
            return grades.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(grades);

        } catch (FeignException.NotFound ex) {
            return ResponseEntity.notFound().build();
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Error retrieving student grades");
        }
    }

    @GetMapping()
    public ResponseEntity<?> getAllGrades() {
        try {
            List<Grade> grades = service.getAllGrades();
            return grades.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(grades);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Error retrieving student grades");
        }
    }

}
