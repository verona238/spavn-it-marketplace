package com.spavnit.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelOrderRequest {

    @NotBlank(message = "Причина отмены обязательна")
    private String reason;
}