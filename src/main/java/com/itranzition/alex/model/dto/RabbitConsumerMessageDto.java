package com.itranzition.alex.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RabbitConsumerMessageDto implements Serializable {
    private String message;
}
