package TruckingAppServer.Hawkers.model;

import jakarta.persistence.*;

@Entity
@Table(name="usersunverified")
public class UsersUnverified {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="customer_name")
    private String customerName;
    
    @Column(name="phonenumber")
    private String phoneNumber;
    
    @Column(name="email")
    private String email;

    public UsersUnverified() {
    }
    
    public UsersUnverified(String customerName, String phoneNumber, String email) {
    	this.customerName = customerName;
    	this.phoneNumber = phoneNumber;
    	this.email = email;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
    
    
}
