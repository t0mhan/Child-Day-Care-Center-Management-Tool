package cdccm.pojo;

public class SchedulePOJO {
	
    private String plantime;
	private String plan;

	public SchedulePOJO(String plantime, String plan) {

		this.plantime = plantime;
		this.plan = plan;
		
	}

	public String getPlantime() {
		return plantime;
	}

	public void setPlantime(String plantime) {
		this.plantime = plantime;
	}

	public String getPlan() {
		return plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	

}
