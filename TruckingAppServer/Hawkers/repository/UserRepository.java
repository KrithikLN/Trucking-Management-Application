package TruckingAppServer.Hawkers.repository;

import TruckingAppServer.Hawkers.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
