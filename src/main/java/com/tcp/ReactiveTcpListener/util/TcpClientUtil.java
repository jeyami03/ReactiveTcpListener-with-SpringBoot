package com.tcp.ReactiveTcpListener.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.tcp.TcpClient;

@Component
public class TcpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(TcpClientUtil.class);

    private final TcpClient tcpClient;

    public TcpClientUtil(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    public Mono<? extends Connection> connect() {
        return tcpClient.connect()
                .doOnSuccess(connection -> logger.info("Successfully connected to the server"))
                .doOnError(error -> logger.error("Error while connecting to the server: {}", error.getMessage()));
    }
}
