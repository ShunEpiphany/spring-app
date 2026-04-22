package com.forwardcompute.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbit")
public class RabbitController {

    private static final Logger log = LoggerFactory.getLogger(RabbitController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;


    // type: Exchange type
    // - direct: exact routing key match
    // - fanout: broadcast to all queues, ignore routing key
    // - topic: wildcard * and # matching
    // - headers: header-based routing

    @PostMapping("/")
    public void send(@RequestParam String routingKey, @RequestParam String message) {
        if (routingKey == null || routingKey.isEmpty()) {
            throw new IllegalArgumentException("routingKey cannot be null or empty");
        }
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("message cannot be null or empty");
        }
        rabbitTemplate.convertAndSend(routingKey, message);
    }

    @PostMapping("/work")
    public void sendWork(@RequestParam String routingKey, @RequestParam String message) {
        if (routingKey == null || routingKey.isEmpty()) {
            throw new IllegalArgumentException("routingKey cannot be null or empty");
        }
        if (message == null || message.isEmpty()) {
            throw new IllegalArgumentException("message cannot be null or empty");
        }
        for(int i = 1; i <= 10; i++) {
            rabbitTemplate.convertAndSend(routingKey, "message number "+ i + ": " + message);
        }
    }

    @PostMapping("/fanout")
    public void sendFanout(@RequestParam String exchange, @RequestParam String message) {
        rabbitTemplate.convertAndSend(exchange, "", message);
    }

    @PostMapping("/direct")
    public void sendDirect(@RequestParam String exchange, @RequestParam String routingKey, @RequestParam String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

}
