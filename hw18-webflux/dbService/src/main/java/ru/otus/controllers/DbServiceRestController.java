package ru.otus.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.dto.ClientDto;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DbServiceRestController {

    private final DBServiceClient dbServiceClient;
    private final ExecutorService executor;

    @GetMapping(value = "/api/clients", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ClientDto> getAllClients() {
        log.info("request for get all clients");
        var future = CompletableFuture.supplyAsync(dbServiceClient::findAll, executor);
        return Mono.fromFuture(future)
                .flatMapMany(Flux::fromIterable)
                .map(ClientDto::new)
                .doOnNext(clientDto -> log.info("clientDTO: " + clientDto));
    }

    @PostMapping(value = "/api/client", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ClientDto> saveClient(@RequestBody ClientDto clientDto) {
        log.info("request for save client " + clientDto);
        var future = CompletableFuture
                .supplyAsync(() -> dbServiceClient.saveClient(clientDto), executor);
        return Mono.fromFuture(future)
                .map(ClientDto::new)
                .doOnNext(savedClient -> log.info("saved client: " + savedClient));
    }
}
