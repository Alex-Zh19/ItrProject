package com.itranzition.alex.model.dto.impl;

import com.itranzition.alex.model.dto.BaseResponseDto;
import lombok.Data;

@Data
public class ResponseErrorDto implements BaseResponseDto {
    private String message;
}
