package cdccm.pojo;

public class ContactPOJO {
	private String street;
	private String city;
	private int pincode;
	private String phoneNumber;
	private String email;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public ContactPOJO() {

	}

	public ContactPOJO(String street, String city, int pincode, String phoneNumber, String email) {
		this.street = street;
		this.city = city;
		this.pincode = pincode;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPincode() {
		return pincode;
	}

	public void setPincode(int pincode) {
		this.pincode = pincode;
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
