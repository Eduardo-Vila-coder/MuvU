package com.example.desarrollo.service.stripe;

import com.example.desarrollo.exceptions.PaymentException;
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

    public PaymentIntent procesarCobro(Long habitacionId,Double monto, String moneda) throws StripeException {
        // Stripe requiere el monto expresado en la unidad mínima de la moneda (centavos/céntimos)
        long montoEnCentavos = Math.round(monto * 100);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()//construcción para el return
                .setAmount(montoEnCentavos)
                .setCurrency(moneda.toLowerCase())//hasta acá creo que es entendible
                // Guardamos el ID de la habitación en la metadata del cobro

                //---------------------------------------------------------------
                //Si quieren que se guarde en BD, uncomment las 2 líneas de abajo.
                //---------------------------------------------------------------

                //.setPaymentMethod("pm_card_visa") // Tarjeta de prueba oficial de Stripe
                //.setConfirm(true)                 // Ejecuta el cobro inmediatamente
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
        if (!"succeeded".equals(pago.getStatus())) {
            throw new PaymentException("El pago no fue aprobado. Estado: " + pago.getStatus());
        }
        return pago;
    }
    //esto espera que en el front-end se llenen los datos bancarios

// esto era para simular el pago con la parte de front-end ya implementada.
//    public PaymentIntent procesarCobro(Double monto, String moneda) throws StripeException {
//        long montoEnCentavos = Math.round(monto * 100);
//
//        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()//Se está construyendo, para el return
//                .setAmount(montoEnCentavos)
//                .setCurrency(moneda.toLowerCase())
//                .setPaymentMethod("pm_card_visa") // Tarjeta de prueba oficial de Stripe
//                .setConfirm(true)                 // Ejecuta el cobro inmediatamente
//                .setAutomaticPaymentMethods(//las 2 líneas de arriba simulan el pago con tarjeta
//                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
//                                .setEnabled(true)
//                                .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
//                                .build()
//                )
//                .build();
//
//        return PaymentIntent.create(params);//por lo que entiendo, acá se manda a la API de STRIPE para el pago
//    }
}
