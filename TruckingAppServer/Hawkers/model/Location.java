package TruckingAppServer.Hawkers.model;

import jakarta.persistence.*;

@Entity
@Table(name="location_entity")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    
    @Column(name="device_id")
    private String deviceId;
    
    @Column(name="latitude")
    private double latitude;
    
    @Column(name="longitude")
    private double longitude;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
    
	public Location() {
	}
	
	public Location(String deviceId, double latitude, double longitude) {
		this.deviceId = deviceId;
		this.latitude = latitude;
		this.longitude = longitude;
	}
    
    
}
