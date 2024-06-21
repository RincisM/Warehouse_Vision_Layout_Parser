package com.roboteon.warehouse.Service;

import com.roboteon.warehouse.Entity.Aisle;
import com.roboteon.warehouse.Entity.Image;
import com.roboteon.warehouse.Repository.AisleRepository;
import com.roboteon.warehouse.Repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImageStorageService {

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AisleRepository aisleRepository;

    public Image saveImage(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("File is empty and cannot be processed.");
            }

            byte[] data = file.getBytes();
            Image imageEntity = new Image(data);
            return imageRepository.save(imageEntity);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    public void saveAisle(Aisle aisle) {
        aisleRepository.save(aisle);
    }

    public ByteArrayResource getImage(Long id) {
        Image imageEntity = imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image not Found"));
        return new ByteArrayResource(imageEntity.getData());
    }
}
