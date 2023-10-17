package com.movielibrary.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.movielibrary.dto.UserResponseDTO;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UserRPC {
    private final String consumerQueueName = "username_consumer";
    private final String publisherQueueName = "username_consumer_response";
    private final Channel channel;

    public UserRPC() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        Connection connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public UserResponseDTO getUserByToken(String token) throws IOException, InterruptedException, ExecutionException, JsonProcessingException {
        final String corrId = UUID.randomUUID().toString();

        String replyQueueName = channel.queueDeclare().getQueue();
        ObjectMapper objectMapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        String tokenJson = objectMapper.writeValueAsString(token);
        channel.basicPublish("", consumerQueueName, props, tokenJson.getBytes(StandardCharsets.UTF_8));

        final CompletableFuture<UserResponseDTO> response = new CompletableFuture<>();

        String channelResponse = channel.basicConsume(publisherQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                UserResponseDTO user = objectMapper.readValue(delivery.getBody(), UserResponseDTO.class);
                response.complete(user);
            }
        }, consumerTag -> {});

        UserResponseDTO result = response.get();
        channel.basicCancel(channelResponse);
        return result;
    }
}
