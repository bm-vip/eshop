package com.eshop.exception.common;


import com.eshop.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class PaymentRequiredException extends BaseException {
    private static final Logger log = LoggerFactory.getLogger(PaymentRequiredException.class);
    public PaymentRequiredException() {
        super("insufficient balance!", HttpStatus.PAYMENT_REQUIRED);
        log.error(
                String.valueOf(this)
        );
    }
    public PaymentRequiredException(String msg) {
        super(String.format("%s",msg), HttpStatus.PAYMENT_REQUIRED);
        log.error(
                String.valueOf(this)
        );
    }
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
