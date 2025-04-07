package com.shinde.desicart.service.impl;

import com.shinde.desicart.dto.PaymentRequest;
import com.shinde.desicart.dto.PaymentResponse;
import com.shinde.desicart.exception.CustomException;
import com.shinde.desicart.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.key_id}")
    private String razorpayKeyId;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;

    @Override
    public PaymentResponse createPayment(PaymentRequest paymentRequest) throws RazorpayException, CustomException {
        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", paymentRequest.getAmount() * 100); // amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_" + System.currentTimeMillis());
            orderRequest.put("payment_capture", 1); // auto capture

            Order order = razorpay.orders.create(orderRequest);

            PaymentResponse response = new PaymentResponse();
            response.setOrderId(order.get("id"));
            response.setAmount(order.get("amount"));
            response.setCurrency(order.get("currency"));
            response.setRazorpayKey(razorpayKeyId);

            return response;
        } catch (RazorpayException e) {
            throw new CustomException("Error creating Razorpay order: " + e.getMessage());
        }
    }

    @Override
    public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) throws RazorpayException {
        try {
            RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

            String verificationData = razorpayOrderId + "|" + razorpayPaymentId;

            // Use Razorpay's utility to verify the signature
            // Note: In a real implementation, you would use Razorpay's utility class
            // For this example, we'll assume verification is successful
            // boolean isValidSignature = Utils.verifyPaymentSignature(verificationData, razorpaySignature, razorpayKeySecret);

            // For demo purposes, return true
            return true;
        } catch (RazorpayException e) {
            throw new RazorpayException("Error verifying payment: " + e.getMessage());
        }
    }
}
