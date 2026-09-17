package com.example.desarrollo.service.stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.jspecify.annotations.NonNull;
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

//    public PaymentIntent procesarCobro(Double monto, String moneda) throws StripeException {
//        // Stripe requiere el monto expresado en la unidad mínima de la moneda (centavos/céntimos)
//        long montoEnCentavos = Math.round(monto * 100);
//
//        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                .setAmount(montoEnCentavos)
//                .setCurrency(moneda.toLowerCase())
//                .setAutomaticPaymentMethods(
//                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
//                                .setEnabled(true)
//                                .build()
//                )
//                .build();
//
//        return PaymentIntent.create(params);
//    }
    //esto espera que en el front end se llenen los datos..., por lo que entiendo

    public PaymentIntent procesarCobro(Double monto, String moneda) throws StripeException {
        long montoEnCentavos = Math.round(monto * 100);

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()//Se está construyendo, para el return
                .setAmount(montoEnCentavos)
                .setCurrency(moneda.toLowerCase()) //hasta acá creo que es entendible
                .setPaymentMethod("pm_card_visa") // Tarjeta de prueba oficial de Stripe
                .setConfirm(true)                 // Ejecuta el cobro inmediatamente
                .setAutomaticPaymentMethods(//las 2 líneas de arriba simulan el pago con tarjeta
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                .build()
                )
                .build();

        return PaymentIntent.create(params);//por lo que entiendo, acá se manda a la API de STRIPE para el pago
    }
}
