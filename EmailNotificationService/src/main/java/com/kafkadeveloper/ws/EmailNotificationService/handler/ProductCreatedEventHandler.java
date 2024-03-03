package com.kafkadeveloper.ws.EmailNotificationService.handler;

import com.kafkadeveloper.ws.EmailNotificationService.error.NotRetryableException;
import com.kafkadeveloper.ws.EmailNotificationService.error.RetryableException;
import com.kafkadeveloper.ws.EmailNotificationService.io.ProcessedEventEntity;
import com.kafkadeveloper.ws.EmailNotificationService.io.ProcessedEventRepository;
import com.kafkadeveloper.ws.core.ProductCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
@KafkaListener(topics="product-created-events-topic")
@Slf4j
public class ProductCreatedEventHandler {

    private RestTemplate restTemplate;
    private ProcessedEventRepository processedEventRepository;

    public ProductCreatedEventHandler(RestTemplate restTemplate, ProcessedEventRepository processedEventRepository) {
        this.restTemplate = restTemplate;
        this.processedEventRepository = processedEventRepository;
    }


    @KafkaHandler
    @Transactional
    public void handle(@Payload ProductCreatedEvent productCreatedEvent,
                       @Header("messageId") String messageId,
                       @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
        //debug dead-letter topic configuration.
        //if(true) throw new NotRetryableException("An Error took place, No need to consume this again");

        String requestUrl = "http://localhost:8082/response/200";
        log.info("Received a new event: " +  productCreatedEvent.getTitle());

        //check if this message is processed before
        ProcessedEventEntity existingRecord = processedEventRepository.findByMessageId(messageId);

        if(existingRecord != null) {
            log.info("Found a Duplicate Message ID: {}", existingRecord.getMessageId());
            return;
        }

        try {
            ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.GET, null, String.class);
            if(response.getStatusCode().value() == HttpStatus.OK.value()) {
                log.info("Received response from a remote service: " + response.getBody());
            }
        } catch (ResourceAccessException ex) {
            log.error(ex.getMessage());
            throw new RetryableException(ex);
        } catch (HttpServerErrorException ex) {
            log.error(ex.getMessage());
            throw new NotRetryableException(ex);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new NotRetryableException(ex);
        }

        try {
            // Save a unique message id in a database table
            processedEventRepository.save(new ProcessedEventEntity(
                    messageId,
                    productCreatedEvent.getProductId()
            ));
        } catch (DataIntegrityViolationException ex) {
            throw new NotRetryableException(ex);
        }
    }
}
