package cdccm.pojo;

public class ChildReportPOJO {
private int childid;
private String name;
private String surname;
private String dateOfBirth;
private String ageGroup;
private String activityName;
private String sessionname;
private String feedback;
public String getFeedback() {
	return feedback;
}
public void setFeedback(String feedback) {
	this.feedback = feedback;
}
private String providername;
public String getProvidername() {
	return providername;
}
public void setProvidername(String providername) {
	this.providername = providername;
}
private int total;
private float percentage;

public ChildReportPOJO(int childid, String name, String surname, String dateOfBirth, String ageGroup,
		String activityName,String sessionname,String providername,String feedback) {
	
	this.childid = childid;
	this.name = name;
	this.surname = surname;
	this.dateOfBirth = dateOfBirth;
	this.ageGroup = ageGroup;
	this.activityName = activityName;
	this.sessionname=sessionname;
	this.providername=providername;
	this.feedback=feedback;
//	this.total = total;
//	this.percentage = percentage;
//	this.mon=mon;
//	this.tue=tue;
//	this.wen=wen;
//	this.thu=thu;
//	this.fri=fri;
}
public int getChildid() {
	return childid;
}
public void setChildid(int childid) {
	this.childid = childid;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getSurname() {
	return surname;
}
public void setSurname(String surname) {
	this.surname = surname;
}
public String getDateOfBirth() {
	return dateOfBirth;
}
public void setDateOfBirth(String dateOfBirth) {
	this.dateOfBirth = dateOfBirth;
}
public String getAgeGroup() {
	return ageGroup;
}
public void setAgeGroup(String ageGroup) {
	this.ageGroup = ageGroup;
}
public String getActivityName() {
	return activityName;
}
public void setActivityName(String activityName) {
	this.activityName = activityName;
}
public int getTotal() {
	return total;
}
public void setTotal(int total) {
	this.total = total;
}
public float getPercentage() {
	return percentage;
}
public void setPercentage(float percentage) {
	this.percentage = percentage;
}

}
