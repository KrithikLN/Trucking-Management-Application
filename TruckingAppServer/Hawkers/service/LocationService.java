package TruckingAppServer.Hawkers.service;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import TruckingAppServer.Hawkers.repository.LocationRepository;
import TruckingAppServer.Hawkers.model.Location;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        if (!locations.isEmpty()) {
            return new ResponseEntity<>(locations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public Location saveOrUpdateLocation(Location location) {
        // Check if location data already exists for the device ID
        Optional<Location> existingLocation = locationRepository.findByDeviceId(location.getDeviceId());

        if (existingLocation.isPresent()) {
            // Update existing location data
            Location updatedLocation = existingLocation.get();
            updatedLocation.setLatitude(location.getLatitude());
            updatedLocation.setLongitude(location.getLongitude());
            return locationRepository.save(updatedLocation);
        } else {
            // Save new location data
            return locationRepository.save(location);
        }
    }
}
