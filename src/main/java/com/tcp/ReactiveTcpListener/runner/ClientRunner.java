package com.tcp.ReactiveTcpListener.runner;

import com.tcp.ReactiveTcpListener.service.client.ReactiveTcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ClientRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(ClientRunner.class);
    private final ReactiveTcpClient reactiveTcpClient;

    @Autowired
    public ClientRunner(ReactiveTcpClient reactiveTcpClient) {
        this.reactiveTcpClient = reactiveTcpClient;
    }

    @Override
    public void run(String... args) {
        reactiveTcpClient.sendMessageWithResponse("Hello from client!")
                .doOnNext(response -> {
                    logger.info("Server responded: {}", response);
                    System.out.println();
                })
                .doOnError(error -> {
                    logger.error("Error sending message: {}", error.getMessage());
                })
                .subscribe();
    }
}

