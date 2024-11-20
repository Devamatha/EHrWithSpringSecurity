package com.techpixe.ehr.exceptionhandle;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PAYMENT_REQUIRED, reason = "Payment required to access this resource.")
public class PaymentRequiredException extends RuntimeException {

    public PaymentRequiredException(String message) {
        super(message);
    }
}