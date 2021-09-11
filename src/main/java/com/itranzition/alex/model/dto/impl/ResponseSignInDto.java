package com.itranzition.alex.model.dto.impl;

import com.itranzition.alex.model.dto.BaseResponseDto;
import lombok.Data;

@Data
public class ResponseSignInDto implements BaseResponseDto {
    private String email;
    private String token;
}

