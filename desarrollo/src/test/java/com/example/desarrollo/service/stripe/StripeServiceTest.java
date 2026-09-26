package com.example.desarrollo.service.stripe;

import com.example.desarrollo.exceptions.PaymentException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StripeServiceTest {

    private final StripeService stripeService = new StripeService();

    @Test
    void cobroAprobado_enviaElMontoEnCentavos() throws Exception {
        PaymentIntent intent = mock(PaymentIntent.class);
        when(intent.getStatus()).thenReturn("succeeded");

        try (MockedStatic<PaymentIntent> api = mockStatic(PaymentIntent.class)) {
            api.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class))).thenReturn(intent);

            assertSame(intent, stripeService.procesarCobro(1L, 30.0, "USD"));

            ArgumentCaptor<PaymentIntentCreateParams> captor = ArgumentCaptor.forClass(PaymentIntentCreateParams.class);
            api.verify(() -> PaymentIntent.create(captor.capture()));
            assertEquals(3000L, captor.getValue().getAmount());
            assertEquals("usd", captor.getValue().getCurrency());
        }
    }

    @Test
    void cobroNoAprobado_lanzaPaymentException() {
        PaymentIntent intent = mock(PaymentIntent.class);
        when(intent.getStatus()).thenReturn("requires_action");

        try (MockedStatic<PaymentIntent> api = mockStatic(PaymentIntent.class)) {
            api.when(() -> PaymentIntent.create(any(PaymentIntentCreateParams.class))).thenReturn(intent);

            assertThrows(PaymentException.class, () -> stripeService.procesarCobro(1L, 30.0, "usd"));
        }
    }
}
