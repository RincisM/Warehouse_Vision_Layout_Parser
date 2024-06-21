package com.roboteon.warehouse.Controller;

import com.roboteon.warehouse.Service.WarehouseService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("*")
@RequestMapping("/collegeproject")
public class WarehouseVisionController {
    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/welcome")
    public String Welcome() {
        return "Welcome to Warehouse Vision Layout Parser";
    }

    @PostMapping("/process")
    public ResponseEntity<?> processImage(@RequestParam("file")MultipartFile file) {
        try {
            System.out.println("Started Rack Service...");
            JSONObject detectedAisles = warehouseService.processImageAndDetectRacks(file);
            return new ResponseEntity<>(detectedAisles.toString(), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            // Handle specific exceptions
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
