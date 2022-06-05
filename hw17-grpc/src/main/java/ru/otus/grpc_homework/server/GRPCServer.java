package ru.otus.grpc_homework.server;

import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc_homework.service.NumbersServiceImpl;

import java.io.IOException;

public class GRPCServer {

    public static final int SERVER_PORT = 8190;
    private static final Logger log = LoggerFactory.getLogger(GRPCServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        var numbersService = new NumbersServiceImpl();

        var server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(numbersService).build();
        server.start();
        log.info("server waiting for client connections...");
        server.awaitTermination();
    }
}
