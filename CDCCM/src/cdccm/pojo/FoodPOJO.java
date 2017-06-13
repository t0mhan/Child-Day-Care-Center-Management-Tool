package cdccm.pojo;

public class FoodPOJO {
    private String day;
	private String lunch;
	private String breakfast;
	private String snack;
	
	
	
	public FoodPOJO(String lunch, String breakfast, String snack) {
		this.lunch=lunch;
		this.breakfast=breakfast;
		this.snack=snack;
	}
	public FoodPOJO(String day, String lunch, String breakfast, String snack) {
		super();
		this.day = day;
		this.lunch = lunch;
		this.breakfast = breakfast;
		this.snack = snack;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public FoodPOJO() {
		// TODO Auto-generated constructor stub
	}
	public String getLunch() {
		return lunch;
	}
	public void setLunch(String lunch) {
		this.lunch = lunch;
	}
	public String getBreakfast() {
		return breakfast;
	}
	public void setBreakfast(String breakfast) {
		this.breakfast = breakfast;
	}
	public String getSnack() {
		return snack;
	}
	public void setSnack(String snack) {
		this.snack = snack;
	}
	
}
