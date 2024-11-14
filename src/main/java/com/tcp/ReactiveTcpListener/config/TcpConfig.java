package com.tcp.ReactiveTcpListener.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.netty.tcp.TcpClient;
import reactor.netty.tcp.TcpServer;

@Configuration
public class TcpConfig {

    @Value("${tcp.server.host}")
    private String serverHost;

    @Value("${tcp.server.port}")
    private int serverPort;

    @Value("${tcp.client.host}")
    private String clientHost;

    @Value("${tcp.client.port}")
    private int clientPort;

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
}
