package com.hmsapp.service;

import com.razorpay.*;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service

public class PayService {
    private RazorpayClient razorpayClient;
    @Value("${razorpay.key_id}")
    private String keyId;
    @Value("${razorpay.key_secret}")
    private String keySecret;
    private RazorpayClient razorpayClients;

    public PayService() throws RazorpayException {
        this.razorpayClients =  new RazorpayClient("rzp_test_pzfwrolB8hINTb"
                , "aDxlSlJ0VTva76ixjAHqHPlr");
    }
    public JSONObject createOrder(Double amount) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient("rzp_test_pzfwrolB8hINTb"
                , "aDxlSlJ0VTva76ixjAHqHPlr");

        // Convert the amount to paise (Razorpay uses paise, not rupees)
        int amountInPaise = (int) (amount * 100);

        // Create the order request
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);  // Amount in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_12345");   // Optional, a unique transaction ID
        orderRequest.put("payment_capture", 1);     // Auto capture payment

        // Create the order on Razorpay
        Order order = razorpayClient.orders.create(orderRequest);

        // Convert the order object to a JSON response
        return order.toJson();  // Return order details as JSON
    }
    public String verifyPaymentStatus(String paymentId) {
        try {
            // Fetch payment details by payment ID
            Payment payment = razorpayClient.payments.fetch(paymentId);

            if (payment != null && "captured".equals(payment.get("status"))) {
                // Payment was successful
                return "Payment completed successfully";
            } else {
                // Payment failed or is pending
                return "Payment failed or pending";
            }
        } catch (RazorpayException e) {
            // Handle exceptions (e.g., payment not found)
            return "Error verifying payment status";
        }
    }

    }

