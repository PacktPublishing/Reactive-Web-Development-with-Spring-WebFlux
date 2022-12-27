package com.packt.reactivewebdevelopment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class FunctionalEndpointController {

    @Bean
    public RouterFunction<ServerResponse> functionalEndpoint(FunctionalEndpointHandler functionalEndpointHandler) {
        return route()
                .GET("/functional-endpoint", accept(APPLICATION_JSON), functionalEndpointHandler::getFunctionalEndpoint)
                .build();
    }
}
