package com.example.onlineshop.service;

import com.example.onlineshop.model.Image;
import com.example.onlineshop.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    // Save an image to the database
    public Image saveImage(MultipartFile file, String imageName, String imageType) throws IOException {
        Image image = new Image();
        image.setImageData(file.getBytes());  // Convert the file to byte array
        image.setImageName(imageName);        // Set the image name
        image.setImageType(imageType);        // Set the image type (MIME type)

        return imageRepository.save(image);
    }

    // Get an image by its ID
    public Image getImage(Long imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + imageId));
    }

    // Delete an image by its ID
    public void deleteImage(Long imageId) {
        if (!imageRepository.existsById(imageId)) {
            throw new RuntimeException("Image not found with id: " + imageId);
        }
        imageRepository.deleteById(imageId);
    }

    // Update an existing image
    public Image updateImage(Long imageId, MultipartFile newFile) throws IOException {
        Image existingImage = imageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found with id: " + imageId));

        existingImage.setImageData(newFile.getBytes());  // Update image data

        return imageRepository.save(existingImage);
    }
}
