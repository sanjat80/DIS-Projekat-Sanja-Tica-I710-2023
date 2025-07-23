package com.sanjat.course_service.init;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sanjat.course_service.model.Course;
import com.sanjat.course_service.repository.CourseRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CourseRepository courseRepository;

    public DataInitializer(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (courseRepository.count() == 0) {
            Course course1 = new Course();
            course1.setName("Matematika 1");
            course1.setDescription("Osnovni kurs matematike za prvi semestar.");
            course1.setDurationInClasses(45);
            course1.setSemester(1);
            course1.setEspb_Points(6);
            course1.setProfessor("Vesna Mihajlovic");
            course1.setApplicationStart(LocalDate.of(2025, 07, 01));
            course1.setApplicationEnd(LocalDate.of(2025, 07, 30));
            course1.setCapacity(20);
            courseRepository.save(course1);

            Course course2 = new Course();
            course2.setName("Programiranje 1");
            course2.setDescription("Uvod u programske jezike i osnove programiranja.");
            course2.setDurationInClasses(60);
            course2.setSemester(1);
            course2.setEspb_Points(7);
            course2.setProfessor("Petar Jankovic");
            course2.setApplicationStart(LocalDate.of(2025, 07, 01));
            course2.setApplicationEnd(LocalDate.of(2025, 07, 30));
            course2.setCapacity(20);

            courseRepository.save(course2);

            Course course3 = new Course();
            course3.setName("Fizika 1");
            course3.setDescription("Osnovni koncepti fizike za studente tehničkih nauka.");
            course3.setDurationInClasses(45);
            course3.setSemester(1);
            course3.setEspb_Points(6);
            course3.setProfessor("Mihajlo Doderovic");
            course3.setApplicationStart(LocalDate.of(2025, 07, 01));
            course3.setApplicationEnd(LocalDate.of(2025, 07, 30));
            course3.setCapacity(20);

            courseRepository.save(course3);
        }
    }
}