package cdccm.servicesimpl;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import cdccm.dbServices.MySQLDBConnector;
import cdccm.pojo.ActivityPOJO;
import cdccm.pojo.AssignActivityPOJO;
import cdccm.pojo.CareProviderPOJO;
import cdccm.pojo.ChildIdAgeGroupId;
import cdccm.pojo.ChildPOJO;
import cdccm.pojo.ChildReportPOJO;
import cdccm.pojo.ContactPOJO;
import cdccm.pojo.FoodPOJO;
import cdccm.pojo.ParentPOJO;
import cdccm.pojo.ProviderFeedbackPOJO;
import cdccm.pojo.SchedulePOJO;
import cdccm.serviceApi.AdminService;
import cdccm.utilities.CdccmUtilities;
import cdccm.utilities.FileNameGenerator;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;

public class AdminServiceImpl implements AdminService {

	private MySQLDBConnector dbConnector;
	Scanner inputChoice = new Scanner(System.in);

	public AdminServiceImpl() {
		dbConnector = MySQLDBConnector.getInstance();
	}

	@Override

	public boolean insertChildDetails(ParentPOJO parentpojo) {
		/* take list of children added before */
		List<ChildPOJO> children = parentpojo.getChild();
		Iterator<ChildPOJO> it = children.iterator();
		while (it.hasNext()) {
			ChildPOJO child = (ChildPOJO) it.next();
			try {
				int age = CdccmUtilities.getAge(child.getDob());
				int ageGroup = CdccmUtilities.getAge(child.getDob());
				if (age < 0 || ageGroup > 4) {
					System.out.println("Wrong Date of birth");
					/* wrong date has gone so remove parent */
					boolean isalldeleted = this.clearTheReferencedData();
					return false;
				}
				int resultCountChild = dbConnector
						.insert("INSERT INTO CHILD_INFO(name,surname,dob,age,fk_age_group,fk_idparent) VALUES('"
								+ child.getFirst_name() + "','" + child.getLast_name() + "','" + child.getDob() + "','"
								+ age + "','" + ageGroup + "'," + "(SELECT MAX(IDPARENT) from PARENT)" + ")");

				if (resultCountChild > 0)
					System.out.println("Child Record Inserted Successfully");
				else {
					boolean alldeleted = this.clearTheReferencedData();
					if (alldeleted) {
						return false;
					} else
						System.out.println("Can Not Clear referenced data from PARENT & CONTACT tables");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	public boolean insertParentDetails(ParentPOJO parentPOJO) {
		try {
			List<ContactPOJO> contactpojo = parentPOJO.getContact();
			int resultCountContact = 0;
			int resultCountParent = dbConnector.insert("INSERT INTO PARENT(name, surname) VALUES('"
					+ parentPOJO.getParentFirst_name() + "','" + parentPOJO.getParentLast_name() + "')");
			Iterator<ContactPOJO> it = contactpojo.iterator();
			while (it.hasNext()) {
				ContactPOJO contact = (ContactPOJO) it.next();
				resultCountContact = dbConnector
						.insert("INSERT INTO CONTACT(street,city,pincode,phone_number,emailid,fk_idparent) VALUES('"
								+ contact.getStreet() + "','" + contact.getCity() + "','" + contact.getPincode() + "','"
								+ contact.getPhoneNumber() + "','" + contact.getEmail() + "',"
								+ "(SELECT MAX(IDPARENT) from PARENT)" + ")");
			}
			if ((resultCountParent > 0) && (resultCountContact > 0)) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<ChildPOJO> listAllChild() {
		List<ChildPOJO> childrenList = new ArrayList<>();
		ResultSet resultSetChildren = null;
		try {
			resultSetChildren = dbConnector.query("SELECT IDCHILD, NAME, SURNAME, DOB, AGE FROM CHILD_INFO");
			while (resultSetChildren.next()) {
				childrenList.add(new ChildPOJO(resultSetChildren.getInt(1), resultSetChildren.getString(2),
						resultSetChildren.getString(3), resultSetChildren.getDate(4).toString(),
						resultSetChildren.getInt(5)));
			}
		} catch (SQLException e) {
			System.out.println("Problem In retriving Child Info " + e);
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
		int ageGroup, session, resultCountAssignActivity;
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
							resultCountAssignActivity = dbConnector
									.insert("insert into report (fk_idchild,fk_idagegroup) VALUES("
											+ chilIdList.getString("IDCHILD") + ","
											+ chilIdList.getString("fk_age_group") + ")");
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
							resultCountAssignActivity = dbConnector
									.insert("insert into report (fk_idchild,fk_idagegroup) VALUES("
											+ chilIdList.getString("IDCHILD") + ","
											+ chilIdList.getString("fk_age_group") + ")");
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
							resultCountAssignActivity = dbConnector
									.insert("insert into report (fk_idchild,fk_idagegroup) VALUES("
											+ chilIdList.getString("IDCHILD") + ","
											+ chilIdList.getString("fk_age_group") + ")");
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
		ResultSet resultSet = null;

		int recordInsert;
		try {
			String query = "select idactivity,activity_name, fk_idcareprovider,activity_description from activity where fk_age_group = ? AND fk_session = ? ";
			resultSet = dbConnector.assignActivity(query, assignActivityPOJO.getAgeGroup(),
					assignActivityPOJO.getSession());
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
							+ assignActivityPOJO.getChildID() + "','" + assignActivityPOJO.getAgeGroup() + "','"
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

	@SuppressWarnings("unused")
	@Override
	public void updateChildInfo(int childId, ChildPOJO childPOJO) {
		try {

			int resultUpdate = 0;
			String updateQuery = "";
			childPOJO.setAge(CdccmUtilities.getAge(childPOJO.getDob()));
			int ageGroup = CdccmUtilities.getAge(childPOJO.getDob());
			if (childPOJO.getAge() < 0 || ageGroup > 4) {
				System.out.println("Child Is Either Too Young Or Too Old For Day Care!! \n");
			} else {
				String query = "Update CHILD_INFO SET NAME = ?,  surname = ?, dob = ?,  age = ?,  fk_age_group = ?  WHERE idchild = ? ";
				resultUpdate = dbConnector.updateChild(query, childPOJO, ageGroup, childId);
			}
			if (resultUpdate > 0) {
				System.out.println("Child Record Updated!! \n");
			} else
				System.out.println("Error Occured, Record Not Updated");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void updateParentInfo(int parentId, ParentPOJO parentPOJO) {
		int resultUpdate = 0;
		String query = "UPDATE PARENT SET name = ?, surname = ? WHERE IDPARENT = ? ";
		resultUpdate = dbConnector.updateParent(query, parentPOJO, parentId);
		if (resultUpdate > 0) {
			System.out.println("Parent Record Updated!!\n");
		} else
			System.out.println("Error Occured, Record Not Updated");
	}

	public void updateContactInfo(int parentId, ContactPOJO contactPOJO) {
		int resultUpdate = 0;
		String query = "UPDATE CONTACT SET street = ?, city = ? , pincode = ?, phone_number = ? , emailid = ? WHERE FK_IDPARENT = ? ";
		resultUpdate = dbConnector.updateContact(query, contactPOJO, parentId);
		if (resultUpdate > 0) {
			System.out.println("Contact Updated!!\n");
		} else
			System.out.println("Error Occured, Record Not Updated");
	}

	@Override
	public void updateCareProviderInfo(int careProviderId, CareProviderPOJO careProviderPOJO) {
		int resultUpdate = 0;
		String query = "UPDATE CARE_PROVIDER SET name = ?,emailid = ?, phone_number = ? Where idcare_provider = ? ";
		resultUpdate = dbConnector.updateCareProvider(query, careProviderPOJO, careProviderId);
		if (resultUpdate > 0) {
			System.out.println("Care Provider Record Updated!!\n");
		} else
			System.out.println("Error Occured, Record Not Updated");
	}

	@Override
	public void generateReport(int childid) {
		// TODO Auto-generated method stub

	}

	@Override

	public void updateActivityToChild(AssignActivityPOJO assignActivityPOJO) {

		try {
			System.out.println("this is first line");
			ResultSet singleChild, resultSetActivity = null;
			String query = "SELECT FK_AGE_GROUP FROM CHILD_INFO WHERE IDCHILD = ?";
			String activityQuery = "select idactivity,activity_name,fk_idcareprovider,activity_description from activity where fk_age_group = ? AND fk_session = ? ";
			String updateReport = "UPDATE REPORT SET fk_idactivity =  ? , fk_idprovider= ? WHERE fk_idchild = ? AND fk_idsession = ?";
			singleChild = dbConnector.displayInfo(query, assignActivityPOJO.getChildID());
			if (singleChild.next()) {
				assignActivityPOJO.setAgeGroup(singleChild.getInt("FK_AGE_GROUP"));
				resultSetActivity = dbConnector.selectActivity(activityQuery, assignActivityPOJO);
				System.out.println("\n------------Activities and Care Provider Available For Your Child----------");
				while (resultSetActivity.next()) {
					System.out.println("Activity ID: " + resultSetActivity.getInt("idactivity"));
					System.out.println("Activity Name: " + resultSetActivity.getString("activity_name"));
					System.out.println("Care Provider ID: " + resultSetActivity.getInt("fk_idcareprovider"));
					System.out.println("Date Of Birth: " + resultSetActivity.getString("activity_description"));
				}
				System.out.println("\nSelect Activity ID available for your child");
				assignActivityPOJO.setActivityID(inputChoice.nextInt());
				System.out.println("\nSelect Care Provider ID available for your child");
				assignActivityPOJO.setCareProviderID(inputChoice.nextInt());
				int result = dbConnector.updateReport(updateReport, assignActivityPOJO);
				if (result > 0) {
					System.out.println("Activity Updated!!");
				} else {
					System.out.println("Activity Not Updated!!");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ChildPOJO displayChild(int id) {
		ResultSet resultSetchildData = null;
		ChildPOJO childObj = new ChildPOJO();
		try {
			String query = "SELECT name, surname, dob, age  FROM CHILD_INFO WHERE IDCHILD = ? ";
			resultSetchildData = dbConnector.displayInfo(query, id);
			if (resultSetchildData.next()) {
				childObj.setFirst_name(resultSetchildData.getString("name"));
				childObj.setLast_name(resultSetchildData.getString("surname"));
				childObj.setDob(resultSetchildData.getString("dob"));
				childObj.setAge(resultSetchildData.getInt("age"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return childObj;
	}

	@Override
	public ParentPOJO displayParent(int id) {
		ResultSet resultSetParent = null;
		ParentPOJO parentObject = null;
		List<ContactPOJO> listOfContacts = new ArrayList<>();
		try {
			String query = "SELECT p.name, p.surname, c.street, c.city, c.pincode, c.phone_number, c.emailid FROM PARENT p JOIN CONTACT c on p.idparent=c.fk_idparent WHERE idparent =  ? ";
			resultSetParent = dbConnector.displayInfo(query, id);
			if (resultSetParent.next()) {
				parentObject = new ParentPOJO();
				parentObject.setParentFirst_name(resultSetParent.getString("name"));
				parentObject.setParentLast_name(resultSetParent.getString("surname"));
				while (resultSetParent.next()) {
					listOfContacts.add(new ContactPOJO(resultSetParent.getString(3), resultSetParent.getString(4),
							resultSetParent.getInt(5), resultSetParent.getString(6), resultSetParent.getString(7)));
				}
				parentObject.setContact(listOfContacts);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return parentObject;
	}

	@Override
	public CareProviderPOJO displayCareProvider(int id) {
		CareProviderPOJO careProviderObj = new CareProviderPOJO();
		ResultSet resultSetProvider = null;
		try {
			String query = "SELECT idcare_provider, name, emailid, phone_number FROM CARE_PROVIDER WHERE IDCARE_PROVIDER = ?";
			resultSetProvider = dbConnector.displayInfo(query, id);
			if (resultSetProvider.next()) {
				careProviderObj.setName(resultSetProvider.getString("name"));
				careProviderObj.setEmail(resultSetProvider.getString("emailid"));
				careProviderObj.setPhoneNumber(resultSetProvider.getString("phone_number"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return careProviderObj;
	}

	@Override
	public void provideFeedback(ProviderFeedbackPOJO providerFeedbackPOJO) {
		try {
			int feedback = dbConnector
					.insert("INSERT INTO FEEDBACK(FK_IDPARENT,FK_IDCARE_PROVIDER,PARENT_FEEDBACK) VALUES('"
							+ providerFeedbackPOJO.getParentId() + "','" + providerFeedbackPOJO.getCareProviderId()
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

	@Override
	public void insertMealDetails(List<FoodPOJO> foodlist) {
		int resultCountFood;
		Iterator it = foodlist.iterator();
		while (it.hasNext()) {
			FoodPOJO foodobj = (FoodPOJO) it.next();
			try {
				resultCountFood = dbConnector.insert("INSERT INTO FOOD(day, breakfast, lunch,snak) VALUES('"
						+ foodobj.getDay() + "','" + foodobj.getBreakfast() + "','" + foodobj.getLunch() + "','"
						+ foodobj.getSnack() + "')");
				if ((resultCountFood > 0))
					System.out.println("Food Record Inserted Successfully\n");
				else
					System.out.println("Error Inserting Record Please Try Again\n");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void updateFood(FoodPOJO foodPOJO) {
		int resultUpdate = 0;

		String column_to_set, query_aux;

		if (foodPOJO.getBreakfast() != null) {
			column_to_set = "breakfast";
			query_aux = foodPOJO.getBreakfast();
		} else if (foodPOJO.getLunch() != null) {
			column_to_set = "lunch";
			query_aux = foodPOJO.getLunch();
		} else {
			query_aux = foodPOJO.getSnack();
			column_to_set = "snak";
		}

		String updateQuery = "UPDATE food SET " + column_to_set;
		updateQuery = updateQuery + " = '" + query_aux + "' WHERE day = '" + foodPOJO.getDay() + "'";

		try {
			resultUpdate = dbConnector.insert(updateQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resultUpdate > 0) {
			System.out.println("Care Food Record Updated!!\n");
		} else
			System.out.println("Error Occured, Record Not Updated");

	}

	@Override
	public void deleteMealDay(FoodPOJO foodPOJO) {
		int resultUpdate = 0;
		String updateQuery = "DELETE FROM FOOD ";
		updateQuery = updateQuery + " WHERE day = '" + foodPOJO.getDay() + "'";

		try {
			resultUpdate = dbConnector.delete(updateQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (resultUpdate > 0) {
			System.out.println("Food Deleted correctly!!\n");
		} else
			System.out.println("Error Occured, Record Not Updated");

	}

	@Override
	public void GenerateScheduleReport() throws SQLException {

		Set<ChildIdAgeGroupId> chid_ageid = getAvailableChilden();
		// above method takes all children from report table
		Collection<SchedulePOJO> schedule = null;
		ResultSet resultset = null;
		/* call to prcedure where it will compose the schedule for child */
		String sql = "{call child_care.update_plan_for_astudent(?, ?)}";
		Iterator<ChildIdAgeGroupId> it = chid_ageid.iterator();

		while (it.hasNext()) {
			ChildIdAgeGroupId cag = it.next();

			// get composed schedule
			resultset = dbConnector.callProcedure(sql, cag.getChildid(), cag.getAgegroupid());
			schedule = new ArrayList<>();

			while (resultset.next()) {// create list of schedule objs.
				schedule.add(new SchedulePOJO(resultset.getString(1), resultset.getString(2)));
			}
			printScheduleReport(schedule, cag.getChildid());
		}
	}

	protected List<FoodPOJO> extractfood() throws SQLException {
		List<FoodPOJO> listOffood = new ArrayList<>();
		ResultSet resultset = null;
		String sqlforfood = "select * from FOOD";
		try {
			resultset = dbConnector.query(sqlforfood);
		} catch (SQLException e1) {
			System.out.println("Problem in extracting food");
		}
		while (resultset.next()) {
			listOffood.add(new FoodPOJO(resultset.getString(1), resultset.getString(2), resultset.getString(3),
					resultset.getString(4)));
		}
		return listOffood;
	}

	protected List<ActivityPOJO> getActivityDescription(int childid) throws SQLException {
		ResultSet resultset = null;

		List<ActivityPOJO> activityDescList = new ArrayList<>();
		String sqlforactivityDesc = "select r.fk_idsession,a.activity_name, a.activity_description "
				+ "from activity a join report r " + "on(a.idactivity=r.fk_idactivity) " + "where r.fk_idchild=?;";
		try {
			resultset = dbConnector.getReport(sqlforactivityDesc, childid);
		} catch (SQLException e) {
			System.out.println("No Activity Description found");
		}
		while (resultset.next()) {
			ActivityPOJO activity = new ActivityPOJO();
			activity.setSession(resultset.getInt(1));
			activity.setName(resultset.getString(2));
			activity.setDescription(resultset.getString(3));
			activityDescList.add(activity);
		}
		return activityDescList;
	}

	private boolean clearTheReferencedData() throws SQLException {
		ResultSet resultset = dbConnector.query("SELECT MAX(idparent) from PARENT");
		if (resultset.next()) {
			int parent2delete = resultset.getInt(1);
			int deltedrows = 0;
			resultset = dbConnector.query("SELECT idchild from child_info where fk_idparent=" + parent2delete);
			// delete the parent and contact only when no first child is already
			// inserted
			if (!resultset.next()) {// first child is not there so we can delete
									// parent
				System.out.println("parent to delete: " + parent2delete);
				deltedrows = dbConnector.delete("DELETE FROM contact where fk_idparent=" + parent2delete);

				System.out.println("deleted contacts: " + deltedrows);
				if (deltedrows > 0) {
					deltedrows = 0;
					System.out.println("Contacts Deleted...");
					deltedrows = dbConnector.delete("DELETE FROM parent where idparent=" + parent2delete);
					System.out.println("deleted parent: " + deltedrows);
					if (deltedrows > 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private void printScheduleReport(Collection<SchedulePOJO> schedule, int childid) throws SQLException {
		System.out.println("Inside Printreport");
		ReportFiller reportfiller = new ReportFiller(schedule);
		try {
			JasperPrint jp = reportfiller.getReport("schedulereport".toString(), childid);
			JasperViewer jasperViewer = new JasperViewer(jp);
			jasperViewer.setVisible(true);

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jp));

			/* util method for creating file with date and rollno name */
			FileNameGenerator filenamegererator = new FileNameGenerator(schedule, childid);
			File file = filenamegererator.generateUniqueFileName("schedulereport".toString());

			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			configuration.setMetadataAuthor("chetan"); // set some
														// config as we like
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		} catch (JRException | ColumnBuilderException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	private void printPerformanceReport(Collection<ChildReportPOJO> subsetoflistofscore, int childid)
			throws SQLException {

		ReportFiller reportfiller = new ReportFiller(subsetoflistofscore);

		try {
			// the argument zero has no meaning just to satisfy the signature
			JasperPrint jp = reportfiller.getReport("performancereport".toString(), 0);

			JasperViewer jasperViewer = new JasperViewer(jp);
			jasperViewer.setVisible(true);
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setExporterInput(new SimpleExporterInput(jp));
			// the second field (0) here has no meaning just to satisfy
			// signatuere
			FileNameGenerator filenamegererator = new FileNameGenerator(subsetoflistofscore, 0);

			// util method for creating file with date name
			File file = filenamegererator.generateUniqueFileName("performancereport".toString());

			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(file));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			// set some config as we like
			configuration.setMetadataAuthor("chetan");

			exporter.setConfiguration(configuration);
			exporter.exportReport();
		} catch (JRException | ColumnBuilderException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

	}

	private Set<ChildIdAgeGroupId> getAvailableChilden() {
		// This supportive method returns the availlable childId and GroupId
		// from report(where schedule is already made) table
		Set<ChildIdAgeGroupId> chid_ageid = new HashSet<>();

		final String sql = "select distinct fk_idchild,fk_idagegroup from report group by fk_idchild;";

		try {
			ResultSet resultset = dbConnector.query(sql);
			while (resultset.next()) {
				chid_ageid.add(new ChildIdAgeGroupId(resultset.getInt(1), resultset.getInt(2)));
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return chid_ageid;
	}

	@Override
	public void dumpReportToArchive() {
		System.out.println("Dumping Report to Archive");
		String sqldump = "insert into archive select * from report";
		String sqlclear = "Delete from report";
		try {
			ResultSet resultset = dbConnector.query(sqldump);
			if (resultset.next()) {
				resultset = null;
				System.out.println("Data Has been Dumpped Now Report table is clearing....");
				resultset = dbConnector.query(sqldump);
				if (resultset.next()) {
					System.out.println("****Report Table cleared****");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void generateBulckPerformanceReport() {
		// TODO Auto-generated method stub

	}

}
