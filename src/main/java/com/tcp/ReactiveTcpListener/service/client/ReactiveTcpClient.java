package com.tcp.ReactiveTcpListener.service.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.tcp.ReactiveTcpListener.util.TcpClientUtil;
import reactor.netty.Connection;

@Service
public class ReactiveTcpClient {
    private static final Logger logger = LoggerFactory.getLogger(ReactiveTcpClient.class);
    private final Mono<? extends Connection> connectionMono;

    public ReactiveTcpClient(TcpClientUtil tcpClientUtil) {
        this.connectionMono = tcpClientUtil.connect()
                .doOnNext(connection -> {
                    connection.inbound().receive()
                            .asString()
                            .doOnNext(response -> logger.info("Client received: {}", response))
                            .subscribe();
                    logger.info("Client connected to server.");
                })
                .doOnError(error -> logger.error("Error connecting to server: {}", error.getMessage()))
                .cache();
    }

    public Mono<Void> sendMessage(String message) {
        return connectionMono.flatMap(connection ->
                connection.outbound()
                        .sendString(Mono.just(message))
                        .then()
        ).doOnError(error -> logger.error("Error during message send: {}", error.getMessage()));
    }
}