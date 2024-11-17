package com.tcp.ReactiveTcpListener.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;

@Service
public class ReactiveTcpClient {

    private static final Logger logger = LoggerFactory.getLogger(ReactiveTcpClient.class);
    private final Mono<? extends Connection> connectionMono;

    @Autowired
    public ReactiveTcpClient(Mono<? extends Connection> connectionMono) {
        this.connectionMono = connectionMono;
    }

    public Mono<String> sendMessageWithResponse(String message) {
        return sendMessage(message)
                .then(Mono.just(message))
                .doOnNext(response -> logger.info("Client: Message sent"))
                .onErrorResume(error -> {
                    logger.error("Client: Failed to send message: {}", error.getMessage());
                    return Mono.just("Failed to send message: " + error.getMessage());
                });
    }

    public Mono<Void> sendMessage(String message) {
        return connectionMono.flatMap(connection ->
                        connection.outbound()
                                .sendString(Mono.just(message))
                                .then()
                )
                .doOnError(error -> {
                    logger.error("Error during message send: {}", error.getMessage());
                });
    }
}
