package com.kafkadeveloper.slm.products.service.impl;

import com.kafkadeveloper.slm.products.dto.ProductsCreateRequest;
import com.kafkadeveloper.slm.products.service.ProductService;
import com.kafkadeveloper.ws.core.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    @Override
    public String createProduct(ProductsCreateRequest productsCreateRequest) throws Exception {
        String productId = UUID.randomUUID().toString();

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(productId,
                productsCreateRequest.getTitle(),
                productsCreateRequest.getPrice(),
                productsCreateRequest.getQuantity());

//        Asyncronys Processing
//        CompletableFuture<SendResult<String, ProductCreatedEvent>> future = kafkaTemplate
//                .send("product-created-events-topic", productId, productCreatedEvent);
//
//        future.whenComplete((result, exception) -> {
//           if(exception != null) {
//               log.error("Failed To Send Message " + exception.getMessage());
//           } else {
//               log.info("Message Sent Successfully " + result.getRecordMetadata());
//           }
//        });


//        Syncronus Processing

        ProducerRecord<String, ProductCreatedEvent> producerRecord = new ProducerRecord<>(
                "product-created-events-topic",
                productId,
                productCreatedEvent
        );
        producerRecord.headers().add("messageId", UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));

        SendResult<String, ProductCreatedEvent> result = kafkaTemplate
                .send(producerRecord).get();

        log.info("partition" + result.getRecordMetadata().partition());
        log.info("topic" + result.getRecordMetadata().topic());
        log.info("Offset" + result.getRecordMetadata().offset());

        return productId;
    }
}
