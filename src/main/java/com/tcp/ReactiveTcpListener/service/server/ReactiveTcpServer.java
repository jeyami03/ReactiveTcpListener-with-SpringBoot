package com.tcp.ReactiveTcpListener.service.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ReactiveTcpServer {
    private static final Logger logger = LoggerFactory.getLogger(ReactiveTcpServer.class);

    private final TcpServer tcpServer;

    public ReactiveTcpServer(TcpServer tcpServer) {
        this.tcpServer = tcpServer;
    }

    public void startServer() {
        DisposableServer server = tcpServer.handle((inbound, outbound) ->
                        inbound.receive()
                                .asString()
                                .doOnNext(message -> logger.info("Server received: {}", message))
                                .flatMap(message -> {
                                    String acknowledgment = "Mi Server Acknowledged: " + message;
                                    return outbound.sendString(Mono.just(acknowledgment)).then();
                                })
                )
                .bindNow();

        logger.info("TCP Server started on port: {}", server.port());
        server.onDispose().block();
    }
}