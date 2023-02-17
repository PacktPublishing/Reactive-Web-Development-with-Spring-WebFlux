package com.packt.webfluxapistudents.controller;


import com.packt.webfluxapistudents.model.Student;
import com.packt.webfluxapistudents.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
@Slf4j
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Student> createStudent(@RequestBody Student student) {
        log.info("Creating a new student with the information: {}", student);
        return studentService.saveStudent(student);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Student> getAllStudents() {
        log.info("Listing all registered students ");
        return studentService.getAllStudents();
    }

    @GetMapping("/{studentId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Student> getStudentById(@PathVariable String studentId) {
        log.info("Searching student with id  {}", studentId);
        return studentService.getStudentById(studentId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Student> updateStudent(@PathVariable String id, @RequestBody Student student) {
        log.info("Update the student with id {} and the infos {}", id, student);
        return studentService.updateStudent(id,student);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteStudent(@PathVariable String id) {
        log.info("Deletind student with id {}", id);
        return studentService.deleteStudent(id);
    }
}
