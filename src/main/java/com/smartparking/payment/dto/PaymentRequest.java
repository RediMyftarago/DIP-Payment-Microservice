package com.smartparking.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(

        @NotNull
        Long userId,

        @NotNull
        Long parkingSpotId,

        @NotNull
        @Min(50)
        Long amount

) {}