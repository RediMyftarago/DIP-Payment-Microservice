package com.smartparking.payment.service;

import com.smartparking.payment.dto.PaymentResponse;
import com.smartparking.payment.dto.PaymentRequest;
import com.smartparking.payment.entity.Payment;
import com.smartparking.payment.entity.PaymentStatus;
import com.smartparking.payment.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentResponse createPayment(PaymentRequest request) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder().setAmount(request.amount()).setCurrency("eur")
                        .setDescription("Pagesa per postin " + request.parkingSpotId())
                        .putMetadata("parkingSpotId", request.parkingSpotId().toString())
                        .putMetadata("userId", request.userId().toString())
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        Payment payment = Payment.builder()
                .userId(request.userId())
                .parkingSpotId(request.parkingSpotId())
                .amount(request.amount())
                .currency("EUR")
                .stripePaymentIntentId(paymentIntent.getId())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        return new PaymentResponse(payment.getId(), paymentIntent.getClientSecret(), payment.getStatus().name());
    }

    public List<Payment> getPaymentsForUser(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    public Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Nuk u gjet pagese"
                        )
                );
    }
}
