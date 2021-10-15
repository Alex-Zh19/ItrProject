package com.itranzition.alex.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RabbitConsumerMessageDto implements Serializable {
    private RegisteredUserDto registeredUser;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime signUpTime;
}
