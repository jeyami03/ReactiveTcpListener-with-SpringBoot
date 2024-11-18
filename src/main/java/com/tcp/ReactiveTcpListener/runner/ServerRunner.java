package com.tcp.ReactiveTcpListener.runner;

import com.tcp.ReactiveTcpListener.service.client.ReactiveTcpClient;
import com.tcp.ReactiveTcpListener.service.server.ReactiveTcpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ServerRunner implements CommandLineRunner {
    private final ReactiveTcpServer reactiveTcpServer;
    private final ReactiveTcpClient reactiveTcpClient;

    @Autowired
    public ServerRunner(ReactiveTcpServer reactiveTcpServer, ReactiveTcpClient reactiveTcpClient) {
        this.reactiveTcpServer = reactiveTcpServer;
        this.reactiveTcpClient = reactiveTcpClient;
    }

    @Override
    public void run(String... args) {
        reactiveTcpServer.startServer().subscribe();
    }
}