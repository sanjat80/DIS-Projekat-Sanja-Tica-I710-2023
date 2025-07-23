package com.sanjat.enrollment_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sanjat.enrollment_service.model.Enrollment;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long userId);

    List<Enrollment> findByCourseId(Long courseId);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    void deleteByStudentIdAndCourseId(Long studentId, Long courseId);

    int countByCourseId(Long courseId);

    int countByStudentIdAndCourseId(Long studentId, Long courseId);

    Optional<Enrollment> findById(Long id);

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    @Query("SELECT e.studentId FROM Enrollment e WHERE e.id = :enrollmentId")
    Optional<Long> findStudentIdByEnrollmentId(@Param("enrollmentId") Long enrollmentId);
}
