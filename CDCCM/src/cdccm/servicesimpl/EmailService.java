package cdccm.servicesimpl;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import cdccm.dbServices.MySQLDBConnector;
import cdccm.helper.PropertyReader;
import cdccm.pojo.ParentNamePlate;

public class EmailService {
	private MySQLDBConnector dbConnector = null;
	private String date;
	private String messageHeadading;
	private String messageBody;
	private String directorylocation;
	PropertyReader directory=null;
	public EmailService(String date, String messageHeadading, String messageBody) {
		dbConnector = MySQLDBConnector.getInstance();
		this.directory=new PropertyReader();
		this.date = date;
		this.messageHeadading = messageHeadading;
		this.messageBody = messageBody;
	
	}

	public EmailService() {
		}

	public void send(String whichmail) {
        
		if (whichmail.equals("performance")) {
			this.directorylocation =this.directory.getPerformanceDirectory();
		} else if (whichmail.equals("schedule")) {
			this.directorylocation =this.directory.getScheduleDirectory();
			//make description and food on the fly
		} else if (whichmail.equals("news")) {

			this.directorylocation = null;
		}
		constructMail();
	}
    public void sendSpecificMail(String emailid,String heading,String body)
    {
    	MailSender mailsender = new MailSender(heading, body, emailid, null);
		mailsender.sendMail();
    }
	private void constructMail() {
		ParentNamePlate parentnameplate = null;
		ArrayList<ParentNamePlate> listofparentnameplate = new ArrayList<ParentNamePlate>();

		String fileNames[] = null;
		String emailids[] = null;

		listofparentnameplate = getAllParentsNameplate();
		if (this.directorylocation == null && this.date == null) {
	
			SendEventInfo(listofparentnameplate);
		} else {
			File mainFolder = new File(this.directorylocation);
			// get all files in direcory
			fileNames = getFiles(mainFolder);
			// get all parent nameplates
			matchFileAndSend(listofparentnameplate, fileNames);
		}
	}

	/*
	 * this method executes when there is no attachment but just information
	 * about news and events
	 */
	private void SendEventInfo(ArrayList<ParentNamePlate> listOfParentNamePlates) {
		Iterator<ParentNamePlate> it = listOfParentNamePlates.iterator();
		while (it.hasNext()) {
			ParentNamePlate parentnameplate = it.next();
			String emailid = parentnameplate.getEmailid();
			String messagebody = "Dear " + parentnameplate.getName() + " " + this.messageBody;
			System.out.println(emailid + " :is queued ");
			MailSender mailsender = new MailSender(this.messageHeadading, messagebody, emailid, null);
			mailsender.sendMail();
		}
	}

	/*
	 * this method iteratos over the list of parents and available array of
	 * files to search the appropriate file for that parent
	 */
	private void matchFileAndSend(ArrayList<ParentNamePlate> listOfParentNamePlates, String[] fileNames)  {
		Iterator<ParentNamePlate> it = listOfParentNamePlates.iterator();

		while (it.hasNext()) {
			ParentNamePlate parentnameplate = it.next();
			int childid = parentnameplate.getChildid();//
		
			System.out.println("child id: " + childid + " is selected for sending");
			for (int i = 0; i < fileNames.length; i++) {
				String[] dateAndId = new String[3];
				/* split the childid(0),date(1),filetype(2) */
				dateAndId = fileNames[i].split("_");
				// check if childid(from filename)==childid(from parent list)
				// and date(entered)==date(report generated)
				if ((Integer.parseInt(dateAndId[0]) == childid) && dateAndId[1].equals(this.date)) {
					// file to attach with mail
					String attachment = this.directorylocation + fileNames[i];
					String emailid = parentnameplate.getEmailid();
					System.out.println(emailid + " :is queued " + "  for child id: " + childid);
					String messagebody = "Dear " + parentnameplate.getName() + " " + this.messageBody;
					MailSender mailsender = new MailSender(this.messageHeadading, messagebody, emailid, attachment);
					mailsender.sendMail();

					break;
				} else {// when entered date is wrong
					if (i == (fileNames.length - 1)) {
						System.out.println(
								"Either date format is Wrong or Report is not available for: " + childid + " child");
					}
				}
			}
		}

	}

	private ArrayList<ParentNamePlate> getAllParentsNameplate() {
		ArrayList<ParentNamePlate> listofparentnameplate = new ArrayList<ParentNamePlate>();
		String sql = "Select distinct con.emailid,p.name,c.idchild from contact con join parent p join child_info c on(con.fk_idparent=p.idparent and c.fk_idparent=p.idparent);";
		try {
			listofparentnameplate.addAll(dbConnector.getParentNameplate(sql));
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return listofparentnameplate;

	}

	@SuppressWarnings("null")
	// this method returns all files in the directory in string array
	private String[] getFiles(File f) {
		File[] files;
		files = f.listFiles();// extract all files in array
		String[] fileNames = new String[files.length];

		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile())
				fileNames[i] = files[i].getAbsoluteFile().getName();

		}
		return fileNames;
	}

}
