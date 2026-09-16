package com.smartparking.payment.dto;

public record PaymentResponse(

        Long paymentId,
        String clientSecret,
        String status

) {}