package com.hmsapp.controller;

import com.hmsapp.entity.Booking;
import com.hmsapp.entity.Property;
import com.hmsapp.entity.RoomAvailability;
import com.hmsapp.repository.BookingRepository;
import com.hmsapp.repository.PropertyRepository;
import com.hmsapp.repository.RoomAvailabilityRepository;
import com.hmsapp.service.*;
import com.razorpay.RazorpayException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class BookingController {
    private RoomAvailabilityRepository roomAvailabilityRepository;
    private PropertyRepository rep;
    private BookingRepository bookingRepository;
    private PDFGenerator pdfGenerator;
    private TwilioService twilioService;
    private PayService payService;
    private PaymentLinkService payLinkService;


    public BookingController(RoomAvailabilityRepository roomAvailabilityRepository, PropertyRepository rep, BookingRepository bookingRepository, PDFGenerator pdfGenerator, TwilioService twilioService, PayService payService, PaymentLinkService payLinkService) {
        this.roomAvailabilityRepository = roomAvailabilityRepository;
        this.rep = rep;
        this.bookingRepository = bookingRepository;
        this.pdfGenerator = pdfGenerator;
        this.twilioService = twilioService;
        this.payService = payService;
        this.payLinkService = payLinkService;
    }

    @GetMapping("/available")
    public ResponseEntity<?> searchRooms(@RequestParam LocalDate fromDate,
                                         @RequestParam LocalDate toDate,
                                         @RequestParam String roomType,
                                         @RequestParam Long propertyId,
                                         @RequestBody Booking booking){
        List<RoomAvailability> roomAvailability = roomAvailabilityRepository.findRoomAvailability(fromDate, toDate, roomType,propertyId);
        return new ResponseEntity<>(roomAvailability,HttpStatus.OK);
    }
    @PostMapping("/book")
    public ResponseEntity<?> bookRoom(@RequestParam LocalDate fromDate,
                                      @RequestParam LocalDate toDate,
                                      @RequestParam String roomType,
                                      @RequestParam Long propertyId,
                                      @RequestBody Booking booking){
        Optional<Property> propertyOptional = rep.findById(propertyId);
        // Check if the property exists
        if (!propertyOptional.isPresent()) {
            return new ResponseEntity<>("Property not found", HttpStatus.NOT_FOUND);
        }
        Property property = propertyOptional.get();
        List<RoomAvailability> roomAvailability = roomAvailabilityRepository.findRoomAvailability(fromDate, toDate, roomType, propertyId);
        if (roomAvailability.isEmpty()) {
            return new ResponseEntity<>("No availability for the selected dates for this property", HttpStatus.NOT_FOUND);
        }
        double totalAmount = 0;
        boolean roomsAvailable = false;
        for (RoomAvailability room : roomAvailability) {
            if (room.getTotal_rooms() > 0) {
                // If at least one room is available, we can proceed
                roomsAvailable = true;
                // Calculate the total amount based on the number of nights
                int numberOfNights = (int) java.time.temporal.ChronoUnit.DAYS.between(fromDate, toDate);
                totalAmount += 100 * numberOfNights;
                room.setTotal_rooms(room.getTotal_rooms() - 1);
            }
        }
        if (!roomsAvailable) {
            return new ResponseEntity<>("No available rooms found or Not Available in the given dates", HttpStatus.NOT_FOUND);
        }
        booking.setProperty(property);
        Booking save = bookingRepository.save(booking);
        try {
            // Create the payment link via Razorpay service
            String paymentLink = PaymentLinkService.createPaymentLink(totalAmount);
            pdfGenerator.generatePdf("A:\\bookig_docs\\bookings"+save.getId()+".pdf",save,property);
            twilioService.sendSms("+919108748796","BOOKING SUCCESSFULL, Please complete payment at ->\t" + paymentLink);
            return new ResponseEntity<>("BOOKING SUCCESSFULL, Please complete payment at -> \t" + paymentLink,HttpStatus.OK);
            // Return the payment link to the client so they can make the payment
        } catch (RazorpayException e) {
            return new ResponseEntity<>("Error Or No Rooms Found!!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/verifyPayment")
    public ResponseEntity<String> verifyPayment(@RequestParam String paymentId) {
        String status = payService.verifyPaymentStatus(paymentId);
        if ("Payment completed successfully".equals(status)) {
            // Update the booking status in the database
            // bookingRepository.updateBookingStatus(bookingId, "confirmed");
            return new ResponseEntity<>(status, HttpStatus.OK);
        }
        return new ResponseEntity<>("Payment Failed Booking is Revoked!!", HttpStatus.BAD_REQUEST);
    }
}


