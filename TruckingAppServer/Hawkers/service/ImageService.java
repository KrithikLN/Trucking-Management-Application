package TruckingAppServer.Hawkers.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ImageService {

    public void saveImage(MultipartFile file) throws IOException {
        // Process the image or save it to a location on your server
        // For example, save it to a folder
        String uploadDir = "W:/TruckingAppServer/receivedimages/";
        String fileName = file.getOriginalFilename();
        String filePath = uploadDir + File.separator + fileName;
        file.transferTo(new File(filePath));
    }
}
