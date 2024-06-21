package com.roboteon.warehouse.Service;

import com.roboteon.warehouse.Domain.AisleDomain;
import com.roboteon.warehouse.Domain.RackDomain;
import com.roboteon.warehouse.Entity.Aisle;
import com.roboteon.warehouse.Entity.Image;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WarehouseService {
    @Autowired
    private ImageStorageService storageService;

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public JSONObject processImageAndDetectRacks(MultipartFile file) throws IOException {
        Mat hierarchy = new Mat();

        List<MatOfPoint> contours = new ArrayList<>();

        Mat image;
        try {
            byte[] imageBytes = file.getBytes();
            image = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_UNCHANGED);
            if (image.empty()) {
                throw new IOException("Failed to read the image. Please ensure the image is not corrupted.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid image format. Please upload a valid JPG or PNG image.", e);
        }

        Image imageEntity = new Image();

        //Saving Image in the Database
        try {
            imageEntity = storageService.saveImage(file);
        } catch (Exception e) {
            System.err.println("Unexpected error while saving image: " + e.getMessage());
            throw new IOException("An unexpected error occurred while saving image. Please try again.", e);
        }

        // Preprocess the image
        List<RackDomain> racks = preprocessImage(image, contours, hierarchy);

        // Call detectAisles method with the list of Rack objects
        JSONArray aislesJsonArray = detectAisles(imageEntity, racks);
        // Create the entire_room object
        JSONObject entireRoomJson = new JSONObject();
        entireRoomJson.put("width", image.width());
        entireRoomJson.put("height", image.height());
        JSONObject finalJson = new JSONObject();
        finalJson.put("entireroom", entireRoomJson);
        finalJson.put("aisles", aislesJsonArray);
        System.out.println(finalJson);

        return finalJson;
    }

    private static List<RackDomain> preprocessImage(Mat image, List<MatOfPoint> contours, Mat hierarchy) {
        // Convert to grayscale
        Mat grayImage = new Mat();
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);

        // Gaussian blur for noise reduction
        Imgproc.GaussianBlur(grayImage, grayImage, new Size(5, 5), 0);
        // Gaussian blur for noise reduction
        Imgproc.GaussianBlur(grayImage, grayImage, new Size(3, 3), 0);

        // Canny edge detection
        Mat edges = new Mat();
        Imgproc.Canny(grayImage, edges, 125, 275);

        // Morphological closing to connect edges
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.morphologyEx(edges, edges, Imgproc.MORPH_CLOSE, kernel);

        // Find contours and draw them on a clone of the original image
        Imgproc.findContours(edges.clone(), contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        Mat contoursImage = image.clone();
        for (int i = 0; i < contours.size(); i++) {
            Rect rect = Imgproc.boundingRect(contours.get(i));
            Imgproc.rectangle(contoursImage, rect.tl(), rect.br(), new Scalar(0, 255, 0), 1);
        }

        Mat rectanglesImage = Mat.zeros(image.size(), CvType.CV_8UC3);
        Mat lineImage = Mat.zeros(image.size(), CvType.CV_8UC3);

        // Attempt to identify the shape of each contour
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint contour = contours.get(i);
            Rect boundingBox = Imgproc.boundingRect(contour);
            double aspectRatio = (double) boundingBox.width / boundingBox.height;
            final double LINE_ASPECT_RATIO_THRESHOLD = Double.POSITIVE_INFINITY;

            // Simple shape identification based on aspect ratio
            if (aspectRatio == LINE_ASPECT_RATIO_THRESHOLD) {
                System.out.println("Contour " + (i + 1) + " is likely a line.");
                Imgproc.drawContours(lineImage, contours, i, new Scalar(0, 255, 0), 1);
            } else if (aspectRatio > 0.5 && aspectRatio < 3) {
                System.out.println("Contour " + (i + 1) + " is likely a square or rectangle.");
                Imgproc.drawContours(rectanglesImage, contours, i, new Scalar(0, 255, 0), 1);
            }
        }

        List<Rect> filteredRectangles = findAndFilterRectanglesByModeArea(rectanglesImage);
//        saveFilteredRectangles(filteredRectangles);
        List<RackDomain> racks = filteredRectangles.stream()
                .map(rect -> new RackDomain(rect.x, rect.y, rect.width, rect.height))
                .collect(Collectors.toList());

        return racks;
    }

    private static List<Rect> findAndFilterRectanglesByModeArea(Mat image) {
        Mat grayImage = new Mat();
        if (image.channels() > 1) {
            Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_BGR2GRAY);
        } else {
            grayImage = image.clone();
        }
        // Apply thresholding and contour detection on the grayscale image
        Imgproc.threshold(grayImage, grayImage, 128, 255, Imgproc.THRESH_BINARY);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(grayImage, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);
        // Image area for comparison
        double imageArea = image.size().area();
        double outlierThreshold = imageArea * 0.95;  // Set threshold as 95% of the image area
        // Filter rectangles by area size (excluding near-image-size contours)
        List<Rect> filteredRectangles = new ArrayList<>();
        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area < outlierThreshold) {  // Check if the area is less than the threshold
                Rect rect = Imgproc.boundingRect(contour);
                boolean isChildContour = false;
                for (int j = 0; j < contours.size(); j++) {
                    if (hierarchy.get(0, j)[2] == contours.indexOf(contour)) {
                        isChildContour = true;
                        break;
                    }
                }
                if (!isChildContour) {
                    filteredRectangles.add(rect);
                }
            }
        }
        return filteredRectangles;
    }

    private JSONArray detectAisles(Image imageEntity, List<RackDomain> racks) {
        Collections.sort(racks, Comparator.comparingInt(RackDomain::getX).thenComparingInt(RackDomain::getY));
        List<AisleDomain> aisles = new ArrayList<>();

        final int proximityThreshold = 20;

        for (RackDomain current : racks) {
            boolean foundAisle = false;
            for (AisleDomain aisle : aisles) {
                RackDomain lastInAisle = aisle.getRacks().get(aisle.getRacks().size() -1 );
                if (isAdjacent(current, lastInAisle, proximityThreshold)) {
                    aisle.add(current);
                    foundAisle = true;
                    break;
                }
            }
            if (!foundAisle) {
                aisles.add(new AisleDomain(current));
            }
        }
        JSONArray aislesJsonArray = new JSONArray();
        for (AisleDomain aisle : aisles) {
            // Calculate aisle dimensions
            int minX = Integer.MAX_VALUE;
            int minY = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            for (RackDomain rack : aisle.getRacks()) {
                minX = Math.min(minX, rack.getX());
                minY = Math.min(minY, rack.getY());
                maxX = Math.max(maxX, rack.getX() + rack.getWidth());
                maxY = Math.max(maxY, rack.getY() + rack.getHeight());
            }
            int aisleWidth = maxX - minX;
            int aisleHeight = maxY - minY;
            JSONObject aisleJson = new JSONObject();
            aisleJson.put("Aisle_id", "A" + (aisles.indexOf(aisle) + 1));
            aisleJson.put("rack_count", aisle.getRacks().size());
            aisleJson.put("x", minX);
            aisleJson.put("width", aisleWidth);
            aisleJson.put("y", minY);
            aisleJson.put("height", aisleHeight);
            aislesJsonArray.put(aisleJson);

            String aisleId = String.format("%02d", aisles.indexOf(aisle) + 1);
            Aisle aisleEntity = new Aisle();
            aisleEntity.setAisleId("A" + aisleId);
            aisleEntity.setX(minX);
            aisleEntity.setY(minY);
            aisleEntity.setWidth(aisleWidth);
            aisleEntity.setHeight(aisleHeight);
            aisleEntity.setRackCount(aisle.getRacks().size());
            aisleEntity.setImage(imageEntity);
            storageService.saveAisle(aisleEntity);
        }
        return aislesJsonArray;
    }

    private static boolean isAdjacent(RackDomain current, RackDomain last, int threshold) {
        try {
            // Check if racks are vertically aligned (for horizontal aisles)
            boolean isAlignedVertically = Math.abs(current.getY() - last.getY()) < (current.getHeight() / 2);
            boolean isCloseHorizontally = Math.abs((last.getX() + last.getWidth()) - current.getX()) <= threshold;
            // Check if racks are horizontally aligned (for vertical aisles)
            boolean isAlignedHorizontally = Math.abs(current.getX() - last.getX()) < (current.getWidth() / 2);
            boolean isCloseVertically = Math.abs((last.getY() + last.getHeight()) - current.getY()) <= threshold;
            return (isAlignedVertically && isCloseHorizontally) || (isAlignedHorizontally && isCloseVertically);
        } catch (ArithmeticException e) {
            // Handle division by zero error
            System.err.println("Error: Division by zero occurred.");
            return false;
        } catch (Exception e) {
            // Handle other exceptions
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }
}
