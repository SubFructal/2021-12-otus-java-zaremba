package ru.otus.grpc_homework.service;


import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.generated.NumberRequest;
import ru.otus.protobuf.generated.NumberResponse;
import ru.otus.protobuf.generated.NumbersServiceGrpc;

import java.util.concurrent.atomic.AtomicLong;


public class NumbersServiceImpl extends NumbersServiceGrpc.NumbersServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(NumbersServiceImpl.class);

    @Override
    public void getStreamFromServer(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        log.info("server started, firstValue:{}, lastValue:{}", request.getFirstValue(), request.getLastValue());
        var currentValue = new AtomicLong(request.getFirstValue());

        for (int i = 0; i < request.getLastValue(); i++) {
            var value = currentValue.incrementAndGet();
            var responseNumber = NumberResponse.newBuilder().setNumber(value).build();
            responseObserver.onNext(responseNumber);
            sleep();
        }
        responseObserver.onCompleted();
        log.info("server finished");
    }

    private static void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
