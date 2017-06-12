package cdccm.pojo;

import java.util.List;

public class ParentPOJO {

	private String parent_first_name;
	private String parent_last_name;
    private List<ContactPOJO> contact;
    private List<ChildPOJO> child;
	public List<ChildPOJO> getChild() {
		return child;
	}

	public void setChild(List<ChildPOJO> child) {
		this.child = child;
	}

	public List<ContactPOJO> getContact() {
		return contact;
	}

	public void setContact(List<ContactPOJO> contact) {
		this.contact = contact;
	}

	public String getParentFirst_name() {
		return parent_first_name;
	}

	public void setParentFirst_name(String first_name) {
		this.parent_first_name = first_name;
	}

	public String getParentLast_name() {
		return parent_last_name;
	}

	public void setParentLast_name(String last_name) {
		this.parent_last_name = last_name;
	}

}
