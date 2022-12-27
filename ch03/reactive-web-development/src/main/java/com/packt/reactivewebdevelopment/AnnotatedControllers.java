package com.packt.reactivewebdevelopment;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnnotatedControllers {

    @GetMapping("/annotated-controller")
    public String getAnnotatedControllers() {
        return "My First AnnotatedControllers ";
    }

}
