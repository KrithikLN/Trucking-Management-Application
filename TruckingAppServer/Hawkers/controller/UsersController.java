package TruckingAppServer.Hawkers.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import TruckingAppServer.Hawkers.model.UsersUnverified;
import TruckingAppServer.Hawkers.service.UsersUnverifiedService;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersUnverifiedService userService;

    @PostMapping("/signup")
    public String signUp(@RequestParam("name") String name,
                         @RequestParam("phoneNumber") String phoneNumber,
                         @RequestParam("email") String email,
                         @RequestParam("file") MultipartFile file) {

        // Save text data to the database
        UsersUnverified user = new UsersUnverified();
        user.setCustomerName(name);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        userService.saveUser(user);

        // Save image file to a folder (you may want to validate and secure this process)
        try {
            String fileName = "W:/TruckingAppServer/Customer_AdhaarQr" + file.getOriginalFilename();
            file.transferTo(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return "Error uploading image";
        }

        return "User signed up successfully!";
    }
}

