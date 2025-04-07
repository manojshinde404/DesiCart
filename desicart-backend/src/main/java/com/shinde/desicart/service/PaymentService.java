package com.shinde.desicart.service;

import com.shinde.desicart.dto.PaymentRequest;
import com.shinde.desicart.dto.PaymentResponse;
import com.shinde.desicart.exception.CustomException;
import com.razorpay.RazorpayException;

public interface PaymentService {
    PaymentResponse createPayment(PaymentRequest paymentRequest) throws RazorpayException, CustomException;
    boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) throws RazorpayException;
}
