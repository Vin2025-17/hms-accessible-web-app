package com.hmsapp.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.hmsapp.entity.Property;
import com.hmsapp.entity.PropertyImage;
import com.hmsapp.entity.User;
import com.hmsapp.payload.JWTToken;
import com.hmsapp.payload.LoginDto;
import com.hmsapp.payload.ProfileDto;
import com.hmsapp.repository.PropertyImageRepository;
import com.hmsapp.repository.PropertyRepository;
import com.hmsapp.repository.UserRepository;
import com.hmsapp.service.BucketService;
import com.hmsapp.service.OTPService;
import com.hmsapp.service.TwilioService;
import com.hmsapp.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
private UserRepository userRepository;
private UserService us;
private PropertyRepository propertyRepository;
private BucketService bks;
private PropertyImageRepository propertyImageRepository;
private OTPService otpService;
private TwilioService twilioService;


    public AuthController(UserRepository userRepository, UserService us, PropertyRepository propertyRepository, BucketService bks,
                          PropertyImageRepository propertyImageRepository, OTPService otpService, TwilioService twilioService) {
        this.userRepository = userRepository;
        this.us = us;
        this.propertyRepository = propertyRepository;
        this.bks = bks;
        this.propertyImageRepository = propertyImageRepository;
        this.otpService = otpService;
        this.twilioService = twilioService;
    }
    @PostMapping("/sign-up")
    public ResponseEntity<?> createUser(@RequestBody User user)
    {
        Optional<User> op = userRepository.findByUsername(user.getUsername());
        if(op.isPresent()){
            return new ResponseEntity<>("username already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> ops = userRepository.findByEmail(user.getEmail());
        if(ops.isPresent()){
            return new ResponseEntity<>("email already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> opss = userRepository.findByMobile(user.getMobile());
        if(opss.isPresent()){
            return new ResponseEntity<>("mobile no. already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(4)));
        user.setRole("ROLE_USER");
        User us = userRepository.save(user);
        return new ResponseEntity<>(us, HttpStatus.CREATED);
    }
    @PostMapping("/owner/sign-up")
    public ResponseEntity<?> createOwner(@RequestBody User user)
    {
        Optional<User> op = userRepository.findByUsername(user.getUsername());
        if(op.isPresent()){
            return new ResponseEntity<>("username already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> ops = userRepository.findByEmail(user.getEmail());
        if(ops.isPresent()){
            return new ResponseEntity<>("email already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> opss = userRepository.findByMobile(user.getMobile());
        if(opss.isPresent()){
            return new ResponseEntity<>("mobile no. already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(4)));
        user.setRole("ROLE_OWNER");
        User us = userRepository.save(user);
        return new ResponseEntity<>(us, HttpStatus.CREATED);
    }
    @PostMapping("/blog/sign-up")
    public ResponseEntity<?> createBlog(@RequestBody User user)
    {
        Optional<User> op = userRepository.findByUsername(user.getUsername());
        if(op.isPresent()){
            return new ResponseEntity<>("username already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> ops = userRepository.findByEmail(user.getEmail());
        if(ops.isPresent()){
            return new ResponseEntity<>("email already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Optional<User> opss = userRepository.findByMobile(user.getMobile());
        if(opss.isPresent()){
            return new ResponseEntity<>("mobile no. already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(4)));
        user.setRole("ROLE_BLOGMANAGER");
        User us = userRepository.save(user);
        return new ResponseEntity<>(us, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto logindto){
        String verify = us.verify(logindto);
        JWTToken token = new JWTToken();
        token.setToken(verify);
        token.setType("JWT");
        if(verify!=null){
            return new ResponseEntity<>(token,HttpStatus.OK);
        }
        return new ResponseEntity<>("INVALID USER/PASSWORD",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("/login/otp")
    public ResponseEntity<?> loginWithOtp(@RequestParam String mobileNumber){
        String otp = otpService.generateOtp(mobileNumber);
        if(otp!=null){
            twilioService.sendSms("+919108748796",otp+ "THIS OTP IS VALID FOR 5 MINS");
            return new ResponseEntity<>("OTP SENT TO REGISTERED NUMBER",HttpStatus.OK);
        }
        return new ResponseEntity<>("INVALID USER/PASSWORD",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("verify-otp")
    public String verifyOtp(@RequestParam String mobileNumber,@RequestParam String otp){
        boolean status = otpService.verifyOtp(mobileNumber, otp);
        if(status==true){
            return "otp verified successfully!!";
        }
        return "Invalid OTP";
    }


    @PostMapping("/add")
    public String addProperty(){
        return "added property sucessfully";
    }

    @DeleteMapping("/delete")
    public String deleteProperty(){
        return "deleted property sucessfully";
    }

    @GetMapping("/{searchParam}")
    public List<Property> searchProperty(@PathVariable String searchParam){
        return propertyRepository.getPropertyDetails(searchParam);
    }
    @GetMapping
    public ResponseEntity<ProfileDto> getProfile(@AuthenticationPrincipal User user){
      ProfileDto dto = new ProfileDto();
      dto.setName(user.getName());
      dto.setUsername(user.getUsername());
      dto.setEmail(user.getEmail());
      return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @PostMapping("/upload/file/{bucketName}/property/{propertyId}")
    public String uploadPropertyPhotos(@RequestParam MultipartFile file,
                                       @PathVariable String bucketName,
                                       @PathVariable long propertyId){
        String img = bks.uploadFile(file,bucketName);
        PropertyImage propertyImage = new PropertyImage();
        propertyImage.setUrl(img);
        Property property = propertyRepository.findById(propertyId).get();
        propertyImage.setProperty(property);
        propertyImageRepository.save(propertyImage);
        return "photo uploaded successfully";
    }
    @GetMapping("/get/property/images")
    public List<PropertyImage> getPropertyImages(@RequestParam long id){
        Property property = propertyRepository.findById(id).get();
        return propertyImageRepository.findByProperty(property);

    }
}
