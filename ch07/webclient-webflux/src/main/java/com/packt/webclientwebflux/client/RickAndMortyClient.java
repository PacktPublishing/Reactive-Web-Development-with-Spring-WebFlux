package com.packt.webclientwebflux.client;

import com.packt.webclientwebflux.response.CharacterResponse;
import com.packt.webclientwebflux.response.ListOfEpisodesResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@Service
@Slf4j
public class RickAndMortyClient {

    private final WebClient webClient;


    public RickAndMortyClient(WebClient.Builder builder) {
        webClient = builder.baseUrl("https://rickandmortyapi.com/api").build();

    }

    public Mono<CharacterResponse> findACharacterById(String id) {
        log.info("Buscando personagem com o id [{}]", id);
        return webClient
                .get()
                .uri("/character/" + id)
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CharacterResponse.class);
    }

    public Flux<ListOfEpisodesResponse> ListAllEpisodes() {
        log.info("Listing all episodes");
        return webClient
                .get()
                .uri("/episode")
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(ListOfEpisodesResponse.class);
    }


}