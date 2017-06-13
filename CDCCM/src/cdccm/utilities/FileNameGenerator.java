package cdccm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import cdccm.helper.PropertyReader;
import cdccm.pojo.ChildReportPOJO;
import cdccm.pojo.SchedulePOJO;

public class FileNameGenerator {
	private int childid = 0;
	private Collection<ChildReportPOJO> listofscoreobject = new ArrayList<>();
	private Collection<SchedulePOJO> schedulelist = new ArrayList<>();
    private PropertyReader property=null;
	public FileNameGenerator(Collection<? extends Object> reportobject, int childid) {
		property=new PropertyReader();
		this.childid = childid;
		for (Object obj : reportobject) {
			if (obj.getClass().getName().equals("cdccm.pojo.ChildReportPOJO")) {
				System.out.println("Inside cdccm.pojo.ChildReportPOJO");
				listofscoreobject.addAll((Collection<? extends ChildReportPOJO>) reportobject);
				break;
			} else if (obj.getClass().getName().equals("cdccm.pojo.SchedulePOJO")) {

				schedulelist.addAll((Collection<? extends SchedulePOJO>) reportobject);
				break;
			}
		}
	}

	public File generateUniqueFileName(String typeofreport) {
		String filetypecategory = "";
		String directoryname = "";
		// when child id is zero means it is score object and ChildReportPOJO
		// contains the id field so we can take it from there
		if (this.childid == 0) {
			this.childid = listofscoreobject.iterator().next().getChildid();
		}
		// Check whether the reporting request is for schedule or performance
		if (typeofreport.equals("performancereport")) {
			filetypecategory = "performancereport";
			directoryname =property.getPerformanceDirectory();

		} else if (typeofreport.equals("schedulereport")) {
			filetypecategory = "schedulereport";
			directoryname =property.getScheduleDirectory();
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// format file object
		File f = new File(directoryname + this.childid + "_" + dateFormat.format(new Date()) + "_"
				+ filetypecategory + ".pdf");
		try {
			// create file object
			f.getParentFile().mkdirs();
			f.createNewFile();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return f;

	}
}
