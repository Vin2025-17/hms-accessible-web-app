package com.hmsapp.service;
import com.hmsapp.payload.OTPDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OTPService {

    private final HashMap<String, OTPDetails> otpStore = new HashMap<>();
    private static final int OTP_EXPIRATION_TIME = 5000; // OTP expires in 5 minutes (in milliseconds)
    private static final Random random = new Random();

    // Generate a 6-digit OTP
    public String generateOtp(String mobileNumber) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        OTPDetails otpDetails = new OTPDetails(otp, System.currentTimeMillis());// 6-digit OTP
        otpStore.put(mobileNumber, otpDetails);
        return otp;
    }

    public boolean verifyOtp(String mobileNumber, String otp) {
        // Get the OTP details for the given mobile number
        OTPDetails otpInfo = otpStore.get(mobileNumber);

        // Check if the OTP details are null (no OTP exists for the mobile number)
        if (otpInfo == null) {
            return false;  // No OTP generated for the mobile number
        }

        // Check if the OTP has expired
        long currentTime = System.currentTimeMillis();
        long otpTime = otpInfo.getExpirationTime();
        // Calculate the difference between the current time and OTP expiration time in minutes
        long timeDifference = TimeUnit.MILLISECONDS.toMinutes(otpTime - currentTime);
        if (timeDifference > OTP_EXPIRATION_TIME) {
            otpStore.remove(mobileNumber);
            return false;
        }

        // If OTP is valid, compare the OTP values and return the result
        return otpInfo.getOtp().equals(otp);
    }
}