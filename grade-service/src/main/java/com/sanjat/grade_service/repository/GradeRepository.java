package com.sanjat.grade_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sanjat.grade_service.model.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByEnrollmentId(Long enrollmentId);

    List<Grade> findByEnrollmentIdIn(List<Long> enrollmentIds);

}
