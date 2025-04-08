package com.hmsapp.controller;

import com.hmsapp.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/img")
public class ImageController {
    @Autowired
    private BucketService bks;
    @PostMapping(path="/upload/file/{bucketName}/property/{propertyId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file,
                                         @PathVariable String bucketName,
                                         @PathVariable long propertyId){
        String img = bks.uploadFile(file,bucketName);
        return new ResponseEntity<>(img, HttpStatus.OK);
    }
}
