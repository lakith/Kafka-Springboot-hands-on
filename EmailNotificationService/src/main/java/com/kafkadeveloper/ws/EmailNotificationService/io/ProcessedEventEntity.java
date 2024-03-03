package com.kafkadeveloper.ws.EmailNotificationService.io;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name="processed-events")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProcessedEventEntity implements Serializable {
    private static final long serialVersionUID=3687553268742697084L;

    public ProcessedEventEntity (String messageId, String productId) {
        this.messageId = messageId;
        this.productId = productId;
    }

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false, unique = true)
    private String messageId;

    @Column(nullable = false)
    private String productId;

}
