package ru.otus.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dto.ClientDto;

@RestController
@Slf4j
public class ClientServiceRestController {

    private final WebClient webClient;

    public ClientServiceRestController(WebClient.Builder builder) {
        webClient = builder
                .baseUrl("http://localhost:8080")
                .build();
    }

    @GetMapping(value = "/allClients", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ClientDto> getAllClients() {
        log.info("request for get all clients");
        return webClient.get().uri("/api/clients")
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(ClientDto.class)
                .doOnNext(client -> log.info("received client: {}", client));
    }


    @PostMapping(value = "/api/client", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ClientDto> saveClient(@RequestBody ClientDto clientDto) {
        log.info("request for save client " + clientDto);
        return webClient.post().uri("/api/client")
                .bodyValue(clientDto)
                .retrieve()
                .bodyToMono(ClientDto.class)
                .doOnNext(client -> log.info("received client: {}", client));
    }
}
