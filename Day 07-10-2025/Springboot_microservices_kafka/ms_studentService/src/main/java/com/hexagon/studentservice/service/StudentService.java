package com.hexagon.studentservice.service;

import com.hexagon.studentservice.dto.School;
import com.hexagon.studentservice.dto.StudentResponse;
import com.hexagon.studentservice.model.Student;
import com.hexagon.studentservice.repository.StudentRepository;
import com.hexagon.studentservice.feign.SchoolClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    
    @Autowired
    private SchoolClient schoolClient;

    public ResponseEntity<?> createStudent(Student student) {
        try {
            return new ResponseEntity<>(studentRepository.save(student), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> fetchStudentById(Long id) {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
//            School school = restTemplate.getForObject("http://SCHOOL-SERVICE/school/" + id, School.class);
            School school = schoolClient.method1(id);

            StudentResponse response = new StudentResponse(
                    String.valueOf(student.getId()),
                    student.getName(),
                    student.getAge(),
                    student.getGender(),
                    school
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No Student Found", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> fetchStudents() {
        List<Student> students = studentRepository.findAll();
        if (!students.isEmpty()) {
            return new ResponseEntity<>(students, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No Students", HttpStatus.NOT_FOUND);
        }
    }
}
