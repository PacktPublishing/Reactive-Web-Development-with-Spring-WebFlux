package com.packt.webfluxapistudents.service;

import com.packt.webfluxapistudents.model.Student;
import com.packt.webfluxapistudents.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentService {

    @Autowired
    private final StudentRepository studentRepository;

    public Mono<Student> saveStudent(Student student) {
        return studentRepository.save(student)
                .doOnError(error -> log.error("An error occurred while saving the student in the base", error));
    }

    public Flux<Student> getAllStudents() {
        return studentRepository.findAll();

    }


    public Mono<Student> getStudentById(String studentId) {
        return studentRepository.findById(studentId)
                .switchIfEmpty(StudentNotFoundException());

    }


    public <T> Mono<T> StudentNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not  found"));
    }

    public Mono<Student> updateStudent(final String id, final Student student) {
        return studentRepository.save(student);
    }

    public Mono<Void> deleteStudent(String studentId) {
        return getStudentById(studentId)
                .flatMap(studentRepository::delete);
    }



}
