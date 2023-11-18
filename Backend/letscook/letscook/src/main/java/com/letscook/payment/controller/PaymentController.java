package com.letscook.payment.controller;

import com.letscook.payment.model.PaymentRequestInput;
import com.letscook.payment.service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping
    public ResponseEntity<String> completePayment(@RequestBody PaymentRequestInput paymentRequestInput) throws StripeException {
        String chargeId= paymentService.chargeCustomer(paymentRequestInput);
        return chargeId!=null? new ResponseEntity<String>(chargeId, HttpStatus.OK):
                new ResponseEntity<String>("Please check the credit card details entered",HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public String handleError(StripeException ex) {
        return ex.getMessage();
    }
}