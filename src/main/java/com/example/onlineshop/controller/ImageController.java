package com.example.onlineshop.controller;

import com.example.onlineshop.model.Image;
import com.example.onlineshop.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    // Endpoint to upload an image
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("imageName") String imageName,
                                              @RequestParam("imageType") String imageType) throws IOException {

        Image image = imageService.saveImage(file, imageName, imageType);
        return ResponseEntity.ok("Image uploaded successfully with ID: " + image.getImageId());
    }

    // Endpoint to get an image by ID
    @GetMapping("/{imageId}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable Long imageId) {
        Image image = imageService.getImage(imageId);

        // Set up the response with the image data
        ByteArrayResource resource = new ByteArrayResource(image.getImageData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + image.getImageName())
                .body(resource);
    }

    // Endpoint to delete an image by ID
    @DeleteMapping("/{imageId}")
    public ResponseEntity<String> deleteImage(@PathVariable Long imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.ok("Image deleted successfully");
    }
}
