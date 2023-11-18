package com.letscook.payment.service;

import com.letscook.order.service.OrderService;
import com.letscook.payment.model.PaymentRequestInput;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = this.stripeApiKey;
    }
    public String chargeCustomer(PaymentRequestInput paymentRequestInput) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", paymentRequestInput.getAmount());
        chargeParams.put("currency", "CAD");
        chargeParams.put("source", paymentRequestInput.getToken());

        Charge charge = Charge.create(chargeParams);
        return charge.getId();
    }
}
