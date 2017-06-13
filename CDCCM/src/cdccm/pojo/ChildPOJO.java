package cdccm.pojo;

public class ChildPOJO {
   private int childId;
	public int getChildId() {
	return childId;
}

public void setChildId(int childId) {
	this.childId = childId;
}

	private String child_first_name;
	private String child_last_name;
	private String date_of_birth;
	public ChildPOJO()
	{
		
	}
	public ChildPOJO(int childId, String child_first_name, String child_last_name, String date_of_birth, int age) {
	
		this.childId = childId;
		this.child_first_name = child_first_name;
		this.child_last_name = child_last_name;
		this.date_of_birth = date_of_birth;
		this.age = age;
	}

	private int age;
	
	public String getFirst_name() {	return child_first_name; }

	public void setFirst_name(String first_name) { this.child_first_name = first_name;}
	
	public String getLast_name() { return child_last_name;	}

	public void setLast_name(String last_name) { this.child_last_name = last_name; }

	public String getDob() { return date_of_birth; }

	public void setDob(String dob) { this.date_of_birth = dob; }
	
	public int getAge() { return age; }

	public void setAge(int age) { this.age = age; }
	
}

