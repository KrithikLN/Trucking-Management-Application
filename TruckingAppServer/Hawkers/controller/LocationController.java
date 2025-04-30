package TruckingAppServer.Hawkers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import TruckingAppServer.Hawkers.model.Location;
import TruckingAppServer.Hawkers.repository.LocationRepository;
import TruckingAppServer.Hawkers.service.LocationService;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    private final LocationRepository locationRepository;
    private final LocationService locationService;

    @Autowired
    public LocationController(LocationRepository locationRepository, LocationService locationService) {
        this.locationRepository = locationRepository;
        this.locationService = locationService;
    }
    
    @GetMapping("/getAll")
    public ResponseEntity<List<Location>> getAllLocations() {
        return locationService.getAllLocations();
    }

    @PostMapping("/uploads")
    public ResponseEntity<String> saveLocation(@RequestBody Location location) {
        // Check if location data already exists for the device ID
        Location existingLocation = locationRepository.findByDeviceId(location.getDeviceId()).orElse(null);

        if (existingLocation != null) {
            // Update existing location data
            existingLocation.setLatitude(location.getLatitude());
            existingLocation.setLongitude(location.getLongitude());
            locationRepository.save(existingLocation);
            return new ResponseEntity<>("Location updated successfully!", HttpStatus.OK);
        } else {
            // Save new location data
            locationRepository.save(location);
            return new ResponseEntity<>("Location saved successfully!", HttpStatus.CREATED);
        }
    }
}
