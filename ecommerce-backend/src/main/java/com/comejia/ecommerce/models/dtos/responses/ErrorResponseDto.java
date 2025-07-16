package com.comejia.ecommerce.models.dtos.responses;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message
) { }

