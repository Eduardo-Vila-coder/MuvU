package com.example.desarrollo.service.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public PaymentIntent procesarCobro(Double monto, String moneda) throws StripeException {
        // Stripe requiere el monto expresado en la unidad mínima de la moneda (centavos/céntimos)
        long montoEnCentavos = Math.round(monto * 100);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(montoEnCentavos)
                .setCurrency(moneda.toLowerCase())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        return PaymentIntent.create(params);
    }

}
