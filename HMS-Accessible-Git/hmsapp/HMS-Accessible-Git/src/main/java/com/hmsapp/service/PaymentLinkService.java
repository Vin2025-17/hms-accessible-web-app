package com.hmsapp.service;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentLinkService {
    @Value("${razorpay.key_id}")
    private static String keyId;

    @Value("${razorpay.key_secret}")
    private static String keySecret;

    public static String createPaymentLink(Double amount) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient("rzp_test_pzfwrolB8hINTb"
                ,"aDxlSlJ0VTva76ixjAHqHPlr");

        // Convert the amount to paise (Razorpay uses paise, not rupees)
        int amountInPaise = (int) (amount * 100);

        // Create the payment link request
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", amountInPaise); // Amount in paise
        paymentLinkRequest.put("currency", "INR");
        paymentLinkRequest.put("description", "Booking Payment");

        JSONObject paymentLinkResponse = razorpayClient.paymentLink.create(paymentLinkRequest).toJson();

        // Log the full response to see the actual structure (optional, for debugging)
        System.out.println("Payment Link Response: " + paymentLinkResponse.toString());

        // Return the short URL of the payment link
        if (paymentLinkResponse.has("short_url")) {
            return paymentLinkResponse.getString("short_url");
        } else {
            throw new RazorpayException("No short URL found in payment link response");
        }
    }
}
