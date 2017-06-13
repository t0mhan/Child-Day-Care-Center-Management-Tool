package cdccm.pojo;

public class ChildNamePlate {
	private String child_first_name;
	private String child_last_name;
	private String date_of_birth;
	private String age_group;
	public ChildNamePlate(String child_first_name, String child_last_name, String date_of_birth, String age_group) {
	
		this.child_first_name = child_first_name;
		this.child_last_name = child_last_name;
		this.date_of_birth = date_of_birth;
		this.age_group = age_group;
	}
	public String getChild_first_name() {
		return child_first_name;
	}
	public void setChild_first_name(String child_first_name) {
		this.child_first_name = child_first_name;
	}
	public String getChild_last_name() {
		return child_last_name;
	}
	public void setChild_last_name(String child_last_name) {
		this.child_last_name = child_last_name;
	}
	public String getDate_of_birth() {
		return date_of_birth;
	}
	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
	}
	public String getAge_group() {
		return age_group;
	}
	public void setAge_group(String age_group) {
		this.age_group = age_group;
	}
}
