package cdccm.pojo;

public class ParentNamePlate {
	private String emailid;
	private String name;
    private int childid;
	public int getChildid() {
		return childid;
	}

	public void setChildid(int childid) {
		this.childid = childid;
	}

	public String getEmailid() {
		return emailid;
	}

	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ParentNamePlate(String emailid, String name,int childid) {

		this.emailid = emailid;
		this.name = name;
		this.childid=childid;
	}

}
