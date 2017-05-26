package cdccm.pojo;

public class ParentPOJO {

		private String parent_first_name;
		private String parent_last_name;
		private String street;
		private String city;
		private int pincode;
		private String phone_number;
		private String email;
		
		public String getParentFirst_name() {	return parent_first_name; }

		public void setParentFirst_name(String first_name) { this.parent_first_name = first_name;}
		
		public String getParentLast_name() { return parent_last_name;	}

		public void setParentLast_name(String last_name) { this.parent_last_name = last_name; }
		
		public String getStreet() { return street; }

		public void setStreet(String dob) { this.street = dob; }
		
		public String getCity() { return city; }

		public void setCity(String dob) { this.city = dob; }
		
		public int getPincode() { return pincode; }

		public void setPincode(int pincode) { this.pincode = pincode; }
		
		public String getPhonenumber() { return phone_number; }

		public void setPhonenumber(String phone_number) { this.phone_number = phone_number; }
		
		public String getEmail() { return email; }

		public void setEmail(String email) { this.email = email; }
	}
