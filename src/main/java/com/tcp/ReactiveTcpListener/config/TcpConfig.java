package com.tcp.ReactiveTcpListener.config;

import com.tcp.ReactiveTcpListener.service.client.ReactiveTcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import reactor.core.publisher.Mono;
import reactor.netty.Connection;
import reactor.netty.tcp.TcpClient;
import reactor.netty.tcp.TcpServer;

@Configuration
public class TcpConfig {
    private static final Logger logger = LoggerFactory.getLogger(TcpConfig.class);
    private ReactiveTcpClient reactiveTcpClient;

    public TcpConfig(@Lazy ReactiveTcpClient reactiveTcpClient) {
        this.reactiveTcpClient = reactiveTcpClient;
    }

    @Value("${tcp.server.host}")
    private String serverHost = "localhost";

    @Value("${tcp.server.port}")
    private int serverPort = 5000;

    @Value("${tcp.client.host}")
    private String clientHost = "localhost";

    @Value("${tcp.client.port}")
    private int clientPort = 5000;

    @Bean
    public TcpServer tcpServer() {
        return TcpServer.create()
                .host(serverHost)
                .port(serverPort)
                .wiretap(true);
    }

    @Bean
    public TcpClient tcpClient() {
        return TcpClient.create()
                .host(clientHost)
                .port(clientPort)
                .wiretap(true);
    }

    @Bean
    public Mono<? extends Connection> connectionMono(TcpClient tcpClient) {
        return tcpClient.connect()
                .doOnNext(connection -> {
                    connection.inbound().receive()
                            .asString()
                            .doOnNext(response -> {
                                logger.info("Client received: {}", response);
                                reactiveTcpClient.sendMessageWithResponse(response).subscribe();
                            })
                            .subscribe();
                    logger.info("Client connected to server.");
                })
                .doOnError(error -> logger.error("Error connecting to server: {}", error.getMessage()))
                .cache();
    }
}
