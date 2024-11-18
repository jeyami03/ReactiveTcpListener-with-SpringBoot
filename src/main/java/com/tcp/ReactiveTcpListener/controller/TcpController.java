package com.tcp.ReactiveTcpListener.controller;

import com.tcp.ReactiveTcpListener.service.client.ReactiveTcpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tcp")
public class TcpController {
    @Autowired
    private ReactiveTcpClient tcpClient;

    @GetMapping("/sendToServer")
    public Mono<String> sendMessageToServer(@RequestParam String message) {
        return tcpClient.sendMessageWithResponse(message)
                .map(response -> "Server response: " + response);
    }
}
