package cdccm.servicesimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;

import cdccm.dbServices.MySQLDBConnector;
import cdccm.pojo.AssignActivityPOJO;
import cdccm.pojo.CareProviderPOJO;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ContactPOJO;
import cdccm.pojo.ParentPOJO;
import cdccm.pojo.ProviderFeedbackPOJO;
import cdccm.serviceApi.AdminService;
import cdccm.utilities.CdccmUtilities;

public class AdminServiceImpl implements AdminService {
	Scanner inputChoice = new Scanner(System.in);
	private MySQLDBConnector dbConnector;

	public AdminServiceImpl() {
		dbConnector = MySQLDBConnector.getInstance();
	}

	@Override
	public void insertChildDetails(ChildPOJO childPOJO) {
		try {
			int age = CdccmUtilities.getAge(childPOJO.getDob());
			int ageGroup = CdccmUtilities.getAge(childPOJO.getDob());

			int resultCountChild = dbConnector
					.insert("INSERT INTO CHILD_INFO(name,surname,dob,age,fk_age_group,fk_idparent) VALUES('"
							+ childPOJO.getFirst_name() + "','" + childPOJO.getLast_name() + "','" + childPOJO.getDob()
							+ "','" + age + "','" + ageGroup + "'," + "(SELECT MAX(IDPARENT) from PARENT)" + ")");

			if (resultCountChild > 0)
				System.out.println("Child Record Inserted Successfully");
			else
				System.out.println("Error Inserting Record Please Try Again");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void insertParentDetails(ParentPOJO parentPOJO, ContactPOJO contactPOJO) {
		try {
			int resultCountParent = dbConnector.insert("INSERT INTO PARENT(name, surname) VALUES('"
					+ parentPOJO.getParentFirst_name() + "','" + parentPOJO.getParentLast_name() + "')");

			int resultCountContact = dbConnector
					.insert("INSERT INTO CONTACT(street,city,pincode,phone_number,emailid,fk_idparent) VALUES('"
							+ contactPOJO.getStreet() + "','" + contactPOJO.getCity() + "','" + contactPOJO.getPincode()
							+ "','" + contactPOJO.getPhoneNumber() + "','" + contactPOJO.getEmail() + "',"
							+ "(SELECT MAX(IDPARENT) from PARENT)" + ")");

			if ((resultCountParent > 0) && (resultCountContact > 0))
				System.out.println("Parent Record Inserted Successfully");
			else
				System.out.println("Error Inserting Record Please Try Again");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ResultSet listAllChild() throws SQLException {
		ResultSet childrenList = null;
		try {
			childrenList = dbConnector.query("SELECT * FROM CHILD_INFO");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return childrenList;
	}

	@Override
	public void insertCareProvider(CareProviderPOJO careProviderPOJO) {

		int resultCountProvider;
		try {
			resultCountProvider = dbConnector.insert(
					"INSERT INTO CARE_PROVIDER(name, emailid, phone_number) VALUES('" + careProviderPOJO.getName()
							+ "','" + careProviderPOJO.getEmail() + "','" + careProviderPOJO.getPhoneNumber() + "')");
			if ((resultCountProvider > 0))
				System.out.println("Care Provider Record Inserted Successfully\n");
			else
				System.out.println("Error Inserting Record Please Try Again\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void assignActivitiesToChildren() {
		int ageGroup, session;
		String childId = "";
		ResultSet chilIdList;
		try {
			for (ageGroup = 1; ageGroup <= 3; ageGroup++) {
				chilIdList = dbConnector
						.query("SELECT idchild,fk_age_group FROM CHILD_INFO where fk_age_group=" + ageGroup);
				while (chilIdList.next()) {
					if (ageGroup == 1) {
						for (session = 1; session < 4; session++) {
							childId = chilIdList.getString("IDCHILD");
							dbConnector.insert("update report set fk_idsession = " + session + " where fk_idagegroup = "
									+ ageGroup + " AND fk_idchild = " + chilIdList.getString("IDCHILD")
									+ " AND fk_idsession = 0" + ";");
							if (session == 1) {
								dbConnector
										.insert("update report set fk_idprovider = 1, fk_idactivity = 1 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							} else if (session == 2) {
								dbConnector
										.insert("update report set fk_idprovider = 3, fk_idactivity = 3 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							} else if (session == 3) {
								dbConnector
										.insert("update report set fk_idprovider = 5, fk_idactivity = 5 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							}
						}
					}
					if (ageGroup == 2) {
						for (session = 1; session <= 3; session++) {
							childId = chilIdList.getString("IDCHILD");
							dbConnector.insert("update report set fk_idsession = " + session + " where fk_idagegroup = "
									+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = 0" + ";");
							if (session == 1) {
								dbConnector
										.insert("update report set fk_idprovider = 2, fk_idactivity = 8 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							} else if (session == 2) {
								dbConnector
										.insert("update report set fk_idprovider = 4, fk_idactivity = 10 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							} else if (session == 3) {
								dbConnector
										.insert("update report set fk_idprovider = 6, fk_idactivity = 12 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							}
						}
					}
					if (ageGroup == 3) {
						for (session = 1; session <= 3; session++) {
							childId = chilIdList.getString("IDCHILD");
							dbConnector.insert("update report set fk_idsession = " + session + " where fk_idagegroup = "
									+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = 0" + ";");
							if (session == 1) {
								dbConnector
										.insert("update report set fk_idprovider = 2, fk_idactivity = 14 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							} else if (session == 2) {
								dbConnector
										.insert("update report set fk_idprovider = 4, fk_idactivity = 16 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							} else if (session == 3) {
								dbConnector
										.insert("update report set fk_idprovider = 6, fk_idactivity = 18 WHERE fk_idagegroup = "
												+ ageGroup + " AND fk_idchild = " + childId + " AND fk_idsession = "
												+ session + ";");
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void assignActivityToChild(AssignActivityPOJO assignActivityPOJO) {

		int recordInsert;
		try {
			ResultSet resultSet = dbConnector
					.query("select idactivity,activity_name, fk_idcareprovider,activity_description from activity where fk_age_group ="
							+ assignActivityPOJO.getAgeGroup() + " AND fk_session = "
							+ assignActivityPOJO.getSession());
			System.out.println("\n------------Activities and Care Provider Available For Child----------\n");
			while (resultSet.next()) {
				System.out.println("Activity ID: " + resultSet.getInt("idactivity"));
				System.out.println("Activity Name: " + resultSet.getString("activity_name"));
				System.out.println("Care Provider ID: " + resultSet.getInt("fk_idcareprovider"));
				System.out.println("Date Of Birth: " + resultSet.getString("activity_description"));
				System.out.println("");
			}
			System.out.println("\nSelect Activity ID available for your child");
			assignActivityPOJO.setActivityID(inputChoice.nextInt());
			System.out.println("\nSelect Care Provider ID available for your child");
			assignActivityPOJO.setCareProviderID(inputChoice.nextInt());
			recordInsert = dbConnector
					.insert("Insert INTO REPORT (fk_idchild,fk_idagegroup,fk_idactivity,fk_idprovider,fk_idsession) VALUES('"
							+ assignActivityPOJO.getSession() + "','" + assignActivityPOJO.getAgeGroup() + "','"
							+ assignActivityPOJO.getActivityID() + "','" + assignActivityPOJO.getCareProviderID()
							+ "','" + assignActivityPOJO.getSession() + "')");
			if (recordInsert > 0) {
				System.out.println("Record Inserted In Report Table\n");
			} else {
				System.out.println("Record Not Inserted\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void selectReport() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectSchedule() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectNewsEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateChildInfo(int childId, ChildPOJO childPOJO) {
		try {
			int resultUpdate = 0;
			int ageGroup;
			String updateQuery = "";
			childPOJO.setAge(CdccmUtilities.getAge(childPOJO.getDob()));
			if (childPOJO.getAge() < 2) {
				ageGroup = 1;
			} else if (childPOJO.getAge() < 3) {
				ageGroup = 2;
			} else {
				ageGroup = 3;
			}
			updateQuery = "UPDATE CHILD_INFO SET ";
			// if(childPOJO.getFirst_name() != null){
			// updateQuery = updateQuery + "name = " +
			// childPOJO.getFirst_name();
			// }
			// if(childPOJO.getLast_name() != null){
			// updateQuery = updateQuery + "surname = " +
			// childPOJO.getLast_name();
			// }
			// if(childPOJO.getDob() != null){
			// updateQuery = updateQuery + "dob = " + childPOJO.getDob();
			// }
			updateQuery = updateQuery + "name = '" + childPOJO.getFirst_name() + "', surname = '"
					+ childPOJO.getLast_name() + "', dob = '" + childPOJO.getDob() + "', age = " + childPOJO.getAge()
					+ ", fk_age_group = " + ageGroup + " WHERE idchild = " + childId;
			resultUpdate = dbConnector.insert(updateQuery);
			if (resultUpdate > 0) {
				System.out.println("Child Record Updated!! \n");
			} else
				System.out.println("Error Occured, Record Not Updated");

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateParentInfo(int parentId, ParentPOJO parentPOJO) {
		int resultUpdate = 0;
		try {
			String updateQuery = "UPDATE PARENT SET ";
			updateQuery = updateQuery + "name = '" + parentPOJO.getParentFirst_name() + "', surname = '"
					+ parentPOJO.getParentLast_name() + "' WHERE IDPARENT = " + parentId;
			resultUpdate = dbConnector.insert(updateQuery);
			if (resultUpdate > 0) {
				System.out.println("Parent Record Updated!!\n");
			} else
				System.out.println("Error Occured, Record Not Updated");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateContactInfo(int parentId, ContactPOJO contactPOJO) {
		int resultUpdate = 0;
		try {
			String updateQuery = "UPDATE CONTACT SET ";
			updateQuery = updateQuery + "street = '" + contactPOJO.getStreet() + "', city = '" + contactPOJO.getCity()
					+ "', pincode = " + contactPOJO.getPincode() + ", phone_number = '" + contactPOJO.getPhoneNumber()
					+ "', emailid = '" + contactPOJO.getEmail() + "'";
			resultUpdate = dbConnector.insert(updateQuery);
			if (resultUpdate > 0) {
				System.out.println("Contact Updated!!\n");
			} else
				System.out.println("Error Occured, Record Not Updated");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateCareProviderInfo(int careProviderId, CareProviderPOJO careProviderPOJO) {
		int resultUpdate = 0;
		try {
			String updateQuery = "UPDATE CARE_PROVIDER SET ";
			updateQuery = updateQuery + "name = '" + careProviderPOJO.getName() + "' , emailid = '"
					+ careProviderPOJO.getEmail() + "', phone_number = '" + careProviderPOJO.getPhoneNumber()
					+ "' WHERE idcare_provider = " + careProviderId;
			resultUpdate = dbConnector.insert(updateQuery);
			if (resultUpdate > 0) {
				System.out.println("Care Provider Record Updated!!\n");
			} else
				System.out.println("Error Occured, Record Not Updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generateReport(int childid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateActivityToChild(AssignActivityPOJO assignActivityPOJO) {
		String updateQuery = "";
		try {
			ResultSet resultSet = dbConnector.query("select * from activity where fk_age_group ="
					+ assignActivityPOJO.getAgeGroup() + " AND fk_session = " + assignActivityPOJO.getSession());
			System.out.println("\n------------Activities and Care Provider Available For Your Child----------");
			while (resultSet.next()) {
				System.out.println("Activity ID: " + resultSet.getInt("idactivity"));
				System.out.println("Activity Name: " + resultSet.getString("activity_name"));
				System.out.println("Care Provider ID: " + resultSet.getInt("fk_idcareprovider"));
				System.out.println("Date Of Birth: " + resultSet.getString("activity_description"));
			}

			System.out.println("\nSelect Activity ID available for your child");
			assignActivityPOJO.setActivityID(inputChoice.nextInt());
			System.out.println("\nSelect Care Provider ID available for your child");
			assignActivityPOJO.setCareProviderID(inputChoice.nextInt());
			updateQuery = "UPDATE REPORT SET fk_idactivity = " + assignActivityPOJO.getActivityID()
					+ ", fk_idprovider= " + assignActivityPOJO.getCareProviderID() + " WHERE fk_idchild = "
					+ assignActivityPOJO.getChildID() + " AND fk_idsession = " + assignActivityPOJO.getSession();
			dbConnector.insert(updateQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean displayInfo(int id, String tableName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean displayChild(int id) {
		ResultSet resultSetChild = null;
		boolean recordExists = false;
		try {
			resultSetChild = dbConnector.query("SELECT * FROM CHILD_INFO WHERE IDCHILD = " + id);
			if (resultSetChild.next()) {
				System.out.println("Child ID: " + resultSetChild.getString("idchild"));
				System.out.println("First Name: " + resultSetChild.getString("name"));
				System.out.println("Last Name: " + resultSetChild.getString("surname"));
				System.out.println("Date Of Birth: " + resultSetChild.getString("dob"));
				System.out.println("Age:" + resultSetChild.getString("age"));
				System.out.println("Age:" + resultSetChild.getString("fk_age_group"));
				recordExists = true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recordExists;
	}

	@Override
	public boolean displayParent(int id) {

		boolean recordExists = false;
		try {
			ResultSet resultSetParent = dbConnector.query("SELECT * FROM PARENT WHERE IDPARENT = " + id);

			if (resultSetParent.next()) {
				System.out.println("Parent First Name: " + resultSetParent.getString("name"));
				System.out.println("Parent Last Name: " + resultSetParent.getString("surname"));
				recordExists = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recordExists;
	}

	@Override
	public boolean displayContact(int id) {
		boolean recordExists = false;
		try {
			ResultSet resultSetContact = dbConnector.query("SELECT * FROM CONTACT WHERE FK_IDPARENT = " + id);
			if (resultSetContact.next()) {
				System.out.println("Street Name: " + resultSetContact.getString("street"));
				System.out.println("City Name: " + resultSetContact.getString("city"));
				System.out.println("Pincode: " + resultSetContact.getString("pincode"));
				System.out.println("Phone Number: " + resultSetContact.getString("phone_number"));
				System.out.println("Email Id: " + resultSetContact.getString("emailid"));
				recordExists = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recordExists;
	}

	@Override
	public boolean displayCareProvider(int id) {

		boolean recordExists = false;
		try {
			ResultSet resultSetProvider = dbConnector
					.query("SELECT * FROM CARE_PROVIDER WHERE IDCARE_PROVIDER = " + id);
			if (resultSetProvider.next()) {
				System.out.println("Care Provider ID: " + resultSetProvider.getString("idcare_provider"));
				System.out.println("Name: " + resultSetProvider.getString("name"));
				System.out.println("Email Id: " + resultSetProvider.getString("emailid"));
				System.out.println("Phone number: " + resultSetProvider.getString("phone_number"));
				recordExists = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return recordExists;
	}

	@Override
	public void provideFeedback(ProviderFeedbackPOJO providerFeedbackPOJO) {
		try {
			int feedback = dbConnector
					.insert("INSERT INTO FEEDBACK(FK_IDPARENT,FK_IDCARE_PROVIDER,PARENT_FEEDBACK) VALUES('"
							+ providerFeedbackPOJO.getCareProviderId() + "','" + providerFeedbackPOJO.getParentId()
							+ "','" + providerFeedbackPOJO.getFeedback() + "')");
			if (feedback > 0) {
				System.out.println("Suggestions/Feedback Added Succesfully!!\n");
			} else {
				System.out.println("Suggestion/Feedback Not Added\n");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
