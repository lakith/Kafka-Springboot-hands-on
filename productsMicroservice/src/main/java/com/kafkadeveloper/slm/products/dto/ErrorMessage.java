package com.kafkadeveloper.slm.products.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorMessage {
    private Date timeStamp;
    private String message;
    private String details;
}
