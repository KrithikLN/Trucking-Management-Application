package TruckingAppServer.Hawkers.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import TruckingAppServer.Hawkers.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByDeviceId(String deviceId);
}
