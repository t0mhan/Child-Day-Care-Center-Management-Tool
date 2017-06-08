package cdccm.pojo;

public class AssignActivityPOJO {

	private int childID;
	private int ageGroup;
	private int activityID;
	private int careProviderID;
	private int session;
	private String feedback;

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public int getChildID() {
		return childID;
	}

	public void setChildID(int childID) {
		this.childID = childID;
	}

	public int getAgeGroup() {
		return ageGroup;
	}

	public void setAgeGroup(int ageGroup) {
		this.ageGroup = ageGroup;
	}

	public int getActivityID() {
		return activityID;
	}

	public void setActivityID(int activityID) {
		this.activityID = activityID;
	}

	public int getCareProviderID() {
		return careProviderID;
	}

	public void setCareProviderID(int careProviderID) {
		this.careProviderID = careProviderID;
	}

	public int getSession() {
		return session;
	}

	public void setSession(int session) {
		this.session = session;
	}

}
