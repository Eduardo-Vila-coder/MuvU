package com.example.desarrollo.service.stripe;

import lombok.extern.slf4j.Slf4j;
import com.example.desarrollo.exceptions.PaymentException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public PaymentIntent procesarCobro(Long habitacionId,Double monto, String moneda) throws StripeException {
        // Stripe requiere el monto expresado en la unidad mínima de la moneda (centavos/céntimos)
        long montoEnCentavos = Math.round(monto * 100);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(montoEnCentavos)
                .setCurrency(moneda.toLowerCase())
                .putMetadata("habitacionId", String.valueOf(habitacionId))
                .setPaymentMethod("pm_card_visa")   // tarjeta de prueba oficial de Stripe
                .setConfirm(true)                   // ejecuta el cobro inmediatamente
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                .build()
                )
                .build();

        PaymentIntent pago = PaymentIntent.create(params);
        log.info("PaymentIntent {} con estado {} para la habitación {}", pago.getId(), pago.getStatus(), habitacionId);
        if (!"succeeded".equals(pago.getStatus())) {
            throw new PaymentException("El pago no fue aprobado. Estado: " + pago.getStatus());
        }
        return pago;
    }
}
